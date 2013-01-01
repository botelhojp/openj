package openjade.keystore.loader;

public class InvalidPinException extends KeyStoreLoaderException {

	private static final long serialVersionUID = 1L;

	public InvalidPinException(String message, Throwable e) {
		super(message, e);
	}

	public InvalidPinException(String message) {
		super(message);
	}

}
