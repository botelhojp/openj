package openjade.cert;

/**
 * Default Exception package
 */
public class CertificateValidatorException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Default constructor
	 */
    public CertificateValidatorException() {
    	super();
    }

	/**
	 * Constructor with message
	 * 
	 * @param message message of exception
	 */
    public CertificateValidatorException(String message) {
    	super(message);
    }

	/**
	 * Constructor with message and cause
	 * 
	 * @param message message of exception
	 * @param cause cause of exception
	 */
    public CertificateValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

}
