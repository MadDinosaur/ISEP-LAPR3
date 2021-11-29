package lapr.project.controller;

import lapr.project.data.DatabaseConnection;
import lapr.project.data.ShipSqlStore;
import lapr.project.mappers.dto.PositioningDataDTO;
import lapr.project.mappers.dto.ShipDTO;
import lapr.project.model.Ship;
import lapr.project.store.ShipFileReader;
import lapr.project.store.ShipStore;
import lapr.project.data.MainStorage;

import java.util.List;
import java.util.Map;

public class ReadShipFileController {

    /**
     * the current ship store
     */
    private final ShipStore shipStore;

    /**
     * Calls the creator with a the current storage instance
     */
    public ReadShipFileController() {
        this(MainStorage.getInstance());
    }

    /**
     * Creates a instance of the controller with the current storage instance
     *
     * @param mainStorage the storage instance used to store all information
     */
    public ReadShipFileController(MainStorage mainStorage) {
        this.shipStore = mainStorage.getShipStore();
    }

    /**
     * reads a file and saves the data in that file
     * @param path the path to the file
     */
    public void readFileAndSaveData(String path){
        Map<ShipDTO, List<PositioningDataDTO>> shipData = ShipFileReader.readShipFile(path);
        DatabaseConnection databaseConnection = MainStorage.getInstance().getDatabaseConnection();
        boolean insert = Boolean.parseBoolean(System.getProperty("database.insert"));
        if (shipData != null) {
            ShipSqlStore shipSqlStore = new ShipSqlStore();
            List<Ship> shipList = shipStore.createShip(shipData);
            for (Ship ship : shipList) {
                shipStore.addShip(ship);
                if (insert)
                    shipSqlStore.save(databaseConnection, ship);
            }
        }
    }
}
