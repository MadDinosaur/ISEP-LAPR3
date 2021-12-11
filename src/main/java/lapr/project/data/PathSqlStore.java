package lapr.project.data;

import oracle.ucp.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PathSqlStore implements Persistable {

    /**
     * saves the border to the database
     * @param databaseConnection the database's connection
     * @param object the object to be added
     * @return true if the border is added
     */
    @Override
    public boolean save(DatabaseConnection databaseConnection, Object object) {
        Pair<Pair<String, String>,String> path = (Pair<Pair<String, String>,String>) object;
        boolean returnValue = false;

        try {
            if (isStorageOnDataBase(databaseConnection, path.get1st()) && !isPathOnDataBase(databaseConnection, path.get1st())) {
                Connection connection = databaseConnection.getConnection();
                String sqlCommand = "insert into Storage_Path(storage_id1, storage_id2, distance) values(?,?,?)";
                try(PreparedStatement savePathPreparedStatement = connection.prepareStatement(sqlCommand)) {
                    savePathPreparedStatement.setString(1, path.get1st().get1st());
                    savePathPreparedStatement.setString(2, path.get1st().get2nd());
                    savePathPreparedStatement.setString(3, path.get2nd());
                    savePathPreparedStatement.executeUpdate();
                }
                returnValue = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, ex.getMessage());
            databaseConnection.registerError(ex);
        }

        return returnValue;
    }

    /**
     * deletes a border in the database
     * @param databaseConnection the database's connection
     * @param object the object to be deleted
     * @return true if the border is deleted
     */
    @Override
    public boolean delete(DatabaseConnection databaseConnection, Object object) {
        boolean returnValue = false;
        Connection connection = databaseConnection.getConnection();
        String country = (String) object;

        try {
            String sqlCommand;

            sqlCommand = "delete from Border where country_name = ? or country_border = ?";
            try (PreparedStatement deletePathPreparedStatement = connection.prepareStatement(sqlCommand)) {
                deletePathPreparedStatement.setString(1, country);
                deletePathPreparedStatement.setString(2, country);
                deletePathPreparedStatement.executeUpdate();
            }

            returnValue = true;

        } catch (SQLException exception) {
            Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
        }

        return returnValue;
    }

    /**
     * checks whether the border is already in the database
     * @param databaseConnection the database's connection
     * @param path an object with both the storages that make up the path
     * @return true if the border is already on the database
     */
    private boolean isPathOnDataBase(DatabaseConnection databaseConnection, Pair<String, String> path) {
        Connection connection = databaseConnection.getConnection();

        try {
            String sqlCommand = "select * from Storage_Path where (storage_id1 = ? and storage_id2 = ?) or (storage_id1 = ? and storage_id2 = ?)";

            try(PreparedStatement getPathPreparedStatement = connection.prepareStatement(sqlCommand)) {
                getPathPreparedStatement.setString(1, path.get1st());
                getPathPreparedStatement.setString(2, path.get2nd());
                getPathPreparedStatement.setString(3, path.get2nd());
                getPathPreparedStatement.setString(4, path.get1st());
                try (ResultSet pathResultSet = getPathPreparedStatement.executeQuery()) {
                    return pathResultSet.next();
                }
            }
        } catch (SQLException exception) {
            Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
            return false;
        }
    }

    /**
     * Checks if a given country is already registered in the database
     *
     * @param databaseConnection Connection to the Database
     * @param path an object with the storages compromising the path
     * @return true if the storage already exists
     */
    private boolean isStorageOnDataBase(DatabaseConnection databaseConnection, Pair<String, String> path) {
        Connection connection = databaseConnection.getConnection();

        try {
            String sqlCommand = "select * from storage where identification = ? or identification = ?";
            boolean returnValue = false;

            try (PreparedStatement getStoragesPreparedStatement = connection.prepareStatement(sqlCommand)) {
                getStoragesPreparedStatement.setString(1, path.get1st());
                getStoragesPreparedStatement.setString(2, path.get2nd());
                try (ResultSet storageResultSet = getStoragesPreparedStatement.executeQuery()) {
                    storageResultSet.next();
                    returnValue = storageResultSet.next();
                }
            }
            return returnValue;
        } catch (SQLException exception) {
            Logger.getLogger(StorageSqlStore.class.getName()).log(Level.SEVERE, null, exception);
            databaseConnection.registerError(exception);
            return false;
        }
    }
}