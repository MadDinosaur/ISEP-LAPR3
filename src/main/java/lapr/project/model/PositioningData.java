package lapr.project.model;

import lapr.project.exception.IllegalPositioningDataException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PositioningData implements Comparable<PositioningData>{

    /**
     * The data's time of creation
     */
    private Date bdt;

    /**
     * The ship's coordinates when the data was sent
     */
    private Coordinate coordinate;

    /**
     * The ship's speed over ground when the data was sent
     */
    private float sog;

    /**
     * The ship's course over ground when the data was sent
     */
    private float cog;

    /**
     * The ship's heading when the data was sent
     */
    private float heading;

    /**
     * The ship's ship code in tow when the data was sent
     */
    private String position;

    /**
     * The ship's transceiver class used to send the data
     */
    private String transceiverClass;

    /**
     * creator class
     * @param bdt The data's time of creation
     * @param coordinate The ship's coordinates when the data was sent
     * @param sog The ship's speed over ground when the data was sent
     * @param cog The ship's course over ground when the data was sent
     * @param heading The ship's heading when the data was sent
     * @param position The ship's ship code in tow when the data was sent
     * @param transceiverClass The ship's transceiver class used to send the data
     */
    public PositioningData(String bdt, Coordinate coordinate, float sog, float cog, float heading, String position, String transceiverClass ){
        setBdt(bdt);
        setCoordinate(coordinate);
        setSog(sog);
        setCog(cog);
        setHeading(heading);
        setPosition(position);
        setTransceiverClass(transceiverClass);
    }

    /**
     * sets the base date time
     * @param bdt The data's time of creation
     */
    public void setBdt(String bdt) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            this.bdt = formatter.parse(bdt);
        } catch (ParseException e){
            throw new IllegalPositioningDataException("Base Date time value \"" + bdt + "\"  is not accepted");
        }
    }

    /**
     * Sets the coordinates of the ship
     * @param coordinate The ship's coordinates when the data was sent
     */
    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * Sets the ship's course over ground
     * @param cog The ship's course over ground when the data was sent
     */
    public void setCog(float cog) {
        checkCogRules(cog);
        if (cog < 0)
            this.cog = 359 + cog;
        else
            this.cog = cog;
    }

    /**
     * checks if the course over ground value is allowed
     * @param cog The ship's course over ground when the data was sent
     */
    private void checkCogRules(float cog) {
        if (!(cog >= -359 && cog <= 359))
            throw new IllegalPositioningDataException("COG value \"" + cog + "\"  is not withing boundaries");
    }

    /**
     * Sets the ship's speed over ground
     * @param sog The ship's speed over ground when the data was sent
     */
    public void setSog(float sog) {
        if (sog >= 0)
            this.sog = sog;
    }

    /**
     * Sets the ship's heading
     * @param heading The ship's heading when the data was sent
     */
    public void setHeading(float heading) {
        checkHeadingRules(heading);
        this.heading = heading;
    }

    /**
     * checks if the heading value is allowed
     * @param heading The ship's heading when the data was sent
     */
    private void checkHeadingRules(float heading) {
        if (!(heading >= 0 && heading <= 359 || heading == 511))
            throw new IllegalPositioningDataException("Heading value \"" + heading + "\"  is not withing boundaries");
    }

    /**
     * sets the ship's ship int tow code
     * @param position The ship's ship code in tow when the data was sent
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * Sets the ship's transceiver class
     * @param transceiverClass The ship's transceiver class used to send the data
     */
    public void setTransceiverClass(String transceiverClass) {
        checkTransceiversRules(transceiverClass);
        this.transceiverClass = transceiverClass;
    }

    /**
     * checks if the transceiver value is allowed
     * @param transceiverClass The ship's transceiver class used to send the data
     */
    public void checkTransceiversRules(String transceiverClass){
        if (transceiverClass == null)
            throw new IllegalPositioningDataException("Transceiver class must not be null");
    }

    /**
     * returns the date values
     * @return returns the date values
     */
    public Date getBdt() {
        return bdt;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public float getSog() {
        return sog;
    }

    public float getCog() {
        return cog;
    }

    public float getHeading() {
        return heading;
    }

    public String getPosition() {
        return position;
    }

    public String getTransceiverClass() {
        return transceiverClass;
    }

    @Override
    public int compareTo(PositioningData otherData) {
        return this.bdt.compareTo(otherData.bdt);
    }
}
