package openjade.cert.extension;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.cert.X509Certificate;

import openjade.cert.CertificateException;
import openjade.cert.IOIDExtensionLoader;


public class DefaultExtensionLoader implements IOIDExtensionLoader {

	public void load(Object object, Field field, X509Certificate x509) {
		if (field.isAnnotationPresent(DefaultExtension.class)) {
			DefaultExtension annotation = field.getAnnotation(DefaultExtension.class);
			
			Object keyValue;
			
			BasicCertificate cert = new BasicCertificate(x509);
			
			switch (annotation.type()) {
			case CRL_URL:
				try {
						keyValue = cert.getCRLDistributionPoint();
					} catch (IOException e1) {
						throw new CertificateException("Error on get value to field "+field.getName(), e1);
					}
				break;
			case SERIAL_NUMBER:
				keyValue = cert.getSerialNumber();
				break;				
			case ISSUER_DN:
				try {
						keyValue = cert.getCertificateIssuerDN().toString();
					} catch (IOException e1) {
						throw new CertificateException("Error on get value to field "+field.getName(), e1);
					}
				break;
			case SUBJECT_DN:
				try {
						keyValue = cert.getCertificateSubjectDN().toString();
					} catch (IOException e1) {
						throw new CertificateException("Error on get value to field "+field.getName(), e1);
					}
				break;				
			case KEY_USAGE:
				keyValue = cert.getICPBRKeyUsage().toString();
				break;
			case PATH_LENGTH:
				keyValue = cert.getPathLength();
				break;
			case AUTHORITY_KEY_IDENTIFIER:
				try {
						keyValue = cert.getAuthorityKeyIdentifier();
					} catch (IOException e1) {
						throw new CertificateException("Error on get value to field "+field.getName(), e1);
					}
				break;
				
			case SUBJECT_KEY_IDENTIFIER:
				try {
						keyValue = cert.getSubjectKeyIdentifier();
					} catch (IOException e1) {
						throw new CertificateException("Error on get value to field "+field.getName(), e1);
					}
				break;
				
			case BEFORE_DATE:
				keyValue = cert.getBeforeDate();
				break;
			case AFTER_DATE:
				keyValue = cert.getAfterDate();
				break;				
			case CERTIFICATION_AUTHORITY:
				keyValue = cert.isCertificadoAc();
				break;
				
			default:
				throw new CertificateException(annotation.type() + " Not Implemented");
			}
			
			try {
				field.setAccessible(true);
				field.set(object, keyValue);
			} catch (Exception e) {
				throw new CertificateException("Error on load value in field "+field.getName(), e); 
			}
		}
	}

}
