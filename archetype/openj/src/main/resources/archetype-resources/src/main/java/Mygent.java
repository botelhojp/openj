package ${packageName};

import jade.lang.acl.ACLMessage;
import openjade.core.OpenAgent;
import openjade.core.annotation.ReceiveSimpleMessage;
import openjade.core.behaviours.SenderByServiceBehaviour;

import org.apache.log4j.Logger;

public class Mygent extends OpenAgent {

	private static final long serialVersionUID = 1L;

	protected static Logger log = Logger.getLogger(Mygent.class);

	protected void setup() {
		log.debug("setup: " + getAID().getLocalName());
		registerService(getAID().getLocalName());
		this.moveContainer("Hi-Container");
		if (getAID().getLocalName().equals("agent2")) {
			ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
			message.setSender(getAID());
			message.setConversationId("HI");
			message.addReplyTo(getAID());
			message.setContent("Hi agent1");
			addBehaviour(new SenderByServiceBehaviour(this, "agent1", message, true));
		}
	}


	@ReceiveSimpleMessage(conversationId = "HI", performative = { ACLMessage.PROPOSE })
	public void receiveMessage(ACLMessage message) {
		log.debug("received " + message.getSender().getLocalName() + ": \"" + message.getContent() + "\"");
		ACLMessage reply = new ACLMessage(randomPerformative());
		reply.addReceiver(message.getSender());
		reply.setSender(getAID());
		reply.setConversationId("HI");
		reply.setContent(((reply.getPerformative() == ACLMessage.REFUSE) ? "Bye " : "Hi ") + message.getSender().getLocalName());
		sendMessage(reply);
	}

	@ReceiveSimpleMessage(conversationId = "HI", performative = { ACLMessage.REFUSE })
	public void receiveHiMessage(ACLMessage message) {
		log.debug("received " + message.getSender().getLocalName() + ": \"" + message.getContent() + "\"");
		ACLMessage reply = new ACLMessage(ACLMessage.REFUSE);
		reply.addReceiver(message.getSender());
		reply.setSender(getAID());
		reply.setConversationId("BYE");
		reply.setContent("Bye Bye " + message.getSender().getLocalName());
		sendMessage(reply);
	}

	@ReceiveSimpleMessage(conversationId = "BYE", performative = { ACLMessage.REFUSE })
	public void receiveByelMessage(ACLMessage message) {
		log.debug("received " + message.getSender().getLocalName() + ": \"" + message.getContent() + "\"");
	}

	private int randomPerformative() {
		int[] options = { ACLMessage.PROPOSE, ACLMessage.PROPOSE, ACLMessage.PROPOSE, ACLMessage.PROPOSE, ACLMessage.PROPOSE, ACLMessage.PROPOSE, ACLMessage.PROPOSE, ACLMessage.PROPOSE, ACLMessage.REFUSE };
		return options[(int) (Math.random() * options.length)];
	}
}
