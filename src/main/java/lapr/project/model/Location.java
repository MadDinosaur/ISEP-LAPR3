package lapr.project.model;

import lapr.project.exception.IllegalLocationException;

import java.util.Objects;

public abstract class Location {

    /**
     *  The Location's continent name
     */
    private String continent;

    /**
     *  The Location's country name
     */
    private String country;

    /**
     *  The Location's coordinate
     */
    private Coordinate coordinate;

    public Location(String continent, String country,Coordinate coordinate){
        setCountry(country);
        setContinent(continent);
        setCoordinate(coordinate);
    }

    /**
     * Sets the Location's country
     * @param country The Location's country
     */
    public void setCountry(String country){
        checkCountryRules(country);
        this.country = country;
    }

    /**
     * Checks the Location's country to see if it is within the allowed boundaries
     * @param country The Location's country
     */
    private void checkCountryRules(String country){
        if (country.length() > 21)
            throw new IllegalLocationException("Country \"" + country + "\" is not supported.");
    }

    /**
     * Sets the Location's coordinate
     * @param coordinate The Location's coordinate
     */
    public void setCoordinate(Coordinate coordinate){ this.coordinate = coordinate; }

    /**
     * Gets the Location's coordinate
     * @return The Location's coordinate
     */
    public Coordinate getCoordinate() { return coordinate; }

    /**
     * Gets the Location's continent
     * @return The Location's continent
     */
    public String getContinent() { return continent; }

    /**
     * Gets the Location's country
     * @return The Location's country
     */
    public String getCountry() { return country; }

    /**
     * Sets the Location's continent
     * @param continent The Location's continent
     */
    public void setContinent(String continent){
        checkContinentRules(continent);
        this.continent = continent;
    }

    /**
     * Checks the Location's continent to see if it is within the allowed boundaries
     * @param continent The Location's continent
     */
    private void checkContinentRules(String continent){
        if (continent.length() > 21)
            throw  new IllegalLocationException("Continent \"" + continent + "\" is not supported.");
    }

    /**
     * returns the distance between two locations
     * @param location the other location to Evaluated
     * @return returns the distance between the two locations
     */
    public double distanceBetween(Location location){
        return this.getCoordinate().getDistanceBetweenCoordinates(location.getCoordinate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (!Objects.equals(continent, location.continent)) return false;
        if (!Objects.equals(country, location.country)) return false;
        return Objects.equals(coordinate, location.coordinate);
    }
}
