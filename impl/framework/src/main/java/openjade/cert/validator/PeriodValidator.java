package openjade.cert.validator;

import java.security.cert.X509Certificate;

import openjade.cert.CertificateValidatorException;
import openjade.cert.IValidator;


public class PeriodValidator implements IValidator {

	public void validate(X509Certificate x509) throws CertificateValidatorException {
		try {
			x509.checkValidity();
		} catch (Exception e) {
			throw new CertificateValidatorException("Certificate not valid", e);
		}
	}

}
