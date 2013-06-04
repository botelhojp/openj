package openjade.cert.repository;

/**
 * Defini exceção para arquivos de CRL não encontrados
 */
public class CRLFileNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	

	/**
	 * Construtor padrão
	 */
    public CRLFileNotFoundException() {
    	super();
    }

    /**
     * Construtor com messagem
     * @param message
     */
    public CRLFileNotFoundException(String message) {
    	super(message);
    }


    /**
     * Construtor com mensagem e causa
     * @param message
     * @param cause
     */
    public CRLFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
