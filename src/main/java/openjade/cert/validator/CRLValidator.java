package openjade.cert.validator;

import java.security.cert.X509Certificate;
import java.util.Collection;

import openjade.cert.CertificateValidatorException;
import openjade.cert.IValidator;
import openjade.cert.extension.ICPBR_CRL;
import openjade.cert.repository.CRLRepository;
import openjade.cert.repository.CRLRepositoryFactory;



public class CRLValidator implements IValidator {

	private CRLRepository crlRepository;

	public CRLValidator() {
		crlRepository = CRLRepositoryFactory.factoryCRLRepository();
	}

	public void validate(X509Certificate x509) throws CertificateValidatorException {
		Collection<ICPBR_CRL> crls = crlRepository.getX509CRL(x509);
		if (crls == null) {
			throw new CertificateValidatorException("Can NOT validate if Certificate is Revoked!, none valid list was found, try Update It");
		}
		for (ICPBR_CRL icpbr_crl : crls) {
			if (icpbr_crl.getCRL().isRevoked(x509)) {
				throw new CertificateValidatorException("Certificate Revoked in CRL");
			}
		}
	}
}