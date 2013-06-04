package dreamteam92.agent;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import openjade.core.OpenAgent;
import openjade.core.annotation.OnChangeIteration;
import openjade.core.annotation.ReceiveSimpleMessage;
import openjade.trust.TrustModelFactory;

import org.apache.log4j.Logger;

import dreamteam92.bean.Task;

public class TaskAgent extends OpenAgent {

	private String[] ability;

	private double efficiency;
	
	private Task currentTask = null;
	
	private static final long serialVersionUID = 1L;

	protected static Logger log = Logger.getLogger(TaskAgent.class);

	protected void setup() {
		trustModel = TrustModelFactory.create((String) getArguments()[0]);
		efficiency = Double.parseDouble((String) getArguments()[1]);
		ability = new String[getArguments().length - 2];
		for (int index = 2; index < getArguments().length; index++) {
			ability[index - 2] = (String) getArguments()[index];
		}
		moveContainer("DreamTime-Container");
		registerService("task-agent", OpenAgent.TIMER_LISTENER);
	}

	@OnChangeIteration(delay=500)
	public void onChangeIteration() {
		currentTask = null;
		if (iteration <= TaskManager.TOTAL_TASKS) {
			sendMessage("task-manager", ACLMessage.INFORM, Task.REQUEST_TASK);
		}
	}

	@ReceiveSimpleMessage(conversationId = Task.GET_TASK)
	public void getTask(ACLMessage message) throws UnreadableException {
		Task task = (Task) message.getContentObject();
		currentTask = (currentTask == null) ? task : currentTask.merge(task);
//		log.debug("RECEBI:" + currentTask);
		if (verify(currentTask)) {
//			log.debug("ENVIEI:" + currentTask);
			String[] senders = {"task-manager","task-agent"};
			sendMessage(senders, ACLMessage.INFORM, Task.GET_TASK, currentTask);
		}
	}

	@ReceiveSimpleMessage(conversationId = Task.DO_TASK)
	public void doTask(ACLMessage message) throws UnreadableException {
		Task task = (Task) message.getContentObject();
		if (executeTask(task)){
			sendMessage("task-manager", ACLMessage.INFORM, Task.DONE_TASK, task);
		}
	}
	
	
	@ReceiveSimpleMessage(conversationId = Task.DONE_TASK)
	public void doneTask(ACLMessage message) throws UnreadableException {
		Task task = (Task) message.getContentObject();
		updateTrust(task);
	}

	private boolean executeTask(Task task) {
		boolean run = false;
		if (!task.hasNode()) {
			if (task.getOwnerAID().equals(getAID())) {
				long q = (long) (efficiency);
				long c = (long) (efficiency);
				task.execute(c, q);
				return true;
			}
		} else {
			for (Task t : task.getSubtasks()) {
				if (executeTask(t)){
					run = true;
				}
			}
		}
		return run;
	}

	private boolean verify(Task task) {
		boolean canDo = false;
		if (!task.hasNode()) {
			for (String myAbility : ability) {
				if (task.getOwnerAID() == null && task.getAbilities().contains(myAbility)) {
					task.setOwnerAID(this.getAID());
					canDo = true;
				} else if (task.getOwnerAID() != null && task.getOwnerAID() != getAID() && task.getAbilities().contains(myAbility)) {
					float trust = trustModel.getValue(task.getOwnerAID());
					if (trust > 0 && efficiency > trust){
						log.debug("I'm better than [" + task.getOwnerAID().getLocalName() + "]--> " + efficiency + "/" + trust);
						task.setOwnerAID(this.getAID());
						canDo = true;
					}
				}
			}
		} else {
			for (Task t : task.getSubtasks()) {
				if (verify(t)) {
					canDo = true;
				}
			}
		}
		return canDo;
	}
	
	private void updateTrust(Task task) {
		if (!task.hasNode()) {
			trustModel.addRating(getAID(), task.getOwnerAID(), iteration, "completed", task.getCompleted());
			trustModel.addRating(getAID(), task.getOwnerAID(), iteration, "quality", task.getQuality());
		} else {
			for (Task t : task.getSubtasks()) {
				updateTrust(t);
			}
		}
	}
}
