package openjade.agent;

import jade.content.ContentElement;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.Hashtable;

import openjade.core.OpenAgent;
import openjade.core.behaviours.BehaviourException;
import openjade.ontology.Certificate;
import openjade.ontology.FindCertificate;
import openjade.ontology.SendCertificate;

public class CMS extends OpenAgent {

	private static final long serialVersionUID = 1L;

	public static final String SERVICE_TYPE = "CMS_SERVICE_TYPE";

	private String keystore;

	private String keystorePassword;

	private Hashtable<AID, Key> keys;

	protected void setup() {
		registerService(CMS.SERVICE_TYPE);
		keystore = "/certs/cms.pfx";
		keystorePassword = "cms123456";
		keys = new Hashtable<AID, Key>();
		super.moveContainer(OpenAgent.MAIN_CONTAINER);
		log.debug("setup: " + getAID().getLocalName());
		addBehaviour(new ReceiveCertificate(this));
	}

	@Override
	protected InputStream getKeystore() {
		return CMS.class.getResourceAsStream(keystore);
	}

	@Override
	protected String getKeystorePassword() {
		return keystorePassword;
	}

	private class ReceiveCertificate extends CyclicBehaviour {

		private static final long serialVersionUID = 1L;

		private OpenAgent myAgent;

		public ReceiveCertificate(Agent _agent) {
			myAgent = (OpenAgent) _agent;
		}

		@Override
		public void action() {
			ACLMessage message = receiveMessage();
			if (message != null && openJadeMT.match(message)) {
				ContentElement ce = extractContent(message, codec, openJadeOntology);
				if (ce instanceof SendCertificate) {
					SendCertificate sc = (SendCertificate) ce;
					byte[] k = (byte[]) sc.getCertificate().getContent();
					X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(k);
					X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(x509EncodedKeySpec.getEncoded());
					try {
						KeyFactory keyFactory = KeyFactory.getInstance(sc.getCertificate().getAlgorithm());
						Key key = keyFactory.generatePublic(publicKeySpec);
						keys.put(sc.getAid(), key);
					} catch (Exception e) {
						throw new BehaviourException(e.getMessage(), e);
					}
				}
				if (ce instanceof FindCertificate) {
					FindCertificate fc = (FindCertificate) ce;
					Key key = keys.get(fc.getAid());
					if (key != null){
						ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
						reply.setSender(myAgent.getAID());
						reply.addReceiver(message.getSender());

						SendCertificate sc = new SendCertificate();
						
						Certificate c = new Certificate();
						c.setAlgorithm(key.getAlgorithm());
						c.setContent(key.getEncoded());
						
						sc.setAid(fc.getAid());
						sc.setCertificate(c);
						
						myAgent.fillContent(reply, sc, myAgent.getCodec(), myAgent.getOpenJadeOntology());
						myAgent.signerAndSend(reply);
					}
				}
			}
		}
	}

}
