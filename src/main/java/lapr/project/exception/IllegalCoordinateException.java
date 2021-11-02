package lapr.project.exception;

public class IllegalCoordinateException extends IllegalArgumentException{

    /**
     * throws an exception for the coordinate class
     */
    public IllegalCoordinateException(){
        this(null);
    }

    /**
     * throws an exception for the coordinate class with a costume message
     */
    public IllegalCoordinateException(String error){
        super(error);
    }
}
