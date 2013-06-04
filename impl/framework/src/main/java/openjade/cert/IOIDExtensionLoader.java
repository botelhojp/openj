package openjade.cert;

import java.lang.reflect.Field;
import java.security.cert.X509Certificate;

public interface IOIDExtensionLoader {
	
	public void load(Object object, Field field, X509Certificate x509);

}
