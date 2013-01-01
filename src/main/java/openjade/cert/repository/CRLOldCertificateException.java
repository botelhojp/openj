package openjade.cert.repository;

/**
 * Defini exception para clr antiga
 */
public class CRLOldCertificateException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	

	/**
	 * Construtor padrão
	 */
    public CRLOldCertificateException() {
    	super();
    }

    /**
     * Construtor com messagem
     * @param message
     */
    public CRLOldCertificateException(String message) {
    	super(message);
    }


    /**
     * Construtor com mensagem e causa
     * @param message
     * @param cause
     */
    public CRLOldCertificateException(String message, Throwable cause) {
        super(message, cause);
    }
}
