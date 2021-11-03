package lapr.project.exception;

public class IllegalGeneratorException extends IllegalArgumentException{

    /**
     * throws an exception for the Generator class
     */
    public IllegalGeneratorException(){
        this(null);
    }

    /**
     * throws an exception for the Generator class with a costume message
     */
    public IllegalGeneratorException(String error){
        super(error);
    }
}
