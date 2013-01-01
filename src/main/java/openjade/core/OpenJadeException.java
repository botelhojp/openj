package openjade.core;

public class OpenJadeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OpenJadeException() {
		super();
	}

	public OpenJadeException(String message) {
		super(message);
	}

	public OpenJadeException(String message, Throwable cause) {
		super(message, cause);
	}

}
