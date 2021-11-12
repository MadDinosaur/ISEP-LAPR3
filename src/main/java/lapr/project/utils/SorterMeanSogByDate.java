package lapr.project.utils;

import lapr.project.model.Ship;

import java.util.Comparator;
import java.util.Date;

public class SorterMeanSogByDate implements Comparator<Ship> {
    Date date1;
    Date date2;


    public SorterMeanSogByDate(Date date1, Date date2) {
        this.date1 = date1;
        this.date2 = date2;
    }

    @Override
    public int compare(Ship o1, Ship o2) {
        double o1MeanSog = o1.getPositioningDataList().getPositionsByDate(date1, date2).meanSog();
        double o2MeanSog = o2.getPositioningDataList().getPositionsByDate(date1, date2).meanSog();

        if (o1MeanSog > o2MeanSog)
            return -1;

        else if (o1MeanSog < o2MeanSog)
            return 1;

        if (o1MeanSog == o2MeanSog){
            return compare(o1, o2);
        }

        return 0;
    }
}
