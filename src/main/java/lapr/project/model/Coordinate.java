package lapr.project.model;

import lapr.project.exception.IllegalCoordinateException;

public class Coordinate {

    /**
     * the coordinate's longitude
     */
    private float longitude;

    /**
     * the coordinate's latitude
     */
    private float latitude;

    /**
     * The main constructor
     * @param longitude the coordinate's longitude
     * @param latitude the coordinate's latitude
     */
    public Coordinate(float longitude, float latitude){
        setLongitude(longitude);
        setLatitude(latitude);
    }

    /**
     * sets the coordinates latitude
     * @param latitude the latitude parameter to be added
     */
    public void setLatitude(float latitude) {
        if (checkLatitudeRules(latitude))
            this.latitude = latitude;
    }

    /**
     * checks the rules of latitude
     * @param latitude the latitude parameter to be added
     * @return true if the latitude value is between the allowed values otherwise throws an exception
     */
    private boolean checkLatitudeRules(float latitude){
        if (latitude >= -90 && latitude <= 90 || latitude == 91)
            return true;
        else
            throw new IllegalCoordinateException("The latitude value \"" + latitude  + "\" is not within the expected boundaries");
    }

    /**
     * sets the coordinates longitude
     * @param longitude the longitude parameter to be added
     */
    public void setLongitude(float longitude) {
        if (checkLongitudeRules(longitude))
            this.longitude = longitude;
    }

    /**
     * checks the rules of longitude
     * @param longitude the longitude parameter to be added
     * @return true if the longitude value is between the allowed values otherwise throws an exception
     */
    private boolean checkLongitudeRules(float longitude){
        if (longitude >= -180 && longitude <= 180 || longitude == 181)
            return true;
        else
            throw new IllegalCoordinateException("The longitude value \"" + longitude  + "\" is not within the expected boundaries");
    }
}
