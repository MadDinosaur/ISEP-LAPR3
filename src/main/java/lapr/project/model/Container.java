package lapr.project.model;

import lapr.project.exception.IllegalContainerException;

public class Container {
    /**
     * The container identification number
     */
    private int containerNum;
    /**
     * The container check digit
     */
    private int checkDigit;
    /**
     * The container iso code
     */
    private String isoCode;
    /**
     * The container gross weight, in kgs
     */
    private double grossWeight;
    /**
     * The container tare weight, in kgs
     */
    private double tareWeight;
    /**
     * The container payload, in kgs
     */
    private double payload;
    /**
     * The container maximum volume
     */
    private double maxVolume;
    /**
     * The container refrigeration capability
     */
    private boolean refrigerated;
    /**
     * The container standard length (m)
     */
    private static double length = 12.19;
    /**
     * The container standard width (m)
     */
    private static double width = 2.44;
    /**
     * The container standard height (m)
     */
    private static double height = 2.9;

    /**
     * Creates the Container object
     *
     * @param containerNum the container identification number
     * @param checkDigit   the container check digit
     * @param isoCode      the container iso code
     * @param grossWeight  the container gross weight
     * @param tareWeight   the container tare weight
     * @param payload      the container payload
     * @param maxVolume    the container maximum volume
     * @param refrigerated the container refrigeration
     */
    public Container(int containerNum, int checkDigit, String isoCode, double grossWeight, double tareWeight, double payload, double maxVolume, boolean refrigerated) {
        setContainerNum(containerNum);
        setCheckDigit(checkDigit);
        setIsoCode(isoCode);
        setGrossWeight(grossWeight);
        setTareWeight(tareWeight);
        setPayload(payload);
        setMaxVolume(maxVolume);
        setRefrigerated(refrigerated);
    }

    /**
     * @return the container number
     */
    public int getContainerNum() {
        return containerNum;
    }

    /**
     * Sets the container number
     *
     * @param containerNum the container number
     */
    private void setContainerNum(int containerNum) {
        this.containerNum = containerNum;
    }

    /**
     * @return the container check digit
     */
    public int getCheckDigit() {
        return checkDigit;
    }

    /**
     * Checks the check digit value to see if it's within the allowed boundaries
     *
     * @param checkDigit the container check digit
     */
    private void checkCheckDigit(int checkDigit) {
        if (checkDigit < 0 || checkDigit > 9)
            throw new IllegalContainerException("Check digit must be between 0 and 9.");
    }

    /**
     * Sets the check digit
     *
     * @param checkDigit the container's check digit
     */
    private void setCheckDigit(int checkDigit) {
        checkCheckDigit(checkDigit);
        this.checkDigit = checkDigit;
    }

    /**
     * @return the container's iso code
     */
    public String getIsoCode() {
        return isoCode;
    }

    /**
     * Checks the iso code value to see if it's within the allowed boundaries
     *
     * @param isoCode the container's iso code
     */
    private void checkIsoCode(String isoCode) {
        if (isoCode == null || isoCode.length() != 4)
            throw new IllegalContainerException("Iso code must have 4 characters.");
    }

    /**
     * Sets the iso code
     *
     * @param isoCode the container's iso code
     */
    private void setIsoCode(String isoCode) {
        checkIsoCode(isoCode);
        this.isoCode = isoCode;
    }

    /**
     * @return the container's gross weight
     */
    public double getGrossWeight() {
        return grossWeight;
    }

    /**
     * Checks the gross weight value to see if it's within the allowed boundaries
     *
     * @param grossWeight the container's gross weight
     */
    private void checkGrossWeight(double grossWeight) {
        if (grossWeight < 0)
            throw new IllegalContainerException("Gross weight must be a positive number.");
    }

    /**
     * Sets the gross weight
     *
     * @param grossWeight the container's gross weight
     */
    public void setGrossWeight(double grossWeight) {
        checkGrossWeight(grossWeight);
        this.grossWeight = grossWeight;
    }

    /**
     * @return the container's tare weight
     */
    public double getTareWeight() {
        return tareWeight;
    }

    /**
     * Checks the tare weight value to see if it's within the allowed boundaries
     *
     * @param tareWeight the container's gross weight
     */
    private void checkTareWeight(double tareWeight) {
        if (tareWeight < 0)
            throw new IllegalContainerException("Tare weight must be a positive number.");
    }

    /**
     * Sets the tare weight
     *
     * @param tareWeight the container's tare weight
     */
    public void setTareWeight(double tareWeight) {
        checkTareWeight(tareWeight);
        this.tareWeight = tareWeight;
    }

    /**
     * Checks the payload value to see if it's within the allowed boundaries
     *
     * @param payload the container's payload
     */
    private void checkPayload(double payload) {
        if (payload < 0)
            throw new IllegalContainerException("Payload must be a positive number.");
    }

    /**
     * @return the container's payload
     */
    public double getPayload() {
        return payload;
    }

    /**
     * Sets the paylaod value
     *
     * @param payload the container's payload
     */
    public void setPayload(double payload) {
        checkPayload(payload);
        this.payload = payload;
    }

    /**
     * @return the container's maximum volume
     */
    public double getMaxVolume() {
        return maxVolume;
    }

    /**
     * Checks the maximum volume value to see if it's within the allowed boundaries
     *
     * @param maxVolume the container's maximum volume
     */
    private void checkMaxVolume(double maxVolume) {
        if (maxVolume < 0)
            throw new IllegalContainerException("Max. volume must be a positive number.");
    }

    /**
     * Sets the maximum volume
     *
     * @param maxVolume the container's max volume
     */
    private void setMaxVolume(double maxVolume) {
        checkMaxVolume(maxVolume);
        this.maxVolume = maxVolume;
    }

    /**
     * @return true if container is refrigerated, false if its not
     */
    public boolean isRefrigerated() {
        return refrigerated;
    }

    /**
     * Sets the container refrigeration
     * @param refrigerated true if container is refrigerated, false if its not
     */
    private void setRefrigerated(boolean refrigerated) {
        this.refrigerated = refrigerated;
    }

    /**
     * @return the container standardized length
     */
    public static double getLength() {
        return length;
    }
    /**
     * @return the container standardized width
     */
    public static double getWidth() {
        return width;
    }
    /**
     * @return the container standardized height
     */
    public static double getHeight() {
        return height;
    }
}
