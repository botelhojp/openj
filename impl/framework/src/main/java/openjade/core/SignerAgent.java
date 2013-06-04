package openjade.core;

import java.io.InputStream;

public interface SignerAgent {
	
	public InputStream getKeystore();
	
	public String getKeystorePassword();

}
