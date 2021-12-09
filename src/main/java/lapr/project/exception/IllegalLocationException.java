package lapr.project.exception;

public class IllegalLocationException extends IllegalArgumentException{

    /**
     * throws an exception for the Location class
     */
    public IllegalLocationException(){
        this(null);
    }

    /**
     * throws an exception for the Location class with a costume message
     */
    public IllegalLocationException(String error){
        super(error);
    }
}
