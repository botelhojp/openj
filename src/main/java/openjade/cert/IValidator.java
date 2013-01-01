package openjade.cert;

import java.security.cert.X509Certificate;

public interface IValidator {
	
	public void validate(X509Certificate x509) throws CertificateValidatorException;

}
