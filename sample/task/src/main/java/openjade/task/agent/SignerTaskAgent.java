package openjade.task.agent;

import java.io.InputStream;

import openjade.core.SignerAgent;

public class SignerTaskAgent extends TaskAgent implements SignerAgent {

	private static final long serialVersionUID = 1L;

	public InputStream getKeystore() {
		return TaskAgent.class.getResourceAsStream(keystore);
	}

	public String getKeystorePassword() {
		return keystorePassword;
	}
}
