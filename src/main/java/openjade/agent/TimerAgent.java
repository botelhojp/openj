package openjade.agent;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.io.InputStream;

import openjade.core.OpenAgent;
import openjade.core.behaviours.BehaviourException;
import openjade.core.behaviours.CyclicTimerBehaviour;
import openjade.ontology.OpenJadeOntology;
import openjade.ontology.SendIteration;

import org.apache.log4j.Logger;

public class TimerAgent extends OpenAgent {

	protected static Logger log = Logger.getLogger(TimerAgent.class);

	private static final long serialVersionUID = 1L;

	private String keystore;

	private String keystorePassword;

	protected void setup() {
		keystore = "/certs/agent_timer_001.pfx";
		keystorePassword = "123456";
		// keystore = (String) getArguments()[0];
		// keystorePassword = (String) getArguments()[1];
		moveContainer(OpenAgent.MAIN_CONTAINER);
		log.debug("setup: " + getAID().getLocalName());
		addBehaviour(new CyclicTimerBehaviour(this, 5000) {

			private static final long serialVersionUID = 7518048669094143009L;
			private int time = 0;

			@Override
			public void run() {
				OpenAgent agent = (OpenAgent) myAgent;
				time++;
				log.debug(".............. time [" + time + "] ..............");
				ACLMessage message = new ACLMessage(ACLMessage.INFORM);
				try {
					DFAgentDescription dfd = new DFAgentDescription();
					ServiceDescription sd = new ServiceDescription();
					sd.setType(OpenAgent.LISTENER_TIMER);
					dfd.addServices(sd);
					DFAgentDescription[] results = DFService.search(myAgent, dfd);
					for (DFAgentDescription result : results) {
						message.addReceiver(result.getName());
					}
				} catch (FIPAException e) {
					throw new BehaviourException(e.getMessage(), e);
				}
				message.setSender(myAgent.getAID());
				SendIteration action = new SendIteration();
				action.setIteration(time);
				agent.fillContent(message, action, agent.getCodec(), OpenJadeOntology.getInstance());
				agent.signerAndSend(message);
			}
		});
	}

	@Override
	protected InputStream getKeystore() {
		return TimerAgent.class.getResourceAsStream(keystore);
	}

	@Override
	protected String getKeystorePassword() {
		return keystorePassword;
	}
}
