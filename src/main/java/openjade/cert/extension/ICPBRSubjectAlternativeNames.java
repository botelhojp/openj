package openjade.cert.extension;

import java.security.cert.X509Certificate;

/**
 * 

*/

public class ICPBRSubjectAlternativeNames {
	
	private String                   	   email = null;
	private ICPBRCertificatePF             icpBrCertPF = null;
	private ICPBRCertificatePJ             icpBrCertPJ = null;
	private ICPBRCertificateEquipment      icpBrCertEquipment = null;
	
	/**
	 * 
	 * @param certificate -> X509Certificate
	 * @see java.security.cert.X509Certificate
	 */
	public ICPBRSubjectAlternativeNames(X509Certificate certificate) {
		CertificateExtra ce = new CertificateExtra(certificate);
		if(ce.isCertificatePF()) {
			icpBrCertPF = new ICPBRCertificatePF(ce.getOID_2_16_76_1_3_1(),
					ce.getOID_2_16_76_1_3_5(),
					ce.getOID_2_16_76_1_3_6(), ce.getOID_0_16_76_1_3_1());
		} else if(ce.isCertificatePJ()) {
			icpBrCertPJ = new ICPBRCertificatePJ(ce.getOID_2_16_76_1_3_2(),
					ce.getOID_2_16_76_1_3_3(),
					ce.getOID_2_16_76_1_3_4(),
					ce.getOID_2_16_76_1_3_7());
		} else if(ce.isCertificateEquipment()) {
			icpBrCertEquipment = new ICPBRCertificateEquipment(ce.getOID_2_16_76_1_3_2(),
					ce.getOID_2_16_76_1_3_3(),
					ce.getOID_2_16_76_1_3_4(),
					ce.getOID_2_16_76_1_3_8());
		}
		this.email = ce.getEmail();
	}
	
	/**
	 * 
	 * @return boolean
	 */
	public boolean isCertificatePF(){
		return icpBrCertPF != null;
	}
	
	/**
	 * 
	 * @return Object ICPBRCertificatePF
	 * @see br.gov.serpro.security.certificate.extension.ICPBRCertificatePF
	 */
	public ICPBRCertificatePF getICPBRCertificatePF(){
		return icpBrCertPF;
	}
	/**
	 * 
	 * @return boolean
	 */
	public boolean isCertificatePJ(){
		return icpBrCertPJ != null;
	}
	
	/**
	 * 
	 * @return Object ICPBRCertificatePJ
	 * @see br.gov.serpro.security.certificate.extension.ICPBRCertificatePJ
	 */
	public ICPBRCertificatePJ getICPBRCertificatePJ(){
		return icpBrCertPJ;
	}
	
	/**
	 * 
	 * @return boolean
	 */
	public boolean isCertificateEquipment(){
		return icpBrCertEquipment != null;
	}
	
	/**
	 * 
	 * @return Object ICPBRCertificateEquipment
	 * @see br.gov.serpro.security.certificate.extension.ICPBRCertificateEquipment
	 */
	public ICPBRCertificateEquipment getICPBRCertificateEquipment(){
		return icpBrCertEquipment;
	}
	
	/**
	 * 
	 * @return String 
	 */
	public String getEmail(){
		return email;
	}

}
