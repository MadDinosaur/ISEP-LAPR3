package lapr.project.mappers.dto;

public class ShipDTO {

    /**
     * The ship's MMSI identification code
     */
    private String mmsi;

    /**
     * The ship's name
     */
    private String shipName;

    /**
     * The ships IMO code
     */
    private String imo;

    /**
     * The Ships call sign
     */
    private String callSign;

    /**
     * The ship's vessel type
     */
    private String vesselType;

    /**
     * The ship's length
     */
    private String length;

    /**
     * The ship's width
     */
    private String width;

    /**
     * The ship's draft
     */
    private String draft;

    /**
     * creates a package object of a Ship object
     * @param mmsi The ship's mmsi identification code
     * @param shipName The ship's name
     * @param imo The ships IMO code
     * @param callSign The Ships call sign
     * @param vesselType The ship's vessel type
     * @param length The ship's length
     * @param width The ship's length
     * @param draft The ship's draft
     */
    public ShipDTO(String mmsi, String shipName, String imo, String callSign, String vesselType, String length, String width, String draft){
        this.mmsi = mmsi;
        this.shipName = shipName;
        this.imo = imo;
        this.callSign = callSign;
        this.vesselType = vesselType;
        this.length = length;
        this.width = width;
        this.draft = draft;
    }

    /**
     * returns the Call sign value
     * @return returns the Call sign value
     */
    public String getCallSign() {
        return callSign;
    }

    /**
     * returns the Draft value
     * @return returns the Draft value
     */
    public String getDraft() {
        return draft;
    }

    /**
     * returns the IMO value
     * @return returns the IMO value
     */
    public String getImo() {
        return imo;
    }

    /**
     * returns the Length value
     * @return returns the Length value
     */
    public String getLength() {
        return length;
    }

    /**
     * returns the MSI value
     * @return returns the MSI value
     */
    public String getMmsi() {
        return mmsi;
    }

    /**
     * returns the Ship name value
     * @return returns the Ship name value
     */
    public String getShipName() {
        return shipName;
    }

    /**
     * returns the vessel type value
     * @return returns the vessel type value
     */
    public String getVesselType() {
        return vesselType;
    }

    /**
     * returns the width value
     * @return returns the width value
     */
    public String getWidth() {
        return width;
    }
}
