package openjade.core.behaviours;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import openjade.core.OpenAgent;

public class SendMessageBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private OpenAgent agent;
	private ACLMessage message;

	public SendMessageBehaviour(OpenAgent _agent, ACLMessage _message) {
		this.agent = _agent;
		this.message = _message;
	}

	@Override
	public void action() {
		agent.send(message);
	}

}
