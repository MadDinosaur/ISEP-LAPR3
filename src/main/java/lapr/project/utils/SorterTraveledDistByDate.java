package lapr.project.utils;

import lapr.project.model.Ship;

import java.util.Comparator;
import java.util.Date;

public class SorterTraveledDistByDate implements Comparator<Ship> {
    Date date1;

    Date date2;


    public SorterTraveledDistByDate(Date date1, Date date2) {
        this.date1 = date1;
        this.date2 = date2;
    }

    @Override
    public int compare(Ship o1,Ship o2) {


        double o1TraveledDistance = o1.getPositioningDataList().getPositionsByDate(date1, date2).traveledDistance();
        double o2TraveledDistance = o2.getPositioningDataList().getPositionsByDate(date1, date2).traveledDistance();

        double o1NumberMovements = o1.getPositioningDataList().getPositionsByDate(date1, date2).size();
        double o2NumberMovements = o2.getPositioningDataList().getPositionsByDate(date1, date2).size();



        if (o1TraveledDistance > o2TraveledDistance)
            return -1;

        else if (o1TraveledDistance < o2TraveledDistance)
            return 1;


        if (o1TraveledDistance == o2TraveledDistance){

            if (o1NumberMovements == o2NumberMovements)
                return 0;

            else if (o1NumberMovements > o2NumberMovements)
                return 1;

            else return -1;
        }

        return 0;
    }
}
