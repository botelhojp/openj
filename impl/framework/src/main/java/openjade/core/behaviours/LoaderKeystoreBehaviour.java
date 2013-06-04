package openjade.core.behaviours;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import openjade.core.OpenAgent;

import org.apache.log4j.Logger;

public class LoaderKeystoreBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private OpenAgent myAgent;

	protected static Logger log = Logger.getLogger(LoaderKeystoreBehaviour.class);

	public LoaderKeystoreBehaviour(Agent agent) {
		super(agent);
		myAgent = (OpenAgent) agent;
	}

	@Override
	public void action() {
		try {
			myAgent.loadKeystore();			
			myAgent.removeBehaviour(this);
		} catch (Exception e) {
			throw new BehaviourException(e.getMessage(), e);
		}
	}
}
