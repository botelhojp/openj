package openjade.trust;

import jade.core.AID;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import openjade.core.RatingCache;
import openjade.ontology.Rating;
import openjade.setting.Configuration;

import org.apache.log4j.Logger;

public class DirectModel implements TrustModel {

	private static final long serialVersionUID = 1L;

	private Hashtable<AID, RatingCache> fullRatings = new Hashtable<AID, RatingCache>();

	private List<RatingCache> ratings = new ArrayList<RatingCache>();

	protected static Logger log = Logger.getLogger(DirectModel.class);

	private Configuration config = Configuration.getInstance();
	
	private int iteration;

	public String getName() {
		return "DirectModel";
	}

	public void addRating(Rating rating) {
		log.debug(rating.getValue() + " " + rating.getClient().getLocalName() + " " + rating.getServer().getLocalName() + " " + rating.getTerm() + " " + rating.getIteration());
		if (!fullRatings.containsKey(rating.getServer())) {
			RatingCache cache = new RatingCache(iteration, config.getTrust_DirectCacheSize());
			cache.add(rating);
			ratings.add(cache);
			fullRatings.put(rating.getServer(), cache);
		} else {
			fullRatings.get(rating.getServer()).add(rating);
		}
	}

	public void setIteration(int _iteration) {
		iteration = _iteration;
		log.debug("setIteration [" + iteration+ "]");
		for (RatingCache rt : ratings) {
			rt.setIteration(iteration);
		}
	}

}
