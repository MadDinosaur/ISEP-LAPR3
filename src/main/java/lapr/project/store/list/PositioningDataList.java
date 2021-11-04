package lapr.project.store.list;

import lapr.project.model.PositioningData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class PositioningDataList {

    /**
     * list of a given ships positioning data
     */
    List<PositioningData> positioningDataList = new ArrayList<>();

    /**
     * populates and rearranges the list
     * @param positioningDataList a list of positioning data
     */
    public void addPositioningDataList(List<PositioningData> positioningDataList) {
        this.positioningDataList.addAll(positioningDataList);
        Collections.sort(this.positioningDataList);
    }

    /**
     * returns the list of position data
     * @return returns the list of position data
     */
    public List<PositioningData> getPositioningDataList() {
        return positioningDataList;
    }

    public Date getFirstDate(){
        return positioningDataList.get(0).getBdt();
    }

    public Date getLastDate(){
        return positioningDataList.get(positioningDataList.size()-1).getBdt();
    }

    public float totalMovementTime(){
        return getLastDate().getTime() - getFirstDate().getTime();
    }

    public float totalMovementNumber(){
        return positioningDataList.size();
    }

    public float departureLatitude(){
        return positioningDataList.get(0).getCoordinate().getLatitude();
    }

    public float arrivalLatitude(){
        return positioningDataList.get(positioningDataList.size()-1).getCoordinate().getLatitude();
    }

    public float departureLongitude(){
        return positioningDataList.get(0).getCoordinate().getLongitude();
    }

    public float arrivalLongitude(){
        return positioningDataList.get(positioningDataList.size()-1).getCoordinate().getLongitude();
    }

    public float maxSog(){
        float max = positioningDataList.get(0).getSog();
        for (PositioningData positioningData : positioningDataList) {
            if (max < positioningData.getSog()) {
                max = positioningData.getSog();
            }
        }
        return max;
    }

    public float meanSog(){
        float sum = 0;
        for (PositioningData positioningData : positioningDataList) {
            sum += positioningData.getSog();
        }
        return sum/positioningDataList.size();
    }

    public float maxCog(){
        float max = positioningDataList.get(0).getCog();
        for (PositioningData positioningData : positioningDataList) {
            if (max < positioningData.getCog()) {
                max = positioningData.getCog();
            }
        }
        return max;
    }

    public float meanCog(){
        float sum = 0;
        for (PositioningData positioningData : positioningDataList) {
            sum += positioningData.getCog();
        }
        return sum/positioningDataList.size();
    }

    public double traveledDistance(){
        final long radius = 6371000; //radius of earth in m
        long totalTraveledDistance = 0;
        for(int i=0; i<positioningDataList.size()-1; i++){
            double latDistance = toRadious(positioningDataList.get(i+1).getCoordinate().getLatitude() - positioningDataList.get(i).getCoordinate().getLatitude());
            double lonDistance = toRadious(positioningDataList.get(i+1).getCoordinate().getLongitude() - positioningDataList.get(i).getCoordinate().getLongitude());
            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(toRadious(positioningDataList.get(i).getCoordinate().getLatitude())) * Math.cos(toRadious(positioningDataList.get(i+1).getCoordinate().getLatitude()))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distance = radius * c;
            totalTraveledDistance += distance;
        }
        return totalTraveledDistance;
    }

    public double deltaDistance(){
        final float radius = 6371000;
        float latDistance = toRadious(arrivalLatitude() - departureLatitude());
        float lonDistance = toRadious(arrivalLongitude() - departureLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(toRadious(departureLatitude())) * Math.cos(toRadious(arrivalLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return radius * c;
    }

    private float toRadious(float deg) {
        return (float) (deg * Math.PI / 180.0);
    }
}
