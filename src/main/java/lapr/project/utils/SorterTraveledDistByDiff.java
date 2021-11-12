package lapr.project.utils;

import lapr.project.model.Ship;

import java.util.Comparator;

/**
 * Sorts ships in descending order of the
 * Travelled Distance difference from the @baseTraveledDistance
 */
public class SorterTraveledDistByDiff implements Comparator<Ship> {

    private final double baseTraveledDistance;

    public SorterTraveledDistByDiff(double baseTraveledDistance) {
        this.baseTraveledDistance = baseTraveledDistance;
    }

    /**
     * Compares two ships based on the Travelled Distance difference from the @baseTraveledDistance
     * @param o1
     * @param o2
     * @return 1 --> o1 traveled distance difference is lesser than o2
     *        -1 --> o1 traveled distance difference is greater than o2
     *         0 --> o1 traveled distance difference is equal to o2
     */
    @Override
    public int compare(Ship o1, Ship o2) {
        double o1TraveledDistDiff = Math.abs(baseTraveledDistance - o1.getPositioningDataList().traveledDistance());
        double o2TraveledDistDiff = Math.abs(baseTraveledDistance - o2.getPositioningDataList().traveledDistance());

        return -Double.compare(o1TraveledDistDiff, o2TraveledDistDiff);
    }
}
