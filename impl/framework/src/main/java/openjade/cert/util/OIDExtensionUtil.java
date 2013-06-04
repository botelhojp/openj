package openjade.cert.util;

import java.io.UnsupportedEncodingException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

public class OIDExtensionUtil {
	
	private X509Certificate x509;
	private Map<String, String> oid_2_5_29_17;
	private Map<String, String> oid_2_5_29_31;
	
	public OIDExtensionUtil(X509Certificate x509) {
		this.x509 = x509;
		oid_2_5_29_17 = new HashMap<String, String>();
		oid_2_5_29_31 = new HashMap<String, String>();
		
		process_2_5_29_17();
		process_2_5_29_31();
	}
	
	private void process_2_5_29_17() {
		byte[] extension = x509.getExtensionValue("2.5.29.17");

		int extLength = extension.length;
		int extIndex = 0;
		int thisOIDLen = 0;
		
		if ((extension[6] == -96) || (extension[6] == -127))
			extIndex = 6; 
		else if ((extension[5] == -96) || (extension[5] == -127))
			extIndex = 5; 
		else if ((extension[4] == -96) || (extension[5] == -127))
			extIndex = 4; 
		else {
			return;
		}
		
		while (extIndex < extLength) {
			if (extension[extIndex] == -96) {
				thisOIDLen = (int) extension[extIndex + 1];
				
				String oidThird = Integer.toString((int) extension[extIndex + 5]);
				String oidForth = Integer.toString((int) extension[extIndex + 6]);
				String oidFifth = Integer.toString((int) extension[extIndex + 7]);
				String oidSixth = Integer.toString((int) extension[extIndex + 8]);

				String thisOIDId = new String("2.16" + "." + oidThird + "."	+ oidForth + "." + oidFifth + "." + oidSixth);
				
				int thisOIDStart = 0;
				int thisOIDDataLen = 0;
				if (extension[extIndex + 9] == -96)	{
					thisOIDDataLen = (int) extension[extIndex + 12];
					thisOIDStart = extIndex + 13;
				} else if (extension[extIndex + 9] == -126) {
					thisOIDDataLen = (int) extension[extIndex + 17];
					thisOIDStart = extIndex + 18;
				} else {
					extIndex += thisOIDLen + 2;
					break;
				}
				
				String thisOIDData = "";
				
				try {
					thisOIDData = new String(extension, thisOIDStart, thisOIDDataLen, "ISO8859_1");
				} catch (UnsupportedEncodingException e) {
					throw new CertificateUtilException("Encoding unsupported in oid data", e);
				}
				
				oid_2_5_29_17.put(thisOIDId, thisOIDData);
				extIndex += thisOIDLen + 2;
			} else if (extension[extIndex] == -127) {
				String thisRFCId = new String("RFC822");
				int thisRFCDataLen = (int) extension[extIndex + 1];
				String thisRFCData = "";

				try {
					thisRFCData = new String(extension, extIndex + 2, thisRFCDataLen, "ISO8859_1");
				} catch (UnsupportedEncodingException e) {
					throw new CertificateUtilException("Encoding unsupported in oid data", e);
				}
				
				oid_2_5_29_17.put(thisRFCId, thisRFCData);
				extIndex += thisRFCDataLen + 2;
			} else {
				extIndex = extLength;
			}
		}
	}
	
	private void process_2_5_29_31() {
		byte[] extension = x509.getExtensionValue("2.5.29.31");

		int extIndex = 10;
		
		if (extension[extIndex] == -122) {
			String thisOIDId = new String("2.5.29.31");
			int thisOIDDataLen = (int) extension[extIndex + 1];
			String thisOIDData = "";

			try {
				thisOIDData = new String(extension, extIndex + 2, thisOIDDataLen, "ISO8859_1");
			} catch (UnsupportedEncodingException e) {
				throw new CertificateUtilException("Encoding unsupported in oid data", e);
			}
			
			oid_2_5_29_31.put(thisOIDId, thisOIDData);
		} 
	}
	
	public String getValue(String key) {
		if (key.equals("2.5.29.31")) {
			return oid_2_5_29_31.get(key);
		} else {
			return oid_2_5_29_17.get(key);
		}
	}
	
	public String getValue(String key, int beginIndex) {
		String value = getValue(key);
		if (value != null) {
			return value.substring(beginIndex);
		} else {
			return null;
		}
	}
	
	public String getValue(String key, int beginIndex, int endIndex) {
		String value = getValue(key);
		if (value != null) {
			return value.substring(beginIndex, endIndex);
		} else {
			return null;
		}
	}	

}
