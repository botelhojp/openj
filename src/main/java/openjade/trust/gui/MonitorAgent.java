package openjade.trust.gui;

import jade.content.ContentElement;
import jade.lang.acl.ACLMessage;

import java.util.HashSet;
import java.util.Iterator;

import openjade.core.OpenAgent;
import openjade.core.RatingCache;
import openjade.core.annotation.OnChangeIteration;
import openjade.core.annotation.ReceiveMatchMessage;
import openjade.core.behaviours.RegisterServiceBehaviour;
import openjade.ontology.OpenJadeOntology;
import openjade.ontology.Rating;
import openjade.ontology.SendRating;
import openjade.setting.Settings;

import org.apache.log4j.Logger;

public class MonitorAgent extends OpenAgent {

	private static final long serialVersionUID = 1L;
	private RatingCache cache;
	private HashSet<String> models;

	protected static Logger log = Logger.getLogger(MonitorAgent.class);

	protected void setup() {
		super.setup();
		super.moveContainer(OpenAgent.MAIN_CONTAINER);
		log.debug("setup: " + getAID().getLocalName());
		getMonitorChart();
		cache = new RatingCache(1, 3);
		models = new HashSet<String>();
		addBehaviour(new RegisterServiceBehaviour(this, OpenAgent.SERVICE_TRUST_MONITOR));
	}

	@OnChangeIteration(delay=0)
	public void onChangeIteration() {
		cache.setIteration(iteration);
		if (cache.isCompleted()) {
			Iterator<String> it = models.iterator();
			while (it.hasNext()) {
				String model = it.next();
				try {
					Float value = cache.getValue(cache.getMin(), model);
					if (value != null) {
						getMonitorChart().addValue(model, cache.getMin(), value);
					}
				} catch (RuntimeException e) {
					log.debug(e);
				}
			}
		}
	}

	@ReceiveMatchMessage(ontology = OpenJadeOntology.class, action = SendRating.class)
	public void receiveTaskDone(ACLMessage message, ContentElement ce) {
		SendRating sa = (SendRating) ce;
		for (int i = 0; i < sa.getRating().size(); i++) {
			Rating r = (Rating) sa.getRating().get(i);
			cache.add(r);
			models.add(r.getTerm());
		}
	}

	private MonitorChart getMonitorChart() {
		return MonitorChart.getInstance(Settings.getInstance().getMonitorMaxValue(), Settings.getInstance().getMonitorIterations());
	}
}
