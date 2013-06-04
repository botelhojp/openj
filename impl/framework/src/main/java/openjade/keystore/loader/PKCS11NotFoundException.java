package openjade.keystore.loader;

public class PKCS11NotFoundException extends KeyStoreLoaderException {

	private static final long serialVersionUID = 1L;

	public PKCS11NotFoundException(String message, Throwable e) {
		super(message, e);
	}

	public PKCS11NotFoundException(String message) {
		super(message);
	}
}
