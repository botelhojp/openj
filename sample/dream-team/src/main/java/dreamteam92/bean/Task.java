package dreamteam92.bean;

import jade.core.AID;
import jade.util.leap.HashSet;
import jade.util.leap.Serializable;

import java.util.ArrayList;
import java.util.List;

public class Task implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String REQUEST_TASK = "REQUEST_TASK";
	public static final String GET_TASK = "GET_TASK";
	public static final String DO_TASK = "DO_TASK";
	public static final String DONE_TASK = "DONE_TASK";

	private List<Task> subtasks = null;

	private long completed;
	private long quality;
	private HashSet abilities = new HashSet();
	private String name;

	private AID ownerAID;

	public Task(String name) {
		subtasks = new ArrayList<Task>();
		this.name = name;
	}

	public Task(String name, String... abilities) {
		this(name);
		for (String ability : abilities) {
			this.abilities.add(ability);
		}
	}

	public List<Task> getSubtasks() {
		return subtasks;
	}

	public String getName() {
		return name;
	}

	public void execute(long completed, long quality) {
		if (subtasks.isEmpty()) {
			this.completed = completed;
			this.quality = quality;
		} else {
			throw new RuntimeException("subtasks is not empty");
		}
	}

	public long getCompleted() {
		if (subtasks.isEmpty()) {
			return completed;
		} else {
			long sum = 0;
			for (Task task : subtasks) {
				sum += task.getCompleted();
			}
			return sum / subtasks.size();
		}
	}

	public boolean isReserved() {
		if (subtasks.isEmpty()) {
			return (ownerAID != null);
		} else {
			for (Task task : subtasks) {
				if (!task.isReserved()) {
					return false;
				}
			}
			return true;
		}
	}

	public long getQuality() {
		if (subtasks.isEmpty()) {
			return quality;
		} else {
			long sum = 0;
			for (Task task : subtasks) {
				sum += task.getQuality();
			}
			return sum / subtasks.size();
		}
	}

	public void addSubTask(Task task) {
		subtasks.add(task);
		abilities.addAll(task.getAbilities());
	}

	public HashSet getAbilities() {
		return abilities;
	}

	public boolean hasNode() {
		return !subtasks.isEmpty();
	}

	public void setOwnerAID(AID aid) {
		this.ownerAID = aid;
	}

	public AID getOwnerAID() {
		return ownerAID;
	}

	@Override
	public String toString() {
		if (!hasNode()) {
			return "|" + getName() + "|c:" + getCompleted() + "|q:" + getQuality() + "|o:" + ((getOwnerAID() == null) ? "null" : getOwnerAID().getLocalName()) +"|";
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append("\n\n|").append(getName()).append("|c:").append(getCompleted()).append("|q:").append(getQuality()).append("|").append("\n\t");
			for (Task t : getSubtasks()) {
				sb.append(t.toString()).append("\n\t");
			}
			return sb.toString();
		}
	}

	public Task merge(Task t1) {
		if (!t1.hasNode()) {
			if (t1.getOwnerAID() != null && (getCompleted() == 0 || getQuality() == 0)) {
				setOwnerAID(t1.getOwnerAID());
				execute(t1.getCompleted(), t1.getQuality());
			}			
		} else {
			for (int i = 0; i < getSubtasks().size(); i++) {
				Task task1 = getSubtasks().get(i);
				Task task2 = t1.getSubtasks().get(i);
				task1.merge(task2);
			}
		}
		return this;
	}

	public boolean isDone() {
		if (!hasNode()) {
			return (getCompleted() > 0) || (getQuality() > 0);
		} else {
			for (Task task : subtasks) {
				if (!task.isDone()){
					return false;
				}
			}
			return true;
		}
	}

}
