package lapr.project.exception;

public class IllegalPositioningDataException extends IllegalArgumentException{

    /**
     * throws an exception for the PositioningData class
     */
    public IllegalPositioningDataException(){
        this(null);
    }

    /**
     * throws an exception for the PositioningData class with a costume message
     */
    public IllegalPositioningDataException(String error){
        super(error);
    }
}
