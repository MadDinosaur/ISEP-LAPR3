package lapr.project.exception;

public class UnauthorizedOperationException extends IllegalArgumentException{

    /**
     * throws an exception for unauthorized access
     */
    public UnauthorizedOperationException(){
        this(null);
    }

    /**
     * throws an exception for unauthorized access with a costume message
     */
    public UnauthorizedOperationException(String error){
        super(error);
    }
}