package openjade.core.behaviours;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;

import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

public abstract class TimerBehaviour extends Behaviour {

	private static final long serialVersionUID = 1L;

	protected static Logger log = Logger.getLogger(TimerBehaviour.class);

	private long lastTime = GregorianCalendar.getInstance().getTimeInMillis();
	private long currentTime = GregorianCalendar.getInstance().getTimeInMillis();
	private long timer;
	private long block;

	public TimerBehaviour(Agent agent, long _timer) {
		this(agent, _timer, 0);
	}

	public TimerBehaviour(Agent agent, long _timer, long _block) {
		super(agent);
		this.timer = _timer;
		this.block = _block;
	}

	@Override
	public final void action() {
		if (currentTime - lastTime > getTimer()) {
			lastTime = currentTime;
			run();
		}
		currentTime = GregorianCalendar.getInstance().getTimeInMillis();
		if (block > 0) {
			block(block);
		}
	}

	public long getTimer() {
		return timer;
	}

	public void setTimer(long timer) {
		this.timer = timer;
	}

	public abstract void run();

	@Override
	public abstract boolean done();
}
