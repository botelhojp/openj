package openjade.cert.extension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;

import openjade.cert.util.Base64Utils;


/**
 * Provide utilities for Certificate Revocation List, by ICP-BRASIL Patterns  
 * 
 */
public class ICPBR_CRL {
	
private X509CRL crl = null;
	
	/**
	 * 
	 * @param is -> InputStream 
	 * @throws CRLException
	 * @throws CertificateException
	 */
	public ICPBR_CRL(InputStream is) throws CRLException, CertificateException{
		this.crl = getInstance(is);
	}

	/**
	 * 
	 * @param data -> byte array
	 * @throws CRLException
	 * @throws CertificateException
	 * @throws IOException
	 */
	public ICPBR_CRL(byte[] data) throws CRLException, CertificateException, IOException{
		this.crl = getInstance(data);
	}
	
	/**
	 *  
	 * @param data -> byte array  
	 * @return Object X509CRL
	 * @see java.security.cert.X509CRL
	 * @throws CRLException
	 * @throws IOException
	 * @throws CertificateException
	 */
	private X509CRL getInstance(byte[] data) throws CRLException, IOException, CertificateException{
		X509CRL crl = null;
		
		try {
			//Tenta carregar a CRL como se fosse um arquivo binario!
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			crl = getInstance(bis);
			bis.close();
			bis = null;
		} catch (CRLException e) {
			//Nao conseguiu carregar o arquivo. Verifica se ele esta codificado em Base64
			byte[] data2 = null;
			try {
				data2 = Base64Utils.base64Decode(new String(data));
			} catch (Exception e2) {
				//Nao foi possivel decodificar o arquivo em Base64
				throw e;
			}
			
			ByteArrayInputStream bis = new ByteArrayInputStream(data2);
			crl = getInstance(bis);
			bis.close();
			bis = null;
		}
		
		return crl;
	}
	
	/**
	 * 
	 * @param is -> InputStream 
	 * @return Objeto X509CRL
	 * @see java.security.cert.X509CRL
	 * @throws CRLException
	 * @throws CertificateException
	 * @throws CertificateException 
	 */
	private X509CRL getInstance(InputStream is) throws CRLException, CertificateException{
		X509CRL crl = null;
		
		CertificateFactory cf = CertificateFactory.getInstance("X509");
		crl = (X509CRL)cf.generateCRL(is);
		
		return crl;
	}
	
	/**
	 * returns the CRL
	 * @return Objeto X509CRL
	 * @see java.security.cert.X509CRL
	 */
	public X509CRL getCRL(){
		return crl;
	}
	

}
