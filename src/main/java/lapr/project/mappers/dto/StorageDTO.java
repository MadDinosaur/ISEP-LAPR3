package lapr.project.mappers.dto;

public class StorageDTO {
    /**
     *  The storage's identification code
     */
    private String identification;

    /**
     *  The storage's name
     */
    private String name;

    /**
     *  The storage's continent name
     */
    private String continent;

    /**
     *  The storage's country name
     */
    private String country;

    /**
     *  The storage's longitude
     */
    private String longitude;

    /**
     * The storage's latitude
     */
    private String latitude;

    /**
     * Gets the Storage DTO's identification
     * @return the Storage DTO's identification
     */
    public String getIdentification() {
        return identification;
    }

    /**
     * Gets the Storage DTO's name
     * @return the Storage DTO's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the Storage DTO's continent
     * @return the Storage DTO's continent
     */
    public String getContinent() {
        return continent;
    }

    /**
     * Gets the Storage DTO's country
     * @return the Storage DTO's country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Gets the Storage DTO's longitude
     * @return the Storage DTO's longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Gets the Storage DTO's latitude
     * @return the Storage DTO's latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Creates the Storage DTO
     * @param identification the Storage DTO's identification
     * @param name the Storage DTO's name
     * @param continent the Storage DTO's continent
     * @param country the Storage DTO's country
     * @param longitude the Storage DTO's longitude
     * @param latitude the Storage DTO's latitude
     */
    public StorageDTO(String identification, String name, String continent, String country, String longitude, String latitude) {
        this.identification = identification;
        this.name = name;
        this.continent = continent;
        this.country = country;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
