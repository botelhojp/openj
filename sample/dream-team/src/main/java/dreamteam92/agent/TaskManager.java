package dreamteam92.agent;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;
import java.util.List;

import openjade.core.OpenAgent;
import openjade.core.annotation.OnChangeIteration;
import openjade.core.annotation.OnGetUtility;
import openjade.core.annotation.ReceiveSimpleMessage;
import openjade.trust.DirectModel;

import org.apache.log4j.Logger;

import dreamteam92.bean.Task;

public class TaskManager extends OpenAgent {

	private static final long serialVersionUID = 1L;
	public static final long TOTAL_TASKS = 50;
	private Task currentTask = null;
	private Task taskTodo = null;
	private Task taskDone = null;
	private boolean taskIsDone = false;
	private List<Task> tasks = new ArrayList<Task>();
	private float utility;

	protected static Logger log = Logger.getLogger(TaskManager.class);

	protected void setup() {
		createTasks();
		log.debug("=== Tarefa ===");
		log.debug(currentTask);
		trustModel = new DirectModel();
		moveContainer("DreamTime-Container");
		registerService("task-manager", OpenAgent.TIMER_LISTENER);
	}

	@OnChangeIteration(delay = 300)
	public void onChangeIteration() {
		log.debug("================ Iteration:[" + iteration + "] ================");
		taskIsDone = false;
		taskDone = null;
		if (iteration <= tasks.size()) {
			currentTask = tasks.get(iteration - 1);
			log.debug("currentTask:" + currentTask.toString());

			if (taskTodo != null && taskTodo.isReserved()) {
				log.debug("team is completed" + taskTodo);
				sendMessage("task-agent", ACLMessage.INFORM, Task.DO_TASK, taskTodo);
			}else{
				taskTodo = null;
			}

		} else {
			log.debug("Done");
		}
	}

	@OnGetUtility
	public Float onGetUtility(long iteration) {
		log.debug("send Utility: [" + utility + "]");
		return utility;
	}

	@ReceiveSimpleMessage(conversationId = Task.REQUEST_TASK)
	public void requestTask(ACLMessage message) {
		sendMessage(message.getSender(), ACLMessage.INFORM, Task.GET_TASK, currentTask);
	}
	

	@ReceiveSimpleMessage(conversationId = Task.GET_TASK)
	public void getTask(ACLMessage message) throws UnreadableException {
		Task _task = (Task) message.getContentObject();
		taskTodo = (taskTodo == null) ? _task : taskTodo.merge(_task);
	}

	@ReceiveSimpleMessage(conversationId = Task.DONE_TASK)
	public void doneTask(ACLMessage message) throws UnreadableException {
		Task _task = (Task) message.getContentObject();
		taskDone = (taskDone == null)? _task : taskDone.merge(_task);
		if (taskDone.isDone() && !taskIsDone) {
			taskIsDone = true;
			log.debug("task [" + taskDone.getName() + "] is done" + taskDone);
			utility = (taskDone.getCompleted() + taskDone.getQuality()) / 2;
			sendMessage("task-agent", ACLMessage.INFORM, Task.DONE_TASK, taskDone);
		}
	}

	private void createTasks() {
		for (int i = 1; i <= TOTAL_TASKS; i++) {
			Task task = new Task("task-" + iteration);
			task.addSubTask(new Task("task-" + iteration + ".1", "A"));
			 task.addSubTask(new Task("task-" + iteration + ".2", "B"));
			 task.addSubTask(new Task("task-" + iteration + ".3", "C"));
			 task.addSubTask(new Task("task-" + iteration + ".3", "D"));
			tasks.add(task);
		}
	}
}
