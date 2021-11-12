package lapr.project.utils;

import lapr.project.model.Ship;

import java.util.Comparator;

public class SorterTraveledDistance implements Comparator<Ship> {

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

