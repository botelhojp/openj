package openjade.keystore.loader;

import java.util.HashSet;
import java.util.Set;

public class KeyStoreLoaderException extends RuntimeException {

	private static final long serialVersionUID = -8414095761444262719L;

	private Set<Throwable> errors;

	public KeyStoreLoaderException() {
		super();
	}

	public KeyStoreLoaderException(String message) {
		super(message);
	}

	public KeyStoreLoaderException(String message, Throwable cause) {
		super(message, cause);
	}

	public void addError(Throwable error) {
		if (this.errors == null)
			this.errors = new HashSet<Throwable>();
		this.errors.add(error);
	}
	
	public Set<Throwable> getErrors() {
		return this.errors;
	}
	
	public boolean hasErrors() {
		return !errors.isEmpty();
	}

}
