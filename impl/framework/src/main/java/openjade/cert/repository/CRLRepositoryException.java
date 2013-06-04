package openjade.cert.repository;

/**
 * Reprente um erro de processamento do Repositorio
 */
public class CRLRepositoryException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão
	 */
    public CRLRepositoryException() {
    	super();
    }

    /**
     * Construtor com messagem
     * @param message
     */
    public CRLRepositoryException(String message) {
    	super(message);
    }


    /**
     * Construtor com mensagem e causa
     * @param message
     * @param cause
     */
    public CRLRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
