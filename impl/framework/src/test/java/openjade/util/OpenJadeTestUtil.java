package openjade.util;

import java.io.File;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;

import openjade.keystore.loader.implementation.KeyStoreLoaderImpl;

public class OpenJadeTestUtil {

	protected final String PATH = OpenJadeTestUtil.class.getResource("/certs").getPath() + "/";

	protected String pinNumber = "123456";

	private PrivateKey privateKey;

	protected KeyStore getKeystore(String file) {
		return (new KeyStoreLoaderImpl(new File(PATH + file))).getKeyStore(pinNumber);
	}

	protected X509Certificate getX509(String file) {
		return getX509(file, pinNumber);
	}
	
	protected X509Certificate getX509(String file, String password) {
		try {
			KeyStore store = (new KeyStoreLoaderImpl(new File(PATH + file))).getKeyStore(pinNumber);
			String key = (String) store.aliases().nextElement();
			privateKey = (PrivateKey) store.getKey(key, pinNumber.toCharArray());
			X509Certificate cert = (X509Certificate) store.getCertificate(key);
			return cert;
		} catch (KeyStoreException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	
	

}
