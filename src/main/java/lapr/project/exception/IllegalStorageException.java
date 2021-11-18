package lapr.project.exception;

public class IllegalStorageException extends IllegalArgumentException{

    /**
     * throws an exception for the Storage class
     */
    public IllegalStorageException(){
        this(null);
    }

    /**
     * throws an exception for the Storage class with a costume message
     */
    public IllegalStorageException(String error){
        super(error);
    }
}
