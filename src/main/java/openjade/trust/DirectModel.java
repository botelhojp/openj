package openjade.trust;

import jade.core.AID;

import java.util.Hashtable;

import openjade.core.RatingCache;
import openjade.ontology.Rating;

public class DirectModel implements TrustModel {
	
	private Hashtable<AID, RatingCache> ratings = new Hashtable<AID, RatingCache>();

	private static final long serialVersionUID = 1L;

	public String getName() {
		return "DirectModel";
	}

	public void addRating(Rating rating) {
	}

}
