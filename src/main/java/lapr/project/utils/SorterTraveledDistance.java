package lapr.project.utils;

import lapr.project.model.Ship;

import java.util.Comparator;

public class SorterTraveledDistance implements Comparator<Ship> {

    /**
     * Compares both ships based on the Traveled Distance and if they have the same value, it checks the Number Of Movements
     *
     * @param o1 the first ship
     * @param o2 the second ship
     * @return a value < -1, 0, 1 >
     */
    @Override
    public int compare(Ship o1,Ship o2) {


        double o1TraveledDistance = o1.getPositioningDataList().traveledDistance();
        double o2TraveledDistance = o2.getPositioningDataList().traveledDistance();

        double o1NumberMovements = o1.getPositioningDataList().size();
        double o2NumberMovements = o2.getPositioningDataList().size();



        if (o1TraveledDistance > o2TraveledDistance)
            return -1;

        else if (o1TraveledDistance < o2TraveledDistance)
            return 1;


        if (o1NumberMovements == o2NumberMovements)
            return o1.compareTo(o2);

        else if (o1NumberMovements > o2NumberMovements)
            return 1;

            else return -1;
        }
    }

