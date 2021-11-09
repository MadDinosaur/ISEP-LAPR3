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

    /**
     * returns the first date values
     * @return returns the first date values
     */
    public Date getFirstDate(){
        return positioningDataList.get(0).getBdt();
    }

    /**
     * returns the last date values
     * @return returns the last date values
     */
    public Date getLastDate(){
        return positioningDataList.get(positioningDataList.size()-1).getBdt();
    }

    /**
     * returns the total movement time values
     * @return returns the total movement time values
     */
    public float totalMovementTime(){
        return getLastDate().getTime() - getFirstDate().getTime();
    }

    /**
     * returns the total movement number values
     * @return returns the total movement number values
     */
    public float totalMovementNumber(){
        return positioningDataList.size();
    }

    /**
     * returns the departure latitude value
     * @return returns the departure latitude value
     */
    public float departureLatitude(){
        return positioningDataList.get(0).getCoordinate().getLatitude();
    }

    /**
     * returns the arrival latitude value
     * @return returns the arrival latitude value
     */
    public float arrivalLatitude(){
        return positioningDataList.get(positioningDataList.size()-1).getCoordinate().getLatitude();
    }

    /**
     * returns the departure longitude value
     * @return returns the departure longitude value
     */
    public float departureLongitude(){
        return positioningDataList.get(0).getCoordinate().getLongitude();
    }

    /**
     * returns the arrival longitude value
     * @return returns the arrival longitude value
     */
    public float arrivalLongitude(){
        return positioningDataList.get(positioningDataList.size()-1).getCoordinate().getLongitude();
    }

    /**
     * returns the max sog value
     * @return returns the max sog value
     */
    public float maxSog(){
        float max = positioningDataList.get(0).getSog();
        for (PositioningData positioningData : positioningDataList) {
            if (max < positioningData.getSog()) {
                max = positioningData.getSog();
            }
        }
        return max;
    }

    /**
     * returns the mean sog value
     * @return returns the mean sog value
     */
    public float meanSog(){
        float sum = 0;
        for (PositioningData positioningData : positioningDataList) {
            sum += positioningData.getSog();
        }
        return sum/positioningDataList.size();
    }

    /**
     * returns the max cog value
     * @return returns the max cog value
     */
    public float maxCog(){
        float max = positioningDataList.get(0).getCog();
        for (PositioningData positioningData : positioningDataList) {
            if (max < positioningData.getCog()) {
                max = positioningData.getCog();
            }
        }
        return max;
    }

    /**
     * returns the mean cog value
     * @return returns the mean cog value
     */
    public float meanCog(){
        float sum = 0;
        for (PositioningData positioningData : positioningDataList) {
            sum += positioningData.getCog();
        }
        return sum/positioningDataList.size();
    }

    /**
     * returns the total traveled distance value
     * @return returns the total traveled distance value
     */
    public double traveledDistance(){
        final long radius = 6371; //radius of earth in km
        long totalTraveledDistance = 0;
        for(int i=0; i<positioningDataList.size()-1; i++){
            if(positioningDataList.get(i+1).getCoordinate().getLatitude() > 90 || positioningDataList.get(i).getCoordinate().getLatitude() > 90){
                continue;
            }
            if(positioningDataList.get(i+1).getCoordinate().getLongitude() > 180 || positioningDataList.get(i).getCoordinate().getLongitude() > 90){
                continue;
            }
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

    /**
     * returns the delta distance value
     * @return returns the delta distance value
     */
    public double deltaDistance(){
        final float radius = 6371;
        float latDistance = toRadious(arrivalLatitude() - departureLatitude());
        float lonDistance = toRadious(arrivalLongitude() - departureLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(toRadious(departureLatitude())) * Math.cos(toRadious(arrivalLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return radius * c;
    }

    /**
     * tranforms degrees into radians
     * @param deg degrees of an angle
     * @return value of degrees to radious
     */
    private float toRadious(float deg) {
        return (float) (deg * Math.PI / 180.0);
    }

    /**
     * returns a list of all position data that takes place in the specified time slot
     * @param date1 the initial date
     * @param date2 the final date
     * @return returns a list of all position data that takes place in the specified time slot
     */
    public List<PositioningData> getPositionsByDate(Date date1, Date date2) {
        List<PositioningData> datePositionList = new ArrayList<>();
        for (PositioningData positioningData : this.positioningDataList){
            if (positioningData.getBdt().compareTo(date1) > 0 && positioningData.getBdt().compareTo(date2) < 0){
                datePositionList.add(positioningData);
            }
        }
        return datePositionList;
    }
}
