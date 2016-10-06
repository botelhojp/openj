package br.pucpr.ppgia.redo;

import org.apache.log4j.Logger;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import openjade.core.annotation.ReceiveMatchMessage;
import openjade.core.annotation.ReceiveSimpleMessage;
import openjade.core.behaviours.SenderByServiceBehaviour;
import openjade.ontology.Rating;
import openjade.ontology.SendDossier;
import openjade.ontology.SendRating;

/**
 * Client
 * 
 * @author vander
 */
public class Agent_00002 extends GenericAgent {

	private static final long serialVersionUID = 1L;

	protected static Logger log = Logger.getLogger(Agent_00002.class);

	private int iteration = 0;

	protected void setup() {
		requestDossie();
	}

	// Solicita Dossier
	private void requestDossie() {
		if (iteration++ < 10) {
			log.debug("------------ request dossier (" + iteration + ") ------------");		
			ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
			message.setSender(getAID());
			message.setConversationId(Conversation.DOSSIER);
			message.addReplyTo(getAID());
			message.setContent("get me your dossier");
			addBehaviour(new SenderByServiceBehaviour(this, "agent_00001", message, true));
		}
	}

	// Solicita servico
	@ReceiveMatchMessage(conversationId = { Conversation.DOSSIER }, action = SendDossier.class, performative = {ACLMessage.INFORM })
	public void receiveByelMessage(ACLMessage _message, SendDossier sr) {
		log.debug("------------ request service ------------");
		if (super.verifySign(_message.getSender().getLocalName(), sr)){
			sendMessage(new AID("agent_00001", false), ACLMessage.REQUEST, Conversation.SERVICE, "get me your service");
		}else{
			log.error("assinatura nao validada");
		}
	}
	
	//Recebe servico e envia feedback
	@ReceiveSimpleMessage(conversationId=Conversation.SERVICE, performative={ACLMessage.INFORM})
	public void receiveService(ACLMessage message) {
		log.debug("------------ send feedback ------------");
		SendRating sd = new SendRating();
		Rating rating = new Rating();
		rating.setClient(getAID());
		rating.setServer(message.getSender());
		rating.setRound(iteration);
		rating.setValue("great");
		sd.addRatings(rating );
		sendMessage(message.getSender(), ACLMessage.INFORM, Conversation.FEEDBACK, sd);
		requestDossie();
	}

}
