package lapr.project.data;

import lapr.project.model.Coordinate;
import lapr.project.model.Country;
import lapr.project.model.Location;
import lapr.project.model.Storage;
import lapr.project.model.graph.matrix.MatrixGraph;
import lapr.project.store.PortsGraph;
import oracle.ucp.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CountrySqlStore implements Persistable {

    /**
     * Saves the country to the database
     * @param databaseConnection the database's connection
     * @param object the object to be added
     * @return true if the country is added
     */
    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Country country = (Country) object;
        boolean returnValue = false;

        try {
            returnValue = saveCountryToDatabase(databaseConnection, country);;

        } catch (SQLException ex) {
            Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, ex.getMessage());
            databaseConnection.registerError(ex);
        }

        return returnValue;
    }

    /**
     * deletes a storage from database
     * @param databaseConnection the database's connection
     * @param object the object to be deleted
     * @return true if the country was deleted
     */
    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        boolean returnValue = false;
        Connection connection = databaseConnection.getConnection();
        Country country = (Country) object;

        try {
            String sqlCommand;

            sqlCommand = "delete from Country where country = ?";
            try (PreparedStatement deleteCountryPreparedStatement = connection.prepareStatement(sqlCommand)) {
                deleteCountryPreparedStatement.setString(1, country.getCountry());
                deleteCountryPreparedStatement.executeUpdate();
            }

            BorderSQLStore borderSQLStore = new BorderSQLStore();
            returnValue = borderSQLStore.delete(databaseConnection, country.getCountry());

            sqlCommand = "delete from Storage where country_name = ?";
            try (PreparedStatement deleteCountryPreparedStatement = connection.prepareStatement(sqlCommand)) {
                deleteCountryPreparedStatement.setString(1, country.getCountry());
                deleteCountryPreparedStatement.executeUpdate();
            }

        } catch (SQLException exception) {
            Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
        }

        return returnValue;
    }

    /**
     * Checks is a country is already registered on the database. If the country
     * is registered it doesn't add it.
     *
     * @param databaseConnection Connection to the Database
     * @param country            an object of type Storage
     * @throws SQLException in case something goes wrong during the Database connection
     */
    private boolean saveCountryToDatabase(DatabaseConnection databaseConnection, Country country) throws SQLException {
        boolean returnValue = false;

        if (!isCountryOnDataBase(databaseConnection, country)) {
            Connection connection = databaseConnection.getConnection();
            String sqlCommand = "INSERT INTO Country(continent,alpha2,alpha3,country,population,capital,latitude,longitude) VALUES(?,?,?,?,?,?,?,?)";
            try(PreparedStatement saveCountryPreparedStatement = connection.prepareStatement(sqlCommand)) {

                saveCountryPreparedStatement.setString(1, country.getContinent());
                saveCountryPreparedStatement.setString(2, country.getAlpha2());
                saveCountryPreparedStatement.setString(3, country.getAlpha3());
                saveCountryPreparedStatement.setString(4, country.getCountry());
                saveCountryPreparedStatement.setFloat(5, country.getPopulation());
                saveCountryPreparedStatement.setString(6, country.getCapital());
                saveCountryPreparedStatement.setFloat(7, country.getCoordinate().getLatitude());
                saveCountryPreparedStatement.setFloat(8, country.getCoordinate().getLongitude());
                saveCountryPreparedStatement.executeUpdate();
                returnValue = true;
            }
        }
        return returnValue;
    }

    /**
     * Checks if a given country is already registered in the database
     *
     * @param databaseConnection Connection to the Database
     * @param country            an object of type Country
     * @return true if the country already exists
     */
    private boolean isCountryOnDataBase(DatabaseConnection databaseConnection, Country country) {
        Connection connection = databaseConnection.getConnection();

        try {
            String sqlCommand = "select * from country where country = ?";

            try(PreparedStatement getCountryPreparedStatement = connection.prepareStatement(sqlCommand)) {
                getCountryPreparedStatement.setString(1, country.getCountry());
                try (ResultSet countryResultSet = getCountryPreparedStatement.executeQuery()) {
                    return countryResultSet.next();
                }
            }

        } catch (SQLException exception) {
            Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
            return false;
        }
    }

    public HashMap<String, PortsGraph> loadGraphMapByContinent(DatabaseConnection databaseConnection, int n) {
        Connection connection = databaseConnection.getConnection();
        HashMap<String, PortsGraph> graphMap = new HashMap<>();

        try {
            String sqlCommand = "select continent from country group by continent";

            try (PreparedStatement getContinentPreparedStatement = connection.prepareStatement(sqlCommand)) {
                try (ResultSet continentResultSet = getContinentPreparedStatement.executeQuery()) {
                    while (continentResultSet.next()) {
                        String continent = continentResultSet.getString(1);

                        sqlCommand = "select * from country where continent like '"+ continent +"'";

                        graphMap.put(continent, loadGraph(databaseConnection, n, sqlCommand));
                    }
                }
            }
        } catch (SQLException exception) {
            Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
            return null;
        }

        return graphMap;
    }

    public static PortsGraph loadGraph(DatabaseConnection databaseConnection, int n){
        return loadGraph(databaseConnection, n, "select * from country");
    }

    /**
     * loads the pathGraph information
     * @param databaseConnection the app's database connection
     */
    public static PortsGraph loadGraph(DatabaseConnection databaseConnection, int n, String sqlCommand){
        Connection connection = databaseConnection.getConnection();
        PortsGraph portsGraph = new PortsGraph();

        try {
            try(PreparedStatement getCountryPreparedStatement = connection.prepareStatement(sqlCommand)) {
                try (ResultSet countryResultSet = getCountryPreparedStatement.executeQuery()) {
                    while (countryResultSet.next()){
                        String continent = countryResultSet.getString(2);
                        String ct = countryResultSet.getString(1);
                        String alpha2 = countryResultSet.getString(4);
                        String alpha3 = countryResultSet.getString(5);
                        String capital = countryResultSet.getString(3);
                        float population = countryResultSet.getFloat(6);
                        float longitude = countryResultSet.getFloat(8);
                        float latitude = countryResultSet.getFloat(7);
                        Country country = new Country(continent, ct, new Coordinate(longitude, latitude), alpha2, alpha3, population, capital);
                        portsGraph.insertLocation(country);
                        portsGraph.insertPath(country, country, (double) 0);
                        loadPorts(databaseConnection, country, portsGraph);
                    }
                }
            }
            
            loadBorders(databaseConnection, portsGraph);
            loadPaths(databaseConnection, portsGraph, n);

            MainStorage.getInstance().setPortsGraph(portsGraph);
            return portsGraph;
            
        } catch (SQLException exception) {
            Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
            return null;
        }
    }

    /**
     * loads tha path's between the ports
     * @param databaseConnection the app's database connection
     * @param portsGraph the graph whose paths are being added
     */
    private static void loadBorders(DatabaseConnection databaseConnection, PortsGraph portsGraph) {
        Connection connection = databaseConnection.getConnection();

        try {
            String sqlCommand = "select * from Border";

            try(PreparedStatement getBorderPreparedStatement = connection.prepareStatement(sqlCommand)) {
                try (ResultSet borderResultSet = getBorderPreparedStatement.executeQuery()) {
                    while(borderResultSet.next())
                        portsGraph.insertCountryPath(borderResultSet.getString(1), borderResultSet.getString(2));
                }
            }

        } catch (SQLException exception) {
            Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
        }
    }

    /**
     * loads the connections between countries
     * @param databaseConnection the app's connection to the database
     * @param portsGraph the graph whose paths are being added
     * @param n
     */
    private static void loadPaths(DatabaseConnection databaseConnection, PortsGraph portsGraph, int n) {
        Connection connection = databaseConnection.getConnection();
        MatrixGraph<Location, Double> mg = portsGraph.getMg();

        for (Location location : mg.vertices()) {
            if (location instanceof Storage) {
                try {
                    String sqlCommand = "select * from storage_path where STORAGE_ID1 = ? or STORAGE_ID2 = ? order by distance";

                    try (PreparedStatement getBorderPreparedStatement = connection.prepareStatement(sqlCommand)) {
                        getBorderPreparedStatement.setInt(1, ((Storage) location).getIdentification());
                        getBorderPreparedStatement.setInt(2, ((Storage) location).getIdentification());
                        try (ResultSet borderResultSet = getBorderPreparedStatement.executeQuery()) {
                            int i = 0;
                            while (borderResultSet.next() && i < n) {
                               if(portsGraph.insertPortPath(borderResultSet.getInt(1), borderResultSet.getInt(2), borderResultSet.getDouble(3)))
                                   i++;
                            }
                        }
                    }

                } catch (SQLException exception) {
                    Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, null, exception);
                    databaseConnection.registerError(exception);
                }
            }
        }
    }

    /**
     * loads the ports from the database
     * @param databaseConnection the app's database connection
     * @param country the country which the ports belong to
     * @param portsGraph the graph which the ports will be added
     */
    private static void loadPorts(DatabaseConnection databaseConnection, Country country, PortsGraph portsGraph) {
        Connection connection = databaseConnection.getConnection();
        try {
            String sqlCommand = "select * from storage where country_name = ?";

            try(PreparedStatement getPortPreparedStatement = connection.prepareStatement(sqlCommand)) {
                getPortPreparedStatement.setString(1, country.getCountry());

                try (ResultSet countryResultSet = getPortPreparedStatement.executeQuery()) {
                    Pair<Double, Location> distance = new Pair<>(Double.MAX_VALUE, null);
                    List<Storage> list = new ArrayList<>();

                    while (countryResultSet.next()){
                        int identification = countryResultSet.getInt(1);
                        String name = countryResultSet.getString(4);
                        float longitude = countryResultSet.getFloat(7);
                        float latitude = countryResultSet.getFloat(6);

                        Storage storage = new Storage(identification, name, country.getContinent(), country.getCountry(), new Coordinate(longitude, latitude));

                        portsGraph.insertLocation(storage);
                        portsGraph.insertPath(storage, storage, (double) 0);


                        for (Storage storage2 : list) {
                            portsGraph.insertPortPath(storage.getIdentification(), storage2.getIdentification(), storage.distanceBetween(storage2));
                        }

                        list.add(storage);

                        double temp = country.distanceBetween(storage);
                        if (temp < distance.get1st())
                            distance = new Pair<>(temp, storage);
                    }


                    if (distance.get2nd() != null)
                        portsGraph.insertPath(country, distance.get2nd(), distance.get1st());
                }
            }

        } catch (SQLException exception) {
            Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);

        }
    }
}