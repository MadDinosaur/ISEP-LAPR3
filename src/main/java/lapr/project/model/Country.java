package lapr.project.model;

import lapr.project.exception.IllegalCountryException;

public class Country extends Location {

    /**
     * the country's alpha2 code
     */
    private String alpha2;

    /**
     * the country's alpha3 code
     */
    private String alpha3;

    /**
     * the country's population in millions
     */
    private int population;

    /**
     * the country's capital
     */
    private String capital;

    /**
     * build teh country object with all of it's important information
     * @param continent the country's continent
     * @param country the country's name
     * @param coordinate the country's capital coordinate
     * @param alpha2 the country's alpha2 code
     * @param alpha3 the country's alpha3 code
     * @param population the country's population
     * @param capital the country's capital
     */
    public Country(String continent, String country, Coordinate coordinate, String alpha2, String alpha3, int population, String capital) {
        super(continent, country, coordinate);
        setAlpha2(alpha2);
        setAlpha3(alpha3);
        setPopulation(population);
        setCapital(capital);
    }

    /**
     * set's the country alpha2 code
     * @param alpha2 the country's alpha2 code
     */
    public void setAlpha2(String alpha2) {
        checkAlpha2Rules(alpha2);
        this.alpha2 = alpha2;
    }

    /**
     * checks if the country's alpha 2 code is made up of 2 characters
     * @param alpha2 the country's alpha 2 code
     */
    private void checkAlpha2Rules(String alpha2) {
        if (alpha2.length() != 2)
            throw new IllegalCountryException("Alpha 2 code \"" + alpha2+"\" must have 2 digits");
    }

    /**
     * set's the country alpha3 code
     * @param alpha3 The country's alpha3 code
     */
    public void setAlpha3(String alpha3) {
        checkAlpha3Rules(alpha3);
        this.alpha3 = alpha3;
    }

    /**
     * checks if the country's alpha 3 code is made up of 3 characters
     * @param alpha3 the country's alpha 3 code
     */
    private void checkAlpha3Rules(String alpha3) {
        if (alpha3.length() != 3)
            throw new IllegalCountryException("Alpha 3 code \"" + alpha3+"\" must have 3 digits");
    }

    /**
     * set's the country's population
     * @param population The country's population
     */
    public void setPopulation(int population) {
        checkPopulationRules(population);
        this.population = population;
    }

    /**
     * checks  if the population values is positive
     * @param population The country's population
     */
    private void checkPopulationRules(float population) {
        if (population < 0)
            throw new IllegalCountryException("Population \"" + population + "\" cannot be negative");
    }

    /**
     * sets the country's capital
     * @param capital the country's capital
     */
    public void setCapital(String capital) {
        checkCapitalRules(capital);
        this.capital = capital;
    }

    /**
     * Checks if the capital is valid
     * @param capital the country's capital
     */
    private void checkCapitalRules(String capital) {
        if (capital.length() > 21)
            throw new IllegalCountryException("Capital \"" + capital + "\" is not supported.");
    }

    /**
     * returns the country's population
     * @return the country's population
     */
    public int getPopulation() {
        return population;
    }

    /**
     * returns the country's alpha 2 code
     * @return the country's alpha 2 code
     */
    public String getAlpha2() {
        return alpha2;
    }

    /**
     * returns the country's alpha 3 code
     * @return the country's alpha 3 code
     */
    public String getAlpha3() {
        return alpha3;
    }

    /**
     * returns the country's capital
     * @return the country's capital
     */
    public String getCapital() {
        return capital;
    }

    /**
     * Transforms the country to a string format
     * @return the country in a string format
     */
    @Override
    public String toString() {
        return String.format("Continent - %s; Country - %s; Capital - %s; Longitude - %.3f; Latitude - %.3f", getContinent(), getCountry(), capital, getCoordinate().getLongitude(), getCoordinate().getLatitude());
    }

    /**
     * compares this object with another and returns true if their parameters are equal
     * @param o the other object
     * @return true if their parameters are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country)) return false;
        if (!super.equals(o)) return false;

        Country country = (Country) o;

        if (Float.compare(country.getPopulation(), getPopulation()) != 0) return false;
        if (getAlpha2() != null ? !getAlpha2().equals(country.getAlpha2()) : country.getAlpha2() != null) return false;
        if (getAlpha3() != null ? !getAlpha3().equals(country.getAlpha3()) : country.getAlpha3() != null) return false;
        return getCapital().equals(country.getCapital());
    }
}
