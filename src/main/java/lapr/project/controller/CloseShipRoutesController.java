package lapr.project.controller;

import lapr.project.data.MainStorage;
import lapr.project.mappers.ShipMapper;
import lapr.project.mappers.dto.ShipDTO;
import lapr.project.model.Ship;
import lapr.project.store.ShipStore;
import oracle.ucp.util.Pair;

import java.util.*;

public class CloseShipRoutesController {

    /**
     *  The current ship store
     */
    private final ShipStore shipStore;

    /**
     * Calls the creator with the current storage instance
     */
    public CloseShipRoutesController() {
        this(MainStorage.getInstance());
    }

    /**
     * Creates a instance of the controller with the current storage instance
     * @param storage the storage instance used to store all information
     */
    public CloseShipRoutesController(MainStorage storage) {
        this.shipStore = storage.getShipStore();
    }

    /**
     * Gets pairs of ships with close coordinates, ordered by 1st first MMSI (ascending) and Traveled Distance (descending)
     * @return returns the sorted pairs of ships
     */
    private HashMap<Ship, TreeMap<Ship, Pair<Double, Double>>>getCloseShipRoutesMap() {

        int minTraveledDistance = 10;
        int distance = 5;

        return shipStore.getCloseShipRoutes(minTraveledDistance, distance);
    }

    /**
     * Converts a HashMap<Ship, TreeMap<Ship, Pair<Double, Double>>> to a DTO structure, where:
     * Pair<ShipDTO, ShipDTO> - pair of ships with close arrival/departure coordinates
     * Pair<Double, Double> - pair of distances between the two ships calculates between arrival coord. and departure coord.
     *
     * @return a list of both data structures, wrapped in a Pair
     */
    public List<Pair<Pair<ShipDTO, ShipDTO>,Pair<Double, Double>>> getCloseShipRoutesDTO() {
        HashMap<Ship, TreeMap<Ship, Pair<Double, Double>>> map = getCloseShipRoutesMap();
        ArrayList<Pair<Pair<ShipDTO, ShipDTO>, Pair<Double, Double>>> list = new ArrayList<>();

        for (Ship ship : map.keySet()) {
            if (map.get(ship).isEmpty()) continue;

            ShipDTO shipDTO = ShipMapper.toDTO(ship);
            shipDTO.setTraveledDistance(ship.getPositioningDataList().traveledDistance());
            shipDTO.setNumberOfMovements(ship.getPositioningDataList().totalMovementNumber());

            for (Map.Entry<Ship, Pair<Double, Double>> entry : map.get(ship).entrySet()) {
                Ship pairShip = entry.getKey();
                ShipDTO pairShipDTO = ShipMapper.toDTO(pairShip);

                Pair<ShipDTO, ShipDTO> shipPair = new Pair<>(shipDTO, pairShipDTO);
                Pair<Double, Double> distancePair = entry.getValue();

                list.add(new Pair<>(shipPair, distancePair));
            }
        }

        return list;
    }

    /**
     * Converts the DTO structure returned by getCloseShipRoutesDTO() into a String, arranged as follows:
     * Ship 1 MMSI
     * Ship 2 MMSI
     * Origin Distance
     * Destination Distance
     * Ship 1 Traveled Distance
     * Ship 1 Number of Movements
     * Ship 2 Traveled Distance
     * Ship 2 Number of Movements
     *
     * @return the string to display
     */
    public String getCloseShipRoutes() {
        StringBuilder string = new StringBuilder();

        List<Pair<Pair<ShipDTO, ShipDTO>,Pair<Double, Double>>> list = getCloseShipRoutesDTO();

        if (list.isEmpty())
            string.append("No ships available.");

        for (Pair<Pair<ShipDTO, ShipDTO>,Pair<Double, Double>> entry : list) {
            Pair<ShipDTO, ShipDTO> ships = entry.get1st();
            Pair<Double, Double> distances = entry.get2nd();

            ShipDTO ship1 = ships.get1st();
            ShipDTO ship2 = ships.get2nd();
            double originDist = distances.get1st();
            double destinationDist = distances.get2nd();

            string.append(
                    String.format("Ship 1 MMSI : %s - Ship 2 MMSI : %s" +
                                    " - OriginDist : %f - DestDist : %f" +
                                    " - Traveled Distance1: %s KM" +
                                    " - Number of Movements1: %s" +
                                    " - Traveled Distance2: %s KM" +
                                    " - Number of Movements2: %s\n",
                            ship1.getMmsi(), ship2.getMmsi(),
                            originDist, destinationDist,
                            ship1.getTraveledDistance(), ship1.getNumberOfMovements(),
                            ship2.getTraveledDistance(), ship2.getNumberOfMovements()));

                            string.append("\n\n");
        }
        return string.toString();
    }
}
