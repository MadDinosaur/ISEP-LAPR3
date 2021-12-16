package lapr.project.mappers.dto;

import lapr.project.model.Country;

public class CountryDTO {

    /**
     *  The Country's continent name
     */
    private String continent;

    /**
     *  The Country's country name
     */
    private String country;

    /**
     *  The Country's longitude
     */
    private String longitude;

    /**
     * The Country's latitude
     */
    private String latitude;

    /**
     * the country's capital
     */
    private String capital;

    /**
     * the country's alpha2-code
     */
    private String alpha2;

    /**
     * the country's alpha3-code
     */
    private String alpha3;

    /**
     * the country's population
     */
    private String population;

    /**
     *
     * @param continent The Country's continent name
     * @param country The Country's country name
     * @param longitude The Country's longitude
     * @param latitude The Country's latitude
     * @param capital The Country's capital
     * @param alpha2 The Country's alpha2-code
     * @param alpha3 The Country's alpha3-code
     * @param population The country's population
     */
    public CountryDTO(String continent, String country, String longitude, String latitude, String capital, String alpha2, String alpha3, String population){
        this.continent = continent;
        this.country = country;
        this.longitude = longitude;
        this.latitude = latitude;
        this.capital = capital;
        this.alpha2 = alpha2;
        this.alpha3 = alpha3;
        this.population = population;
    }

    /**
     * returns the country's alpha2 code
     * @return returns the country's alpha2 code
     */
    public String getAlpha2() {
        return alpha2;
    }

    /**
     * returns the country's alpha3 code
     * @return returns the country's alpha3 code
     */
    public String getAlpha3() {
        return alpha3;
    }

    /**
     * returns the country's capital
     * @return returns the country's capital
     */
    public String getCapital() {
        return capital;
    }

    /**
     * returns the country's continent
     * @return returns the country's continent
     */
    public String getContinent() {
        return continent;
    }

    /**
     * returns the country's country
     * @return returns the country's country
     */
    public String getCountry() {
        return country;
    }

    /**
     * returns the capital's latitude
     * @return returns the capital's latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * return the capital's longitude
     * @return return the capital's longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * returns the capitals population
     * @return returns the capitals population
     */
    public String getPopulation() {
        return population;
    }
}
