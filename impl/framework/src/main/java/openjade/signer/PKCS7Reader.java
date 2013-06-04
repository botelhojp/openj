package openjade.signer;

import java.io.IOException;
import java.security.cert.X509Certificate;

import openjade.cert.CertificateManager;
import openjade.cert.bean.CertificateBean;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.ParsingException;

public class PKCS7Reader {
	
	private X509Certificate certificate;
	private CertificateBean certBean;
	private String data;
	

	public PKCS7Reader(byte[] signedBytes) {
		load(signedBytes);
	}

	private void load(byte[] signedBytes) {
		try {
			PKCS7 pkcs7 = new PKCS7(signedBytes);
			certificate = pkcs7.getCertificates()[0];
			CertificateManager cm = new CertificateManager(certificate, false);
			certBean = cm.load(CertificateBean.class);
			this.data = new String(pkcs7.getContentInfo().getData(), "UTF-8");
		} catch (ParsingException e) {
			throw new SignerException(e);
		} catch (IOException e) {
			throw new SignerException(e);
		}
	}

	public CertificateBean getCertificateBean() {
		return certBean;
	}

	public String getDataToString() {
		return data;
	}
	
	public X509Certificate getX509Certificate(){
		return certificate;
	}

}
