package lapr.project.model;

import lapr.project.exception.IllegalStorageException;

public class Storage extends Location {


    /**
     *  The storage's identification code
     */
    private int identification;

    /**
     *  The storage's name
     */
    private String name;

    public Storage(int identification,String name, String continent, String country,Coordinate coordinate){
        super(continent, country, coordinate);
        setIdentification(identification);
        setName(name);
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

        String identification2 = Integer.toString(id);
        if (identification2.length() > 10)
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
     * Gets the storage's identification
     * @return The storage's identification
     */
    public int getIdentification() { return identification; }

    /**
     * Gets the storage's name
     * @return The storage's name
     */
    public String getName() { return name; }

    /**
     * Transforms the storage into a string
     * @return a string version of the storage
     */
    @Override
    public String toString() {
        return String.format("Storage %d: Name - %s; Continent - %s; Country - %s; Longitude - %.2f; Latitude - %.2f", identification, name, getContinent(), getCountry(), getCoordinate().getLongitude(), getCoordinate().getLatitude());
    }

    /**
     * compares this object with another and returns true if their parameters are equal
     * @param o the other object
     * @return true if their parameters are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Storage)) return false;
        if (!super.equals(o)) return false;

        Storage storage = (Storage) o;

        if (getIdentification() != storage.getIdentification()) return false;
        return getName() != null ? getName().equals(storage.getName()) : storage.getName() == null;
    }
}
