package lapr.project.controller;

import lapr.project.data.*;
import lapr.project.model.Country;
import lapr.project.store.SeaDistFilesReader;
import oracle.ucp.util.Pair;

import java.util.List;

public class ReadSeaDistFilesController {

    /**
     * the current ship store
     */
    private final DatabaseConnection shipStore;

    /**
     * Calls the creator with a the current storage instance
     */
    public ReadSeaDistFilesController() {
        this(MainStorage.getInstance());
    }

    /**
     * Creates a instance of the controller with the current storage instance
     *
     * @param mainStorage the storage instance used to store all information
     */
    public ReadSeaDistFilesController(MainStorage mainStorage) {
        this.shipStore = mainStorage.getDatabaseConnection();
    }

    /**
     * reads a file and saves the data in that file
     * @param path the path to the file
     */
    public void readCountryFileAndSaveData(String path){
        List<Country> shipData = SeaDistFilesReader.readCountries(path);
        DatabaseConnection databaseConnection = MainStorage.getInstance().getDatabaseConnection();
        if (databaseConnection != null && shipData != null) {
            CountrySqlStore countrySqlStore = new CountrySqlStore();
            for (Country country : shipData){
                countrySqlStore.save(databaseConnection, country);
            }
        }
    }

    /**
     * reads a file and saves the data in that file
     * @param path the path to the file
     */
    public void readBorderFileAndSaveData(String path){
        List<Pair<String, String>> BorderData = SeaDistFilesReader.readBorders(path);
        DatabaseConnection databaseConnection = MainStorage.getInstance().getDatabaseConnection();
        if (databaseConnection != null && BorderData != null) {
            BorderSQLStore borderSQLStore = new BorderSQLStore();
            for (Pair<String, String> border : BorderData){
                borderSQLStore.save(databaseConnection, border);

            }
        }
    }

    /**
     * reads a file and saves the data in that file
     * @param path the path to the file
     */
    public void readSeaDistFileAndSaveData(String path){
        List<Pair<Pair<String, String>, String>> pathData = SeaDistFilesReader.readSeaDist(path);
        DatabaseConnection databaseConnection = MainStorage.getInstance().getDatabaseConnection();
        if (databaseConnection != null && pathData != null) {
            PathSqlStore pathSqlStore = new PathSqlStore();
            for (Pair<Pair<String, String>, String> country : pathData){
                pathSqlStore.save(databaseConnection, country);
            }
        }
    }
}
