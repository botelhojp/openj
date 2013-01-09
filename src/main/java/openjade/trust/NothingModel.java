package openjade.trust;

import openjade.ontology.Rating;

public class NothingModel implements TrustModel {

	private static final long serialVersionUID = 1L;

	public String getName() {
		return "NothingModel";
	}

	@Override
	public void addRating(Rating rating) {
	}

	@Override
	public void setIteration(int iteration) {
		// TODO Auto-generated method stub
		
	}

}
