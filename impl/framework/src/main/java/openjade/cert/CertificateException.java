package openjade.cert;

public class CertificateException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
    public CertificateException() {
    	super();
    }

    public CertificateException(String message) {
    	super(message);
    }

    public CertificateException(String message, Throwable cause) {
        super(message, cause);
    }

}
