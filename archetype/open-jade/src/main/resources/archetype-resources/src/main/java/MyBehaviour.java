package ${packageName};

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import openjade.core.OpenAgent;
import openjade.core.behaviours.SenderByServiceBehaviour;

import org.apache.log4j.Logger;

public class MyBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;

	public static final String SIGNED = "SIGNED";
	public static final String CIPHER_PRIVATE_KEY = "CIPHER_PRIVATE_KEY";
	public static final String CIPHER_PUBLIC_KEY = "CIPHER_PUBLIC_KEY";

	private OpenAgent myAgent;

	private String mode;

	protected static Logger log = Logger.getLogger(MyBehaviour.class);

	public MyBehaviour(Agent agent, String mode) {
		super(agent);
		myAgent = (OpenAgent) agent;
		this.mode = mode;
		startConversation();
	}
	
	private void startConversation() {
		if (myAgent.getAID().getLocalName().equals("agent2")) {
			log.info("====== startConversation ======");
			ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
			message.setSender(myAgent.getAID());
			message.addReplyTo(myAgent.getAID());
			message.setContent("Hi!");
			myAgent.addBehaviour(new SenderByServiceBehaviour(myAgent, "agent1", message, true));
		}
	}

	@Override
	public void action() {
		sleep(300);
		MessageTemplate mt = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE), MessageTemplate.MatchPerformative(ACLMessage.CANCEL));
		ACLMessage message = myAgent.receiveMessage(mt);

		if (message != null) {
			log.debug("received " + mode + " by " + message.getSender().getLocalName() + ": \"" + message.getContent() + "\"");
			switch (message.getPerformative()) {
			case ACLMessage.CANCEL:
				log.info( mode + " [bye " + message.getSender().getLocalName() + "]");
				myAgent.removeBehaviour(this);
				break;
			default:
				sendMessage(message);
			}
		}
	}



	public void sendMessage(ACLMessage message) {
		int[] options = { ACLMessage.PROPOSE, ACLMessage.PROPOSE, ACLMessage.PROPOSE, ACLMessage.PROPOSE, ACLMessage.PROPOSE, ACLMessage.CANCEL, ACLMessage.PROPOSE, ACLMessage.PROPOSE, ACLMessage.PROPOSE, ACLMessage.PROPOSE };
		int option = (int) (Math.random() * options.length);
		ACLMessage replyMessage = new ACLMessage(options[option]);		
		replyMessage.setSender(myAgent.getAID());		
		if ( replyMessage.getPerformative() == ACLMessage.CANCEL ){
			replyMessage.setContent("[bye " + message.getSender().getLocalName() + "]");
			myAgent.removeBehaviour(this);			
		}else{
			replyMessage.setContent("Hi " + message.getSender().getLocalName() + "!");	
		}		
		replyMessage.addReceiver(message.getSender());

		if (mode.equals(SIGNED)) {
			myAgent.sendMessage(replyMessage);
		}
		if (mode.equals(CIPHER_PRIVATE_KEY)) {
			myAgent.cipherMyKeyAndSend(replyMessage);
		}
		if (mode.equals(CIPHER_PUBLIC_KEY)) {
			myAgent.cipherSenderKeyAndSend(replyMessage);
		}
	}

	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String getMode() {
		return mode;
	}	
	
}