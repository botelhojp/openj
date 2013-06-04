package openjade.certbuilder;

public class CertBuilderException extends RuntimeException {

	private static final long serialVersionUID = 2644286427013050386L;
	
	public CertBuilderException() {
		super();
	}
	
	public CertBuilderException(String message) {
		super(message);
	}

	public CertBuilderException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public CertBuilderException(Throwable cause) {
		super(cause);
	}
}
