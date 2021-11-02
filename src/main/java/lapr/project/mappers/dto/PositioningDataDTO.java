package lapr.project.mappers.dto;

public class PositioningDataDTO {

    /**
     * The data's time of creation
     */
    private String bdt;

    /**
     * the coordinate's latitude
     */
    private String latitude;

    /**
     * the coordinate's longitude
     */
    private String longitude;

    /**
     * The ship's speed over ground when the data was sent
     */
    private String sog;

    /**
     * The ship's course over ground when the data was sent
     */
    private String cog;

    /**
     * The ship's heading when the data was sent
     */
    private String heading;

    /**
     * The ship's ship code in tow when the data was sent
     */
    private String position;

    /**
     * The ship's transceiver class used to send the data
     */
    private String transceiverClass;

    /**
     * creates a package with all the information that a PositioningData class needs
     *
     * @param bdt The data's time of creation
     * @param latitude the coordinate's latitude
     * @param longitude the coordinate's longitude
     * @param sog The ship's speed over ground when the data was sent
     * @param cog The ship's course over ground when the data was sent
     * @param heading The ship's heading when the data was sent
     * @param position The ship's ship code in tow when the data was sent
     * @param transceiverClass The ship's transceiver class used to send the data
     */
    public PositioningDataDTO(String bdt, String latitude, String longitude, String sog, String cog, String heading, String position, String transceiverClass){
        this.bdt = bdt;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sog = sog;
        this.cog = cog;
        this.heading = heading;
        this.position = position;
        this.transceiverClass = transceiverClass;
    }

    /**
     * returns the base date time value
     * @return returns the base date time value
     */
    public String getBdt() {
        return bdt;
    }

    /**
     * returns the course over ground value
     * @return returns the course over ground value
     */
    public String getCog() {
        return cog;
    }

    /**
     * returns the heading value
     * @return returns the heading value
     */
    public String getHeading() {
        return heading;
    }

    /**
     * returns the latitude value
     * @return returns the latitude value
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * returns the longitude value
     * @return returns the longitude value
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * returns the position value
     * @return returns the position value
     */
    public String getPosition() {
        return position;
    }

    /**
     * returns the speed over ground value
     * @return returns the speed over ground value
     */
    public String getSog() {
        return sog;
    }

    /**
     * returns the Transceiver class
     * @return returns the Transceiver class
     */
    public String getTransceiverClass() {
        return transceiverClass;
    }
}
