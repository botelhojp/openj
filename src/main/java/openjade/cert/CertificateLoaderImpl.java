package openjade.cert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import openjade.keystore.loader.KeyStoreLoader;
import openjade.keystore.loader.implementation.KeyStoreLoaderImpl;

public class CertificateLoaderImpl implements CertificateLoader {

	public X509Certificate load(File file) {
		try {
			FileInputStream fileInput = new FileInputStream(file);
			return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(fileInput);
		} catch (FileNotFoundException e) {
			throw new CertificateException("FileNotFoundException", e);
		} catch (CertificateException e) {
			throw new CertificateException("CertificateException", e);
		} catch (java.security.cert.CertificateException e) {
			throw new CertificateException("CertificateException", e);
		}
	}

	public X509Certificate loadFromToken() {
		return loadFromToken(null);
	}
	
	public X509Certificate loadFromToken(String pinNumber) {
		KeyStoreLoader keyStoreLoader = new KeyStoreLoaderImpl();
		KeyStore keystore = keyStoreLoader.getKeyStore(pinNumber);
		String alias;
		try {
			alias = keystore.aliases().nextElement();
			return (X509Certificate) keystore.getCertificateChain(alias)[0];	
		} catch (KeyStoreException e) {
			throw new CertificateValidatorException("",e);
		}		
	}

}
