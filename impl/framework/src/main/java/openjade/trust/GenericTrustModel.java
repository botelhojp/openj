package openjade.trust;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import openjade.core.OpenAgent;
import openjade.core.RatingCache;
import openjade.ontology.OpenJadeOntology;
import openjade.ontology.Rating;
import openjade.ontology.SendRating;
import openjade.setting.Settings;
import openjade.trust.model.Pair;

import org.apache.log4j.Logger;

public abstract class GenericTrustModel implements ITrustModel {

	private static final long serialVersionUID = 1L;

	private Hashtable<AID, RatingCache> ratingHash = new Hashtable<AID, RatingCache>();

	private List<RatingCache> ratingList = new ArrayList<RatingCache>();

	protected static Logger log = Logger.getLogger(GenericTrustModel.class);

	private Settings config = Settings.getInstance();

	protected OpenAgent myAgent;

	protected int iteration;

	public abstract String getName();

	public void addRating(Rating rating) {
		log.debug(" = " + rating.getClient().getLocalName() + " -> " + rating.getServer().getLocalName() + "[" + rating.getValue() + "] - term: " + rating.getTerm());
		if (!ratingHash.containsKey(rating.getServer())) {
			RatingCache cache = new RatingCache(iteration, config.getTrust_DirectCacheSize());
			cache.add(rating);
			ratingList.add(cache);
			ratingHash.put(rating.getServer(), cache);
		} else {
			ratingHash.get(rating.getServer()).add(rating);
		}
		
//		super.addRating(rating);
		if (rating.getClient().equals(myAgent.getAID())){
			//Envia sua avaliacao ao grupo de listener
			SendRating sendRating = new SendRating();
			jade.util.leap.List ratingList = new jade.util.leap.ArrayList();
			ratingList.add(rating);
			sendRating.setRating(ratingList);
			myAgent.sendMessage(OpenAgent.TRUSTMODEL_REPUTATION_LISTENER, ACLMessage.AGREE, sendRating, OpenJadeOntology.getInstance());
		}
	}

	public Rating addRating(AID client, AID server, int iteration, String term, float value) {
		Rating rating = new Rating();
		rating.setClient(client);
		rating.setIteration(iteration);
		rating.setServer(server);
		rating.setTerm(term);
		rating.setValue(value);
		addRating(rating);
		return rating;
	}

	public List<Rating> getRatings(AID aid) {
		List<Rating> result = new ArrayList<Rating>();
		RatingCache cache = ratingHash.get(aid);
		if (cache != null) {
			for (int it = cache.getMin(); it <= iteration; it++) {
				List<Rating> ratings = cache.getRatings(it);
				if (ratings != null) {
					for (Rating rating : cache.getRatings(it)) {
						result.add(rating);
					}
				}
			}
		}
		return result;
	}

	public void currentIteration(int _iteration) {
		iteration = _iteration;
		log.debug("new iteration [" + iteration + "]");
		for (RatingCache rt : ratingList) {
			rt.setIteration(iteration);
		}
	}

	public List<Pair> getPairs(String[] terms) {
		List<Pair> pairs = new ArrayList<Pair>();
		Enumeration<AID> aids = ratingHash.keys();
		while (aids.hasMoreElements()) {
			AID aid = (AID) aids.nextElement();
			if (ratingHash.get(aid).isCompleted()) {
				pairs.add(new Pair(aid, ratingHash.get(aid).getValue()));
			}
		}
		Collections.sort(pairs);
		return pairs;
	}

	public float getValue(AID server) {
		if (ratingHash.containsKey(server)){
			return ratingHash.get(server).getValue();
		}else{
			return 0F;
		}
	}

	public void setAgent(OpenAgent _agent) {
		this.myAgent = _agent;
	}
}
