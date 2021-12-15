package lapr.project.controller;

import lapr.project.data.CargoManifestSqlStore;
import lapr.project.data.DatabaseConnection;
import lapr.project.data.MainStorage;
import oracle.ucp.util.Pair;

import java.sql.SQLException;

public class GetManifestInformationController {
    /**
     * The current storage
     */
    private final MainStorage mainStorage;

    /**
     * The current sql cargo manifest store
     */
    private final CargoManifestSqlStore cargoManifestStore;

    /**
     * Calls the creator with a the current storage instance
     */
    public GetManifestInformationController() {
        this.mainStorage = MainStorage.getInstance();
        this.cargoManifestStore = new CargoManifestSqlStore();
    }

    /**
     * Creates a instance of the controller with the current storage instance and sql store
     *
     * @param mainStorage the storage instance used to store all information
     * @param cargoManifestSqlStore the store instance used to store database requests
     */
    public GetManifestInformationController(MainStorage mainStorage, CargoManifestSqlStore cargoManifestSqlStore) {
        this.mainStorage = mainStorage;
        this.cargoManifestStore = cargoManifestSqlStore;
    }

        /**
         * sets up the controller with the amount of cargo manifests in a given year
         * @param captain_id the chosen captain's id
         * @param year the chosen year
         * @return true if the result is not null
         */
    public Pair<Integer, Double> findCargoManifests(String captain_id, int year) {
        try {
            return cargoManifestStore.getCargoManifestInYear(mainStorage.getDatabaseConnection(), captain_id, year);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
