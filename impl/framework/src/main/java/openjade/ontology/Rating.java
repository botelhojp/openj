package openjade.ontology;

import jade.core.AID;

/**
 * Protege name: Rating
 * 
 * @author ontology bean generator
 * @version 2014/07/8, 11:38:13
 */
public class Rating extends ASCLMessage {

	private static final long serialVersionUID = 1L;
	/**
	 * Protege name: server
	 */
	private AID server;

	public void setServer(AID value) {
		this.server = value;
	}

	public AID getServer() {
		return this.server;
	}

	/**
	 * Protege name: attributes
	 */
	private String attributes;

	public void setAttributes(String value) {
		this.attributes = value;
	}

	public String getAttributes() {
		return this.attributes;
	}

	/**
	 * Protege name: client
	 */
	private AID client;

	public void setClient(AID value) {
		this.client = value;
	}

	public AID getClient() {
		return this.client;
	}

	/**
	 * valor da iteração Protege name: value
	 */
	private String value;

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

}
