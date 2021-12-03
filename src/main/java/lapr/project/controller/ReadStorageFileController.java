package lapr.project.controller;

import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import lapr.project.data.StorageSqlStore;
import lapr.project.mappers.dto.StorageDTO;
import lapr.project.model.Storage;
import lapr.project.store.StorageFileReader;
import lapr.project.store.StorageStore;

import java.util.ArrayList;
import java.util.List;

public class ReadStorageFileController {


    /**
     * the current ship store
     */
    private final StorageStore storageStore;

    /**
     * Calls the creator with the current storage instance
     */
    public ReadStorageFileController() {
        this(MainStorage.getInstance());
    }

    /**
     * Creates an instance of the controller with the current storage instance
     *
     * @param mainStorage the storage instance used to store all information
     */
    public ReadStorageFileController(MainStorage mainStorage) {
        this.storageStore = mainStorage.getStorageStore();
    }

    /**
     * Reads a file and saves the data in that file in the Database
     * @param path the path to the file
     */
    private void readFileAndSaveDataToDB(String path, DatabaseConnection databaseConnection){
        List<StorageDTO> storageData = StorageFileReader.readStorageFile(path);

        if (storageData != null){
            StorageSqlStore storageSqlStore = new StorageSqlStore();
            List<Storage> storageList = storageStore.createStorageList(storageData);
            for (Storage storage : storageList)
                storageSqlStore.save(databaseConnection, storage);
        }
    }

    public void readFileAndSaveData(String path) {
        List<Storage> storageList = new ArrayList<>();

        if (Boolean.parseBoolean(System.getProperty("database.insert"))) {
            DatabaseConnection databaseConnection = MainStorage.getInstance().getDatabaseConnection();
            StorageSqlStore storageSqlStore = new StorageSqlStore();

            readFileAndSaveDataToDB(path, databaseConnection);
            storageList = storageSqlStore.getStorageDataFromDataBase(databaseConnection);
            storageStore.empty();
        } else {
            List<StorageDTO> storageData = StorageFileReader.readStorageFile(path);

            if (storageData != null)
                storageList = storageStore.createStorageList(storageData);
        }

        storageStore.addStorageList(storageList);
    }
}
