package lapr.project.exception;

public class IllegalContainerException extends IllegalArgumentException{

    /**
     * throws an exception for the Container class
     */
    public IllegalContainerException(){
        this(null);
    }

    /**
     * throws an exception for the Container class with a costume message
     */
    public IllegalContainerException(String error){
        super(error);
    }
}