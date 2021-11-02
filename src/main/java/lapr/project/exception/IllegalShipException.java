package lapr.project.exception;

public class IllegalShipException extends IllegalArgumentException{

    /**
     * throws an exception for the Ship class
     */
    public IllegalShipException(){
        this(null);
    }

    /**
     * throws an exception for the Ship class with a costume message
     */
    public IllegalShipException(String error){
        super(error);
    }
}