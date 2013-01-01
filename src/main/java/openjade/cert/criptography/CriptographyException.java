package openjade.cert.criptography;

public class CriptographyException extends RuntimeException {

	private static final long serialVersionUID = 2644286427013050386L;
	
	public CriptographyException() {
		super();
	}
	
	public CriptographyException(String message) {
		super(message);
	}

	public CriptographyException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public CriptographyException(Throwable cause) {
		super(cause);
	}
}
