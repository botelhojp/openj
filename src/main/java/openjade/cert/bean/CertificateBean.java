package openjade.cert.bean;

import java.util.List;

import openjade.cert.extension.DefaultExtension;
import openjade.cert.extension.DefaultExtensionType;
import openjade.cert.extension.ICPAgentExtension;
import openjade.cert.extension.ICPBRExtension;
import openjade.cert.extension.ICPBRExtensionType;
import openjade.cert.extension.ICPAgentExtensionType;


public class CertificateBean {

	@ICPAgentExtension(type = ICPAgentExtensionType.ID)
	private String agentID;
	
	@ICPBRExtension(type = ICPBRExtensionType.PERSON_ID)
	private String personID;

	@ICPBRExtension(type = ICPBRExtensionType.PERSON_NAME)
	private String personName;

	@ICPBRExtension(type = ICPBRExtensionType.PERSON_EMAIL)
	private String personEmail;
	
	@DefaultExtension(type = DefaultExtensionType.CRL_URL)
	private List<String> crlURL;

	public CertificateBean() {
	}

	public String getPersonID() {
		validSize(personID, 11);
		return personID;
	}

	public void setPersonID(String _personID) {
		validSize(_personID, 11);
		this.personID = _personID;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String _personName) {
		this.personName = _personName;
	}

	public List<String> getCrlURL() {
		return crlURL;
	}

	public void setCrlURL(List<String> crlURL) {
		this.crlURL = crlURL;
	}

	public String getEmail() {
		return personEmail;
	}

	public void setEmail(String email) {
		this.personEmail = email;
	}


	public String getAgentID() {
		return agentID;
	}

	public void setAgentID(String agentID) {
		this.agentID = agentID;
	}

	@Override
	public String toString() {
		StringBuffer r = new StringBuffer();
		r.append("Agent Id:").append("[").append(agentID).append("]\n");
		
		r.append("Person Name:").append("[").append(personName).append("]\n");
		r.append("Person ID:").append("[").append(personID).append("]\n");
		r.append("Person Email:").append("[").append(personEmail).append("]\n");
		
		r.append("CRL_URL:").append("[").append(crlURL).append("]\n");
		
		return r.toString();
	}

	private void validSize(String value, int tam) {
//		if (value == null || value.length() != tam) {
//			throw new RuntimeException("field [" + value + "] size should be equal to [" + tam + "] characters");
//		}
	}
}
