package openjade.task.behaviour;

import jade.core.Agent;

import java.util.List;

import openjade.core.behaviours.CyclicTimerBehaviour;
import openjade.task.agent.TaskAgent;
import openjade.task.agent.ontology.Task;

import org.apache.log4j.Logger;
/**
 * Habilidade para tratar tarefas 
 */
public class AbilityBehaviour extends CyclicTimerBehaviour {

	private static final long serialVersionUID = 1L;

	protected static Logger log = Logger.getLogger(AbilityBehaviour.class);

	protected long capacity = 0;

	private TaskAgent myAgent;

	private AbilityConfig abilityConfig;

	public AbilityBehaviour(Agent agent, AbilityConfig abilityConfig) {
		super(agent, abilityConfig.speed(), abilityConfig.speed());
		myAgent = (TaskAgent) agent;
		this.abilityConfig = abilityConfig;
	}

	public void setAgent(TaskAgent _agent) {
		this.myAgent = _agent;
	}

	int getCompleted() {
		int range = (int) (Math.random() * abilityConfig.completedRange());
		if (Math.random() > 0.50000) {
			return abilityConfig.completed() + range;
		} else {
			return abilityConfig.completed() - range;
		}
	}

	public long capacity() {
		return abilityConfig.capacity();
	}

	int getPoints(int _base) {
		float points = _base * (abilityConfig.points()/100);
		float range = _base *  (abilityConfig.pointsRange()/100);
		if (Math.random() > 0.50000) {
			return (int) (points + range);
		} else {
			return (int) (points - range);
		}
	}

	String getStatus() {
		return TaskAgent.STATUS_DONE;
	}

	public boolean addTask(Task task) {
		capacity += task.getPoints();
		if (capacity > this.capacity()) {
			capacity -= task.getPoints();
			log.debug("addTask [NOK] capacity: " + capacity);
			return false;
		}
		return true;
	}

	/**
	 * Trata todas as tarefas no status "to process"
	 */
	@Override
	public void run() {
		List<Task> tasks = myAgent.getTasks().get(TaskAgent.TASK_TO_PROCESS);
		if (!tasks.isEmpty()) {
			Task task = tasks.remove(0);
			capacity -= task.getPoints();
			task.setCompleted(getCompleted());
			task.setStatus(getStatus());
			task.setPoints(getPoints(task.getPoints()));
			myAgent.getTasks().get(TaskAgent.TASK_TO_COMPLETED).add(task);
		}
	}

	public static AbilityBehaviour getInstance(String _abilityConfig, TaskAgent taskAgent) {
		AbilityConfig ability = AbilityConfig.valueOf(_abilityConfig.toUpperCase());
		return new AbilityBehaviour(taskAgent, ability);
	}

	public AbilityConfig getAbilityConfig() {
		return abilityConfig;
	}

	public void setAbilityConfig(AbilityConfig abilityConfig) {
		this.abilityConfig = abilityConfig;
	}
}
