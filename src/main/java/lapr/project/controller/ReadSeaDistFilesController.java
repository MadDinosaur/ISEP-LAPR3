package lapr.project.controller;

import lapr.project.data.*;
import lapr.project.model.Country;
import lapr.project.store.SeaDistFilesReader;
import oracle.ucp.util.Pair;

import java.util.List;

public class ReadSeaDistFilesController {

    /**
     * reads a file and saves the data in that file
     * @param path the path to the file
     */
    public void readCountryFileAndSaveData(String path){
        List<Country> countryData = SeaDistFilesReader.readCountries(path);
        DatabaseConnection databaseConnection = MainStorage.getInstance().getDatabaseConnection();
        if (databaseConnection != null && countryData != null) {
            CountrySqlStore countrySqlStore = new CountrySqlStore();
            for (Country country : countryData){
                countrySqlStore.save(databaseConnection, country);
            }
        }
    }

    /**
     * reads a file and saves the data in that file
     * @param path the path to the file
     */
    public void readBorderFileAndSaveData(String path){
        List<Pair<String, String>> borderData = SeaDistFilesReader.readBorders(path);
        DatabaseConnection databaseConnection = MainStorage.getInstance().getDatabaseConnection();
        if (databaseConnection != null && borderData != null) {
            BorderSQLStore borderSQLStore = new BorderSQLStore();
            for (Pair<String, String> border : borderData){
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
