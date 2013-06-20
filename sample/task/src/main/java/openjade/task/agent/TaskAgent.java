package openjade.task.agent;

import jade.content.ContentElement;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import openjade.core.OpenAgent;
import openjade.core.OpenJadeException;
import openjade.core.RatingCache;
import openjade.core.annotation.OnChangeIteration;
import openjade.core.annotation.OnGetUtility;
import openjade.core.annotation.ReceiveMatchMessage;
import openjade.ontology.OpenJadeOntology;
import openjade.ontology.Rating;
import openjade.ontology.RequestRating;
import openjade.ontology.SendRating;
import openjade.task.agent.ontology.SendTask;
import openjade.task.agent.ontology.Task;
import openjade.task.agent.ontology.TaskOntology;
import openjade.task.behaviour.AbilityBehaviour;
import openjade.task.behaviour.AbilityConfig;
import openjade.task.behaviour.RequestTaskBehaviour;
import openjade.task.behaviour.ResponseTaskBehaviour;
import openjade.trust.TrustModelFactory;

import org.apache.log4j.Logger;

public class TaskAgent extends OpenAgent {
	
	public static final String SERVICE_WORKER = "worker";	
	public static final String TASK_TO_DELEGATE = "TASK_TODO";
	public static final String TASK_TO_PROCESS = "TASK_TO_PROCESS";
	public static final String TASK_TO_COMPLETED = "TASK_TO_COMPLETED";	
	public static final String STATUS_DONE = "DONE";
	public static final String STATUS_NEW = "NEW";

	private static final long serialVersionUID = 1L;
	protected static Logger log = Logger.getLogger(TaskAgent.class);

	protected String keystore;
	protected String keystorePassword;
	private RatingCache cache;
	private AbilityBehaviour ability;
	private Hashtable<String, List<Task>> tasks = new Hashtable<String, List<Task>>();

	protected void setup() {
		switch (getArguments().length) {
		case 3:
			setupNoSigner();
			break;
		case 5:
			setupSigner();
			break;
		default:
			throw new OpenJadeException("Numero de parametros invalidos, usar 3 (sem certificado) ou 5 (com certificado)");
		}
		tasks.put(TASK_TO_DELEGATE, new ArrayList<Task>());
		tasks.put(TASK_TO_PROCESS, new ArrayList<Task>());
		tasks.put(TASK_TO_COMPLETED, new ArrayList<Task>());

		addBehaviour(ability);
		trustModel.setAgent(this);
		log.debug("setup: " + getAID().getLocalName());
		cache = new RatingCache(1, 10);

		registerService(SERVICE_WORKER, OpenAgent.TIMER_LISTENER);

		addBehaviour(new RequestTaskBehaviour(this));
		addBehaviour(new ResponseTaskBehaviour(this));
	}

	private void setupSigner() {
		keystore = (String) getArguments()[0];
		keystorePassword = (String) getArguments()[1];
		moveContainer((String) getArguments()[2]);
		trustModel = TrustModelFactory.create((String) getArguments()[3]);
		ability = AbilityBehaviour.getInstance((String) getArguments()[4], this);
	}

	private void setupNoSigner() {
		moveContainer((String) getArguments()[0]);
		trustModel = TrustModelFactory.create((String) getArguments()[1]);
		ability = AbilityBehaviour.getInstance((String) getArguments()[2], this);
	}

	/**
	 * Para cada iteração cria um conjunto de tarefas para serem processadas e
	 * envia suas satisfações para a iteração atual
	 * 
	 * @param message
	 * @param ce
	 */	
	@OnChangeIteration
	public void onChangeIteration() {
		cache.setIteration(iteration);
		sendTasks(5);
		if (iteration > 1 && iteration % 10 == 0){
			log.debug("----------------changeAbility----------------");
			AbilityConfig change = ability.getAbilityConfig().change();
			log.debug(ability.getAbilityConfig() + ">" + change);
			ability.setAbilityConfig(change);
		}
	}
	
	@OnGetUtility
	public Float getUtility(long iteration) {
		return cache.getValue();
	}
	
	/**
	 * Recebe o pedido para executar uma tarefa
	 * @param message
	 * @param ce
	 */
	@ReceiveMatchMessage(action = SendTask.class, performative = { ACLMessage.REQUEST }, ontology = TaskOntology.class)
	public void receiveTaskRequest(ACLMessage message, ContentElement ce) {
		SendTask action = (SendTask) ce;
		if (ability.addTask(action.getTask())) {
			tasks.get(TASK_TO_PROCESS).add(action.getTask());
		} else {
			ACLMessage msg = new ACLMessage(ACLMessage.REFUSE);
			msg.setSender(getAID());
			msg.addReceiver(action.getTask().getTaskSender());
			fillContent(msg, action, getCodec(), TaskOntology.getInstance());
			sendMessage(msg);
		}
	}

	/**
	 * Recebe mensagem que a tarefa foi executada
	 * @param message
	 * @param ce
	 */
	@ReceiveMatchMessage(action = SendTask.class, performative = { ACLMessage.CONFIRM }, ontology = TaskOntology.class)
	public void receiveTaskDone(ACLMessage message, ContentElement ce) {
		SendTask da = (SendTask) ce;
		int satistaction = (da.getTask().getCompleted() + da.getTask().getPoints()) / 2;

		trustModel.addRating(newRating(getAID(), message.getSender(), iteration, "completed", da.getTask().getCompleted()));
		trustModel.addRating(newRating(getAID(), message.getSender(), iteration, "points", da.getTask().getPoints()));

		cache.add(newRating(getAID(), message.getSender(), iteration, trustModel.getName(), satistaction));
	}

	/**
	 * Recebe mensagem que a tarafe foi rejeitada
	 * @param message
	 * @param ce
	 */
	@ReceiveMatchMessage(action = SendTask.class, performative = { ACLMessage.REFUSE }, ontology = TaskOntology.class)
	public void receiveTaskRefuse(ACLMessage message, ContentElement ce) {
		log.debug("REFUSE");
		cache.add(newRating(getAID(), message.getSender(), iteration, trustModel.getName(), 0.0F));
		cache.add(newRating(getAID(), message.getSender(), iteration, "Refuse", 1.0F));
	}

	public void sendConfirmTask(SendTask action) {
		ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
		msg.setSender(getAID());
		msg.addReceiver(action.getTask().getTaskSender());
		fillContent(msg, action, getCodec(), TaskOntology.getInstance());
		sendMessage(msg);
	}

	/**
	 * Criar tarefas que deveram ser processadas
	 * @param count
	 */
	private void sendTasks(int count) {
		for (int i = 0; i < count; i++) {
			Task task = new Task();
			task.setCompleted(0);
			task.setPoints(100);
			task.setStatus(STATUS_NEW);
			task.setTaskSender(getAID());
			tasks.get(TASK_TO_DELEGATE).add(task);
		}
	}

	public Hashtable<String, List<Task>> getTasks() {
		return tasks;
	}

	private Rating newRating(AID _client, AID _server, int _iteration, String _term, float _value) {
		Rating rating = new Rating();
		rating.setClient(_client);
		rating.setIteration(_iteration);
		rating.setServer(_server);
		rating.setTerm(_term);
		rating.setValue(_value);
		return rating;
	}
	
//	/**
//	 * Troca as habilidades dos agentes
//	 */
//	private void changeAbility() {
//	}
	
	//PARA O MODELO DE CONFIANÇA INDIRETO

	/**
	 * Atende uma solicitação para envio das suas avaliações para um determinado AID
	 * @param message
	 * @param ce
	 */
	@ReceiveMatchMessage(action = RequestRating.class, ontology = OpenJadeOntology.class)
	public void responseSendRation(ACLMessage message, ContentElement ce) {
		RequestRating request = (RequestRating) ce;
		AID aid = request.getAid();
		List<Rating> ratings = trustModel.getRatings(aid);
		if (ratings != null && !ratings.isEmpty()) {
			SendRating sendRating = new SendRating();
			for (Rating r : ratings) {
				sendRating.addRating(r);
			}
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setSender(getAID());
			msg.addReceiver(message.getSender());
			fillContent(msg, sendRating, getCodec(), OpenJadeOntology.getInstance());			
			sendMessage(msg);
		}
	}

	/**
	 * Recebe a avaliacao
	 * @param message
	 * @param ce
	 */
	@ReceiveMatchMessage(action = SendRating.class, performative={ACLMessage.INFORM}, ontology = OpenJadeOntology.class)
	public void receptSendRation(ACLMessage message, ContentElement ce) {
		SendRating sd = (SendRating) ce;
		for (int i = 0; i < sd.getRating().size(); i++) {
			Rating rt = (Rating) sd.getRating().get(i);
			this.trustModel.addRating(rt);
		}
	}
}
