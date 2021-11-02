package lapr.project.model;

import lapr.project.exception.IllegalCoordinateException;

public class Coordinate {

    /**
     * A constant string value that both the longitude and latitude can have
     */
    private static final String UNAVAILABLE =  "not available";

    /**
     * the coordinate's longitude
     */
    private long longitude;

    /**
     * the coordinate's latitude
     */
    private long latitude;

    /**
     * The main constructor
     * @param longitude the coordinate's longitude
     * @param latitude the coordinate's latitude
     */
    public Coordinate(long longitude, long latitude){
        setLongitude(longitude);
        setLatitude(latitude);
    }

    /**
     * sets the coordinates latitude
     * @param latitude the latitude parameter to be added
     */
    public void setLatitude(long latitude) {
        if (checkLatitudeRules(latitude))
            this.latitude = latitude;
    }

    /**
     * checks the rules of latitude
     * @param latitude the latitude parameter to be added
     * @return true if the latitude value is between the allowed values otherwise throws an exception
     */
    private boolean checkLatitudeRules(long latitude){
        if (latitude >= -90 && latitude <= 90 || latitude == 91)
            return true;
        else
            throw new IllegalCoordinateException("The latitude value \"" + latitude  + "\" is not within the expected boundaries");
    }

    /**
     * sets the coordinates longitude
     * @param longitude the longitude parameter to be added
     */
    public void setLongitude(long longitude) {
        if (checkLongitudeRules(longitude))
            this.longitude = longitude;
    }

    /**
     * checks the rules of longitude
     * @param longitude the longitude parameter to be added
     * @return true if the longitude value is between the allowed values otherwise throws an exception
     */
    private boolean checkLongitudeRules(long longitude){
        if (longitude >= -180 && longitude <= 180 || longitude == 181)
            return true;
        else
            throw new IllegalCoordinateException("The longitude value \"" + longitude  + "\" is not within the expected boundaries");
    }
}
