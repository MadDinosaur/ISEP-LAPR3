package lapr.project.controller;

import lapr.project.data.MainStorage;
import lapr.project.mappers.dto.StorageDTO;
import lapr.project.model.Storage;
import lapr.project.store.StorageFileReader;
import lapr.project.store.StorageStore;

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
     * Reads a file and saves the data in that file
     * @param path the path to the file
     */
    public void readFileAndSaveData(String path){
        List<StorageDTO> storageData = StorageFileReader.readStorageFile(path);

        if (storageData != null){
            List<Storage> storageList = storageStore.createStorage(storageData);
            storageStore.addStorage(storageList);
        }
    }

}
