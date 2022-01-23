package lapr.project.controller;


import lapr.project.data.MainStorage;
import lapr.project.model.CartesianCoordinate;
import lapr.project.store.ShipStore;
import oracle.ucp.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class PositionContainersController {

    /**
     * The current ship Store
     */
    private final ShipStore shipStore;

    /***
     * Calls the creator with the current storage instance
     */
    public PositionContainersController(){
        this(MainStorage.getInstance());
    }

    /**
     * Creates an instance of the controller with the current storage instance
     * @param storage The storage instance used to store all information
     */
    public PositionContainersController(MainStorage storage){
        this.shipStore = storage.getShipStore();
    }

    /**
     * Determines the positions of a given number of containers in order to keep the center of gravity at (xx, yy)
     * @param shipLength a list of the sections of the ship according to whether it can support containers and its respective length
     * @param shipWidth the width of the ship
     * @param shipHeight the height of the ship
     * @param shipMass the mass of the ship
     * @param towerMass the mass of the control tower
     * @param towerCoord the coordinates of the ship's control tower(s)
     * @param nContainers the number of containers to load
     * @return list of coordinates (in container units) to store all the container positions
     */
    public List<CartesianCoordinate<Integer>> getContainerPositions(List<Pair<Double, Boolean>> shipLength, double shipWidth, double shipHeight, double shipMass, List<Double> towerMass, List<Pair<Double,Double>> towerCoord, int nContainers){
        CartesianCoordinate<Integer> maxContainers = shipStore.getShipContainerCapacity(shipLength, shipWidth, shipHeight);
        int xMax = maxContainers.getX();
        int yMax = maxContainers.getY();
        int zMax = maxContainers.getZ();

        double length = shipLength.stream()
                .map(Pair::get1st)
                .reduce(0.0, Double::sum);
        Pair<Double, Double> shipCenterMass = shipStore.getCenterOfMass(shipMass, length, shipWidth, towerMass, towerCoord);
        CartesianCoordinate<Integer> shipCenterMassContainers = shipStore.convertCoordinatePhysicalToContainers(shipLength, shipCenterMass);

        List<CartesianCoordinate<Integer>> coordinates = new ArrayList<>();
        shipStore.positionContainers(xMax, yMax, zMax, shipCenterMassContainers, nContainers, coordinates);
        return coordinates;
    }

    /**
     * Determined the new center of mass given the container positions determined in getContainerPositions
     * @param shipMass the mass of the ship
     * @param shipLength a list of the sections of the ship according to whether it can support containers and its respective length
     * @param shipWidth the width of the ship
     * @param towerMass towerMass the mass of the control tower(s)
     * @param towerCoord the coordinates of the ship's control tower(s)
     * @param massLoad the total mass of the containers to be loaded (assuming each container has the same mass)
     * @param centerMassLoadList the containers center of mass coordinates (in container units)
     * @return coordinates of the center of mass
     */
    public Pair<Double, Double> getLoadedCenterOfMass(double shipMass, List<Pair<Double, Boolean>> shipLength, double shipWidth, List<Double> towerMass, List<Pair<Double, Double>> towerCoord, double massLoad, List<CartesianCoordinate<Integer>> centerMassLoadList) {
        massLoad = massLoad / centerMassLoadList.size();

        List<CartesianCoordinate<Double>> centerMassLoadListPhys = new ArrayList<>();
        for (CartesianCoordinate<Integer> centerMassLoad : centerMassLoadList)
            centerMassLoadListPhys.add(shipStore.convertCoordinateContainersToPhysical(shipLength, centerMassLoad));

        double length = shipLength.stream()
                .map(Pair::get1st)
                .reduce(0.0, Double::sum);

        return shipStore.getCenterOfMass(shipMass, length, shipWidth, towerMass, towerCoord, massLoad, centerMassLoadListPhys);
    }
}

