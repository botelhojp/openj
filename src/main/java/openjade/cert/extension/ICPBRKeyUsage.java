package openjade.cert.extension;

import java.security.cert.X509Certificate;

/**
 * 

 * 
 */
public class ICPBRKeyUsage {
	
	private static final String[] KEY_USAGE = {
	     "digitalSignature",
	     "nonRepudiation",
	     "keyEncipherment",
	     "dataEncipherment",
	     "keyAgreement",
	     "keyCertSign",
	     "cRLSign",
	     "encipherOnly",
	     "decipherOnly"	};
	
	private boolean[] keyUsage;
	
	/**
	 * 
	 * @param cert X509Certificate
	 */
	public ICPBRKeyUsage(X509Certificate cert){
		this.keyUsage = cert.getKeyUsage();
	}
	
	public boolean isDigitalSignature(){
		return keyUsage[0];
	}
	
	public boolean isNonRepudiation() {
		return keyUsage[1];
	}

	public boolean isKeyEncipherment() {
		return keyUsage[2];
	}

	public boolean isDataEncipherment() {
		return keyUsage[3];
	}

	public boolean isKeyAgreement() {
		return keyUsage[4];
	}

	public boolean isKeyCertSign() {
		return keyUsage[5];
	}

	public boolean isCRLSign() {
		return keyUsage[6];
	}

	public boolean isEncipherOnly() {
		return keyUsage[7];
	}

	public boolean isDecipherOnly() {
		return keyUsage[8];
	}

	public String toString(){
		String ret = "";
		for(int i=0 ; i<keyUsage.length ; i++) {
			if(keyUsage[i]){
				if(ret.length() > 0) {
					ret += ", ";
				}
				ret += KEY_USAGE[i];
			}
		}
		return ret;
	}
}
