package openjade.trust;

import jade.core.AID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import openjade.core.RatingCache;
import openjade.ontology.Rating;
import openjade.setting.Settings;
import openjade.trust.model.Pair;

import org.apache.log4j.Logger;

public class DirectModel implements TrustModel {

	private static final long serialVersionUID = 1L;

	private Hashtable<AID, RatingCache> ratingHash = new Hashtable<AID, RatingCache>();

	private List<RatingCache> ratingList = new ArrayList<RatingCache>();

	protected static Logger log = Logger.getLogger(DirectModel.class);

	private Settings config = Settings.getInstance();

	private int iteration;

	public String getName() {
		return "DirectModel";
	}

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
	}

	public void setIteration(int _iteration) {
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
}
