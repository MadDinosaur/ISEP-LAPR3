package lapr.project.model;

import lapr.project.exception.IllegalStorageException;

public class Storage  {


    /**
     *  The storage's identification code
     */
    private int identification;

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
     *  The storage's coordinate
     */
    private Coordinate coordinate;

    public Storage(int identification,String name, String continent, String country,Coordinate coordinate){
        setIdentification(identification);
        setName(name);
        setCountry(country);
        setContinent(continent);
        setCoordinate(coordinate);
    }


    /**
     * Sets the storage's identification
     * @param identification The storage's identification
     */
    public void setIdentification(int identification){
        checkIdentificationRules(identification);
        this.identification = identification;
    }

    private void checkIdentificationRules(int id) {
        if (id == 0)
            throw new IllegalStorageException("Identification \"" + id + "\" is not supported. (Cannot be empty or 0))");

        String identification = Integer.toString(id);
        if (identification.length() > 10)
            throw new IllegalStorageException("Identification \"" + id + "\" is not supported. (Cannot be bigger than 10 characters)");
    }

    /**
     * Sets the storage's name
     * @param name The storage's name
     */
    public void setName(String name){
        checkNameRules(name);
        this.name = name;
    }

    /**
     * Checks the storage's name to see if it is within the allowed boundaries
     * @param name The storage's name
     */
    private void checkNameRules(String name){
        if (name.length() > 21)
            throw new IllegalStorageException("Name \"" + name + "\" is not supported.");
    }

    /**
     * Sets the storage's continent
     * @param continent The storage's continent
     */
    public void setContinent(String continent){
        checkContinentRules(continent);
        this.continent = continent;
    }

    /**
     * Checks the storage's continent to see if it is within the allowed boundaries
     * @param continent The storage's continent
     */
    private void checkContinentRules(String continent){
        if (continent.length() > 21)
            throw  new IllegalStorageException("Continent \"" + continent + "\" is not supported.");
    }

    /**
     * Sets the storage's country
     * @param country The storage's country
     */
    public void setCountry(String country){
        checkCountryRules(country);
        this.country = country;
    }

    /**
     * Checks the storage's country to see if it is within the allowed boundaries
     * @param country The storage's country
     */
    private void checkCountryRules(String country){
        if (country.length() > 21)
            throw new IllegalStorageException("Country \"" + country + "\" is not supported.");
    }

    /**
     * Sets the storage's coordinate
     * @param coordinate The storage's coordinate
     */
    public void setCoordinate(Coordinate coordinate){ this.coordinate = coordinate; }

    /**
     * Gets the storage's coordinate
     * @return The storage's coordinate
     */
    public Coordinate getCoordinate() { return coordinate; }

    /**
     * Gets the storage's identification
     * @return The storage's identification
     */
    public int getIdentification() { return identification; }

    /**
     * Gets the storage's continent
     * @return The storage's continent
     */
    public String getContinent() { return continent; }

    /**
     * Gets the storage's country
     * @return The storage's country
     */
    public String getCountry() { return country; }

    /**
     * Gets the storage's name
     * @return The storage's name
     */
    public String getName() { return name; }

    @Override
    public String toString() {
        return String.format("Storage %d: Name - %s; Continent - %s; Country - %s; Longitude - %f; Latitude - %f", identification, name, continent, country, coordinate.getLongitude(), coordinate.getLatitude());
    }
}
