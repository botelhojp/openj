package br.pucpr.ppgia.redo;

import org.apache.log4j.Logger;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import openjade.composite.DossierModel;
import openjade.core.annotation.ReceiveMatchMessage;
import openjade.core.annotation.ReceiveSimpleMessage;
import openjade.ontology.MerkleTree;
import openjade.ontology.PKCS7Message;
import openjade.ontology.Rating;
import openjade.ontology.SendDossier;
import openjade.ontology.SendRating;
import openjade.ontology.Sign;

/**
 * Server
 * 
 * @author vander
 */
public class Agent_00001 extends GenericAgent {

	private static final long serialVersionUID = 1L;

	protected static Logger log = Logger.getLogger(Agent_00001.class);

	private DossierModel dossier;

	public Agent_00001() {
		dossier = new DossierModel();
		List l = new ArrayList();
		dossier.setRatings(l);
		MerkleTree tree = new MerkleTree();
		dossier.setTree(tree);
	}

	// Retorna dossie
	@ReceiveSimpleMessage(conversationId = Conversation.DOSSIER, performative = { ACLMessage.REQUEST })
	public void responseDossier(ACLMessage message) {
		log.debug("------------ response dossier ------------");
		SendDossier sendDossier = new SendDossier();
		sendDossier.setDossier(dossier.getModel());
		PKCS7Message pkcs7Message = new PKCS7Message();
		String signature = super.arrayToString(this.signer.signPkcs7(super.md5(dossier.toString()).getBytes()));
		pkcs7Message.setContent(signature);
		Sign signMessage = new Sign();
		signMessage.setPkcs7(pkcs7Message);
		sendDossier.setSignature(pkcs7Message);
		sendMessage(new AID("Agent_00002", false), ACLMessage.INFORM, Conversation.DOSSIER, sendDossier);
	}

	// Retorna serviço
	@ReceiveSimpleMessage(conversationId = Conversation.SERVICE, performative = { ACLMessage.REQUEST })
	public void responseService(ACLMessage message) {
		log.debug("------------ response service ------------");
		sendMessage(message.getSender(), ACLMessage.INFORM, Conversation.SERVICE, "my service");
	}

	// Recebe Feedback
	@ReceiveMatchMessage(conversationId = { Conversation.FEEDBACK }, action = SendRating.class, performative = {
			ACLMessage.INFORM })
	public void receiveByelMessage(ACLMessage _message, SendRating sr) {
		log.debug("------------ receive feedback ------------");
		dossier.insert((Rating) sr.getRatings().get(0));
		log.debug("dossier size: " + dossier.getRatings().size());

	}
}
