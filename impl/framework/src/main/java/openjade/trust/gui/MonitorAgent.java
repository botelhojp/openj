package openjade.trust.gui;

import jade.content.ContentElement;
import jade.lang.acl.ACLMessage;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	private String fileCSV;

	protected static Logger log = Logger.getLogger(MonitorAgent.class);

	protected void setup() {
		super.setup();
		super.moveContainer(OpenAgent.MAIN_CONTAINER);
		log.debug("setup: " + getAID().getLocalName());
		getMonitorChart();
		cache = new RatingCache(1, 3);
		models = new HashSet<String>();
		
	    String strDate = (new SimpleDateFormat("-yyyyMMdd-HHmmss")).format(new Date());
		fileCSV = Thread.currentThread().getContextClassLoader().getResource("").getFile() + Settings.getInstance().getMonitorFileName() + strDate+ ".csv";
		
		addBehaviour(new RegisterServiceBehaviour(this, OpenAgent.SERVICE_TRUST_MONITOR));
	}

	@OnChangeIteration(delay = 0)
	public void onChangeIteration() {
		log.debug("---------- iteration [" + iteration + "] ---------------");
		cache.setIteration(iteration);
		if (cache.isCompleted()) {
			Iterator<String> it = models.iterator();
			
			StringBuffer csvModels = new StringBuffer();
			StringBuffer csvValues = new StringBuffer();
			int interation = cache.getMin();			
			
			while (it.hasNext()) {
				String model = it.next();
				try {
					Float value = cache.getValue(cache.getMin(), model);
					if (value != null) {
						csvModels.append(";").append(model);
						csvValues.append(";").append(value);
						getMonitorChart().addValue(model, cache.getMin(), value);
					}
				} catch (RuntimeException e) {
					log.debug(e);
				}				
			}
			writeCSV(interation, csvModels.toString(), csvValues.toString());
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
	
	private void writeCSV(int it, String models, String values) {
		try {			
			FileWriter csv;
			File file = new File(fileCSV);
			if (!file.exists()){
				log.debug("creating file csv: " + file.getAbsolutePath());
				csv = new FileWriter(file, true);
				csv.write("iteration" + models + "\n");				
			}else{
				csv = new FileWriter(file, true);
			}
			csv.write(it + values.replace('.', ',') + "\n");
			csv.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private MonitorChart getMonitorChart() {
		return MonitorChart.getInstance(Settings.getInstance().getMonitorMaxValue(), Settings.getInstance().getMonitorIterations());
	}
}
