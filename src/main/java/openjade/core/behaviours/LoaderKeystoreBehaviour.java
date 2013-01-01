package openjade.core.behaviours;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import openjade.core.OpenAgent;
import openjade.ontology.Certificate;
import openjade.ontology.SendCertificate;

import org.apache.log4j.Logger;

public class LoaderKeystoreBehaviour extends Behaviour {

	private static final long serialVersionUID = 1L;

	private OpenAgent myAgent;

	private boolean done = false;

	protected static Logger log = Logger.getLogger(LoaderKeystoreBehaviour.class);

	public LoaderKeystoreBehaviour(Agent agent) {
		super(agent);
		myAgent = (OpenAgent) agent;
	}

	@Override
	public void action() {
		try {
			myAgent.loadKeystore();
			if (myAgent.getCms() != null) {
				myAgent.setCms(myAgent.getCms());
				ACLMessage message = new ACLMessage(ACLMessage.INFORM);
				message.setSender(myAgent.getAID());
				message.addReceiver(myAgent.getCms());

				SendCertificate sc = new SendCertificate();
				Certificate c = new Certificate();
				c.setAlgorithm(myAgent.getCertificate().getPublicKey().getAlgorithm());
				c.setContent(myAgent.getCertificate().getPublicKey().getEncoded());
				sc.setAid(myAgent.getAID());
				sc.setCertificate(c);
				myAgent.fillContent(message, sc, myAgent.getCodec(), myAgent.getOpenJadeOntology());
				myAgent.signerAndSend(message);
				done = true;
			}
		} catch (Exception e) {
			throw new BehaviourException(e.getMessage(), e);
		}
	}


	@Override
	public boolean done() {
		return done;
	}
}
