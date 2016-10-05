package br.pucpr.ppgia.redo;

import java.io.InputStream;
import java.util.Base64;

import jade.content.lang.Codec;
import jade.content.lang.sl.SimpleSLCodec;
import openjade.composite.DossierModel;
import openjade.core.OpenAgent;
import openjade.core.SignerAgent;
import openjade.ontology.Dossier;
import openjade.ontology.Rating;
import openjade.ontology.SendDossier;

//public class GenericAgent extends OpenAgent {
public class GenericAgent extends OpenAgent implements SignerAgent {

	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		super.setup();
		log.debug("setup: " + getAID().getLocalName());
		registerService(getAID().getLocalName());
		//this.moveContainer("Hi-Container");
	}

	public InputStream getKeystore() {
		return this.getClass().getResourceAsStream("/" + this.getLocalName() + ".pfx");
	}

	public String getKeystorePassword() {
		return "123456";
	}

	@Override
	public void setCodec(Codec codec) {
		//super.setCodec(codec);
		super.setCodec(new SimpleSLCodec());
	}
}
