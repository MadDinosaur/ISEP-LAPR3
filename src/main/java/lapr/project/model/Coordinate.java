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
        checkLatitudeRules(latitude);
        this.latitude = latitude;
    }

    /**
     * checks the rules of latitude
     * @param latitude the latitude parameter to be added
     */
    private void checkLatitudeRules(float latitude){
        if (!(latitude >= -90 && latitude <= 90 || latitude == 91))
            throw new IllegalCoordinateException("The latitude value \"" + latitude  + "\" is not within the expected boundaries");
    }

    /**
     * sets the coordinates longitude
     * @param longitude the longitude parameter to be added
     */
    public void setLongitude(float longitude) {
        checkLongitudeRules(longitude);
        this.longitude = longitude;
    }

    /**
     * checks the rules of longitude
     * @param longitude the longitude parameter to be added
     */
    private void checkLongitudeRules(float longitude){
        if (!(longitude >= -180 && longitude <= 180 || longitude == 181))
            throw new IllegalCoordinateException("The longitude value \"" + longitude  + "\" is not within the expected boundaries");
    }

    /**
     * returns the ship's longitude
     * @return returns the ship's longitude
     */
    public float getLongitude() {
        return longitude;
    }
    /**
     * returns the ship's latitude
     * @return returns the ship's latitude
     */
    public float getLatitude() {
        return latitude;
    }
}
