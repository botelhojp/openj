package openjade.cert.extension;

import java.lang.reflect.Field;
import java.security.cert.X509Certificate;

import openjade.cert.CertificateException;
import openjade.cert.IOIDExtensionLoader;

public class ICPBRExtensionLoader implements IOIDExtensionLoader {

	public void load(Object object, Field field, X509Certificate x509) {
		Object value = null;
		if (field.isAnnotationPresent(ICPAgentExtension.class)) {
			value = loadICPAgentExtension(object, field, x509);			
		}
		if (field.isAnnotationPresent(ICPBRExtension.class)) {
			value = loadICPBrasilExtension(object, field, x509);
		}
		try {
			field.setAccessible(true);
			field.set(object, value);
		} catch (Exception e) {
			throw new CertificateException("Error on load value in field "+field.getName(), e); 
		}

	}

	private String loadICPBrasilExtension(Object object, Field field, X509Certificate x509) {
		ICPBRExtension annotation = field.getAnnotation(ICPBRExtension.class);
		
		try {
		
			BasicCertificate cert = new BasicCertificate(x509);
		
			switch (annotation.type()) {
			case PERSON_ID:
				if (cert.hasCertificatePF()) {
					return cert.getICPBRCertificatePF().getCPF();
				};
				break;
			case CORPORATION_ID:
				if (cert.hasCertificatePJ()) {
					return cert.getICPBRCertificatePJ().getCNPJ();
				}else{
					if (cert.hasCertificateEquipment()){
						return cert.getICPBRCertificateEquipment().getCNPJ();
					}					
				}
				break;
			case PERSON_NAME:
				return cert.getNome();
			case PERSON_EMAIL:
				return cert.getEmail();
			case CORPORATION_NAME:					
				if (cert.hasCertificateEquipment()) {
					return cert.getICPBRCertificateEquipment().getNomeEmpresarial();
				}					
				break;
			case CERTIFICATE_TYPE:					
				if (cert.hasCertificatePF()) {
					return "PF";
				}else{ 
					if(cert.hasCertificatePJ()){
						return "PJ";
					}else{
						if(cert.hasCertificateEquipment()){
							return "EA";
						}
					}
				}
				break;
			case CERTIFICATE_LEVEL:					
				return cert.getNivelCertificado();
			default:
				throw new CertificateException(annotation.type() + " Not Implemented");
			}
			return "";
		}catch (Exception e) {
			throw new CertificateException("Error trying get Keyvalue "+annotation.type(), e); 
		}
	}

	private String loadICPAgentExtension(Object object, Field field, X509Certificate x509) {
		ICPAgentExtension annotation = field.getAnnotation(ICPAgentExtension.class);
		try {
			BasicCertificate cert = new BasicCertificate(x509);
			switch (annotation.type()) {
			case ID:
				if (cert.hasAgentID()) {
					return cert.getICPBRCertificatePF().getAgentID();
				}
			}
			return "";
		} catch (Exception e) {
			throw new CertificateException("Error on load value in field " + field.getName(), e);
		}
	}
}
