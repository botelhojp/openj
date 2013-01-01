package openjade.keystore.loader.implementation;

import java.security.KeyStore;

import openjade.keystore.loader.KeyStoreLoader;
import openjade.keystore.loader.KeyStoreLoaderException;

/**
 * Implementação de KeyStoreLoader baseado no Provider
 * específico do Windows que acompanha a distribuição da
 * Sun JVM 1.6.
 */
public class MSKeyStoreLoader implements KeyStoreLoader {
	
	protected static final String MS_PROVIDER = "SunMSCAPI";
	protected static final String MS_TYPE = "Windows-MY";
	protected static final String MS_ERROR_LOAD = "Error on load a KeyStore from SunMSCAPI";

	public KeyStore getKeyStore(String pinNumber) {
		try {
			KeyStore result = KeyStore.getInstance(MSKeyStoreLoader.MS_TYPE, MSKeyStoreLoader.MS_PROVIDER);
			result.load(null, null);
			return result;
		} catch (Throwable error) {
			throw new KeyStoreLoaderException(MSKeyStoreLoader.MS_ERROR_LOAD, error);
		}
	}

}
