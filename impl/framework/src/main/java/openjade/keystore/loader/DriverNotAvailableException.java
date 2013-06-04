package openjade.keystore.loader;

public class DriverNotAvailableException extends KeyStoreLoaderException {

	private static final long serialVersionUID = 1L;

	public DriverNotAvailableException(String message, Throwable e) {
		super(message, e);
	}

	public DriverNotAvailableException(String message) {
		super(message);
	}
}
