package openjade.trust;

import openjade.ontology.Rating;

public class DirectModel implements TrustModel {

	private static final long serialVersionUID = 1L;

	public String getName() {
		return "DirectModel";
	}

	@Override
	public void addRating(Rating rating) {
		
	}

}
