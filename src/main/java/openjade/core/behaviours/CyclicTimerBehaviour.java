package openjade.core.behaviours;

import jade.core.Agent;

import org.apache.log4j.Logger;

public abstract class CyclicTimerBehaviour extends TimerBehaviour {

	private static final long serialVersionUID = 1L;

	protected static Logger log = Logger.getLogger(CyclicTimerBehaviour.class);

	public CyclicTimerBehaviour(Agent agent, long _timer) {
		super(agent, _timer);
	}
	
	public CyclicTimerBehaviour(Agent agent, long _timer, long _block) {
		super(agent, _timer, _block);
	}

	@Override
	public abstract void run();
	
	public final boolean done() {
		return false;
	}

}
