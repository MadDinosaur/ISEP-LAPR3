package lapr.project.model;

import lapr.project.exception.IllegalShipException;
import lapr.project.store.list.PositioningDataTree;

public class Ship implements Comparable<Ship> {

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
    private int imo;

    /**
     * The ships generator information
     * placeHolder as it won't be used this sprint
     */
    private Generator generator;


    /**
     * The Ships call sign
     */
    private String callSign;

    /**
     * The ship's vessel type
     */
    private int vesselType;

    /**
     * The ship's length
     */
    private float length;

    /**
     * The ship's width
     */
    private float width;

    /**
     * The ship's capacity
     * placeHolder as it won't be used this sprint
     */
    private float capacity;

    /**
     * The ship's draft
     */
    private float draft;

    /**
     * The ship's positioning data
     */
    private PositioningDataTree positioningDataTree;

    /**
     * Creates the object Ship
     * @param mmsi The ship's mmsi identification code
     * @param shipName The ship's name
     * @param imo The ships IMO code
     * @param callSign The Ships call sign
     * @param vesselType The ship's vessel type
     * @param length The ship's length
     * @param width The ship's length
     * @param draft The ship's draft
     */
    public Ship(String mmsi, String shipName, int imo, String callSign, int vesselType, float length, float width, float draft){
        setMmsi(mmsi);
        setShipName(shipName);
        setImo(imo);
        setCallSign(callSign);
        setVesselType(vesselType);
        setLength(length);
        setWidth(width);
        setDraft(draft);
        capacity = 0;
    }

    /**
     * Set's the Ships MMSI value
     * @param mmsi The ship's MMSI identification code
     */
    public void setMmsi(String mmsi) {
        checkMmsiRules(mmsi);
        this.mmsi = mmsi;
    }

    /**
     * Checks the MMSI value to see if it is within the allowed boundaries
     * @param mmsi The ship's MMSI identification code
     */
    private void checkMmsiRules(String mmsi) {
        if (mmsi.length() != 9)
            throw new IllegalShipException("MMSI code \"" + mmsi + "\" is not supported.");
    }

    /**
     * Set's the ship's name
     * @param shipName  The ship's name
     */
    public void setShipName(String shipName) {
        if (shipName != null)
            this.shipName = shipName;
        else
            throw new IllegalShipException("Ship Name Must not me null.");
    }

    /**
     * Set's the ship's positions positioning data
     * @param positioningDataTree The ship's positions positioning data
     */
    public void setPositioningDataList(PositioningDataTree positioningDataTree) {
        this.positioningDataTree = positioningDataTree;
    }

    /**
     * Set's the ships Call sign
     * @param callSign The ships Call sign
     */
    public void setCallSign(String callSign) {
        if (callSign != null)
            this.callSign = callSign;
        else
            throw new IllegalShipException("Call Name Must not me null.");

    }

    /**
     * Set's the ships Draft
     * @param draft The ships Draft
     */
    public void setDraft(float draft) {
        checkDraftRules(draft);
        this.draft = draft;
    }

    /**
     * Check the ship's draft rules
     * @param draft The ships Draft
     */
    private void checkDraftRules(float draft) {
        if (draft < 0)
            throw new IllegalShipException("Draft  \"" + draft + "\" is not supported.");
    }

    /**
     * Set's the ship's IMO code
     * @param imo The ships IMO code
     */
    public void setImo(int imo) {
        checkIMORules(imo);
        this.imo = imo;
    }

    /**
     * Checks the IMO value to see if it is within the allowed boundaries
     * @param imo The ships IMO code
     */
    private void checkIMORules(int imo) {
        if (!(imo >= 1000000 && imo <= 9999999))
            throw new IllegalShipException("IMO code \"" + imo + "\" is not supported.");
    }

    /**
     * Set's the ships length value
     * @param length The ship's length value
     */
    public void setLength(float length) {
        checkLengthRules(length);
        this.length = length;
    }

    /**
     * Checks the length value to see if it is within the allowed boundaries
     * @param length The ship's length value
     */
    private void checkLengthRules(float length) {
        if (length < 0)
            throw new IllegalShipException("Length  \"" + length + "\" is not supported.");
    }

    /**
     * Set the vessel's type
     * @param vesselType The ship's vessel type
     */
    public void setVesselType(int vesselType) {
        checkVesselTypeRules(vesselType);
        this.vesselType = vesselType;
    }

    /**
     * Checks the vessel type value to see if it is within the allowed boundaries
     * @param vesselType The ship's vessel type
     */
    private void checkVesselTypeRules(int vesselType) {
        if ((vesselType < 0))
            throw new IllegalShipException("The vessel type code \"" + vesselType + "\" is not supported.");
    }

    /**
     * Set's the ship's width
     * @param width The ship's width
     */
    public void setWidth(float width) {
        checkWidthRules(width);
        this.width = width;
    }

    /**
     * Checks the width value to see if it is within the allowed boundaries
     * @param width The ship's width
     */
    private void checkWidthRules(float width) {
        if (width < 0)
            throw new IllegalShipException("Width  \"" + width + "\" is not supported.");
    }

    /**
     * returns the ship's name
     * @return returns the ship's name
     */
    public String getShipName() {
        return shipName;
    }

    /**
     * returns the ship's mmsi
     * @return returns the ship's mmsi
     */
    public String getMmsi() {
        return mmsi;
    }

    /**
     * returns the ship's call sign
     * @return returns the ship's call sign
     */
    public String getCallSign() {
        return callSign;
    }

    /**
     * returns the ship's imo
     * @return returns the ship's imo
     */
    public int getImo() {
        return imo;
    }

    /**
     * returns the ship's vessel type
     * @return returns the ship's vessel type
     */
    public int getVesselType() {
        return vesselType;
    }

    /**
     * returns the ship's draft
     * @return returns the ship's draft
     */
    public float getDraft() {
        return draft;
    }

    /**
     * returns the ship's length
     * @return returns the ship's length
     */
    public float getLength() {
        return length;
    }

    /**
     * returns the ship's width
     * @return returns the ship's width
     */
    public float getWidth() {
        return width;
    }

    /**
     * returns the ship's positioning data
     * @return returns the ship's positioning data
     */
    public PositioningDataTree getPositioningDataList() {
        return positioningDataTree;
    }

    /**
     * returns the ship's generator data
     * @return returns the ship's generator
     */
    public Generator getGenerator() {
        return generator;
    }

    /**
     * returns the ship's capacity
     * @return returns the ship's capacity
     */
    public float getCapacity() {
        return capacity;
    }

    /**
     * default comparable to the ship class
     * @param o the other ship to compare to
     * @return returns 1 if the other ship is smaller, -1 if it is bigger and 0 if it is equal
     */
    @Override
    public int compareTo(Ship o) {
        return mmsi.compareTo(o.getMmsi());
    }

}
