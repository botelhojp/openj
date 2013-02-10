package openjade.trust;

import java.util.List;

import openjade.ontology.Rating;
import openjade.trust.model.Pair;

public class IndirectModel implements TrustModel {

	private static final long serialVersionUID = 1L;

	public String getName() {
		return "IndirectModel";
	}

	public void addRating(Rating rating) {
		// TODO Auto-generated method stub
	}

	public void setIteration(int iteration) {
		// TODO Auto-generated method stub
	}
	
	public List<Pair> getPairs(String term) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Pair> getPairs(String[] terms) {
		// TODO Auto-generated method stub
		return null;
	}

}
