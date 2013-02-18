package openjade.core.behaviours;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class RegisterServiceBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = -1149631629503495214L;

	private String[] services;

	public RegisterServiceBehaviour(Agent _agent, String service) {
		super(_agent);
		this.services = new String[1];
		this.services[0] = service;
	}

	public RegisterServiceBehaviour(Agent _agent, String[] service) {
		super(_agent);
		this.services = service;
	}

	@Override
	public void action() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(myAgent.getAID());
		for (String service : services) {
			ServiceDescription sd = new ServiceDescription();
			sd.setType(service);
			sd.setName(myAgent.getLocalName());
			dfd.addServices(sd);
		}
		try {
			DFService.register(myAgent, dfd);
		} catch (FIPAException e) {
			throw new BehaviourException(e.getMessage(), e);
		}
	}
}
