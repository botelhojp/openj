package openjade.core.behaviours;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import openjade.core.OpenAgent;

public class SenderByServiceBehaviour extends Behaviour {

	private static final long serialVersionUID = 1L;

	boolean done = false;
	private String serviceType;
	private ACLMessage message;
	private OpenAgent myAgent;

	private boolean signer = false;

	public SenderByServiceBehaviour(Agent _agent, String _serviceType, ACLMessage _message) {
		this(_agent, _serviceType, _message, false);
	}
	
	public SenderByServiceBehaviour(Agent _agent, String _serviceType, ACLMessage _message, boolean _signer) {
		super(_agent);
		myAgent = (OpenAgent) _agent;
		serviceType = _serviceType;
		message = _message;
		signer = _signer;
	}

	@Override
	public void action() {
		try {
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType(serviceType);
			dfd.addServices(sd);
			DFAgentDescription[] result = DFService.search(this.myAgent, dfd);
			if (result != null && result.length > 0) {
				int index = (int) (Math.random() * result.length);
				message.addReceiver(result[index].getName());
				if (signer){
					myAgent.sendMessage(message);	
				}else{
					myAgent.send(message);
				}
				done = true;
			}
		} catch (FIPAException e) {
			throw new BehaviourException(e.getMessage(), e);
		}
	}

	@Override
	public boolean done() {
		return done;
	}
}
