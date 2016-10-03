package br.pucpr.ppgia.redo;

import java.io.InputStream;

import jade.content.lang.Codec;
import openjade.core.OpenAgent;
import openjade.core.SignerAgent;

//public class GenericAgent extends OpenAgent {
public class GenericAgent extends OpenAgent implements SignerAgent {

	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		super.setup();
		log.debug("setup: " + getAID().getLocalName());
		registerService(getAID().getLocalName());
		//this.moveContainer("Hi-Container");
		log.debug("setup: end");
	}

	public InputStream getKeystore() {
		return this.getClass().getResourceAsStream("/" + this.getLocalName() + ".pfx");
	}

	public String getKeystorePassword() {
		return "123456";
	}

	@Override
	public void setCodec(Codec codec) {
		super.setCodec(codec);
		//super.setCodec(new SimpleSLCodec());
	}

}
