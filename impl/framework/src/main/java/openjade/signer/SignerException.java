package openjade.signer;

public class SignerException extends RuntimeException {

	private static final long serialVersionUID = 2644286427013050386L;
	
	public SignerException() {
		super();
	}
	
	public SignerException(String message) {
		super(message);
	}

	public SignerException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public SignerException(Throwable cause) {
		super(cause);
	}
}
