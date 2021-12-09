package lapr.project.exception;

public class IllegalCountryException extends IllegalArgumentException{

    /**
     * throws an exception for the Location class
     */
    public IllegalCountryException(){
        this(null);
    }

    /**
     * throws an exception for the Location class with a costume message
     */
    public IllegalCountryException(String error){
        super(error);
    }
}