package openjade.core.behaviours;

public class BehaviourException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
    public BehaviourException() {
    	super();
    }

    public BehaviourException(String message) {
    	super(message);
    }

    public BehaviourException(String message, Throwable cause) {
        super(message, cause);
    }

}
