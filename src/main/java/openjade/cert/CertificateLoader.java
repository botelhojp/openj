package openjade.cert;

import java.io.File;
import java.security.cert.X509Certificate;


/**
 * Carregamento de Certificados Digitais.
 */
public interface CertificateLoader {

	/**
	 * Obtem o certificado A1 a partir de um arquivo
	 * @param file
	 * @return
	 * @throws CertificateException
	 */
	public X509Certificate load(File file) throws CertificateException;

	/**
	 * Obtem o certificado A3 a partir de um dispositivo.
	 * @return
	 * @throws CertificateException
	 */
	public X509Certificate loadFromToken() throws CertificateException;
	
	/**
	 * Obtem o certificado A3 a partir de um dispositivo.
	 * @param pinNumber
	 * @return
	 * @throws CertificateException
	 */
	public X509Certificate loadFromToken(String pinNumber) throws CertificateException;

}
