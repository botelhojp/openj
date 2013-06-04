package openjade.keystore.loader.implementation;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import openjade.keystore.loader.KeyStoreLoader;
import openjade.keystore.loader.KeyStoreLoaderException;

public class KeyStoreLoaderImpl implements KeyStoreLoader {

	private static final String FILE_TYPE_PKCS12 = "PKCS12";
	private static final String FILE_TYPE_JKS = "JKS";
	private static final String FILE_LOAD_ERROR = "Error on load a keystore from file";
	private static final String FILE_NOT_VALID = "File invalid or not exist";

	private File fileKeyStore = null;
	private InputStream isKeystore = null;
	
	public KeyStoreLoaderImpl() {
	}

	public KeyStoreLoaderImpl(File file) {
		if (file == null || !file.exists() || !file.isFile())
			throw new KeyStoreLoaderException(FILE_NOT_VALID);
		this.setFileKeyStore(file);
	}

	public KeyStoreLoaderImpl(InputStream isKeystore) {
		if (isKeystore == null)
			throw new KeyStoreLoaderException(FILE_NOT_VALID);
		this.setIsKeystore(isKeystore);
	}

	private void setIsKeystore(InputStream isKeystore) {
		this.isKeystore = isKeystore;
	}

	public File getFileKeyStore() {
		return fileKeyStore;
	}

	public void setFileKeyStore(File fileKeyStore) {
		this.fileKeyStore = fileKeyStore;
	}

	public KeyStore getKeyStore(String pinNumber) {
		KeyStore result = null;
		try {
			result = this.getKeyStoreWithType(pinNumber, FILE_TYPE_PKCS12);
		} catch (Throwable throwable) {
			try {
				result = this.getKeyStoreWithType(pinNumber, FILE_TYPE_JKS);
			} catch (Throwable error) {
				throw new KeyStoreLoaderException("Error on load a KeyStore from file. KeyStore unknow format", throwable);
			}
		}
		return result;
	}

	private KeyStore getKeyStoreWithType(String pinNumber, String keyStoreType) {
		KeyStore result = null;
		try {
			result = KeyStore.getInstance(keyStoreType);
			char[] pwd = pinNumber == null ? null : pinNumber.toCharArray();
			InputStream is = (isKeystore != null) ? isKeystore : new FileInputStream(this.fileKeyStore);
			result.load(is, pwd);
		} catch (Throwable error) {
			throw new KeyStoreLoaderException(FILE_LOAD_ERROR, error);
		}
		return result;
	}
}
