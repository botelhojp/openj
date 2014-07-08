package openjade.ontology;

/**
 * Protege name: Encipher
 * 
 * @author ontology bean generator
 * @version 2014/07/8, 11:38:13
 */
public class Encipher extends MessageAction {
	private static final long serialVersionUID = 1L;
	/**
	 * Protege name: message
	 */
	private EncryptedMessage message;

	public void setMessage(EncryptedMessage value) {
		this.message = value;
	}

	public EncryptedMessage getMessage() {
		return this.message;
	}

	/**
	 * Protege name: provider
	 */
	private String provider;

	public void setProvider(String value) {
		this.provider = value;
	}

	public String getProvider() {
		return this.provider;
	}

	/**
	 * Protege name: signMode
	 */
	private int signMode;

	public void setSignMode(int value) {
		this.signMode = value;
	}

	public int getSignMode() {
		return this.signMode;
	}

	/**
	 * Protege name: algorithm
	 */
	private String algorithm;

	public void setAlgorithm(String value) {
		this.algorithm = value;
	}

	public String getAlgorithm() {
		return this.algorithm;
	}

}
