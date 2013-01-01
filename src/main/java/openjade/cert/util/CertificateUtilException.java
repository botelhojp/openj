package openjade.cert.util;

/**
 * Default Exception package
 */
public class CertificateUtilException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
    public CertificateUtilException() {
    	super();
    }

    public CertificateUtilException(String message) {
    	super(message);
    }

    public CertificateUtilException(String message, Throwable cause) {
        super(message, cause);
    }

}
