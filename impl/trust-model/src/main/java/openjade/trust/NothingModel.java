package openjade.trust;

import jade.core.AID;

import java.util.List;

import openjade.core.OpenAgent;
import openjade.ontology.Rating;
import openjade.trust.model.Pair;

public class NothingModel extends GenericTrustModel {

	private static final long serialVersionUID = 1L;

	public String getName() {
		return "NothingModel";
	}

	@Override
	public void addRating(Rating rating) {
	}

	@Override
	public void setIteration(int iteration) {
	}

	@Override
	public List<Pair> getPairs(String[] terms) {
		return null;
	}

	@Override
	public void setAgent(OpenAgent taskAgent) {
	}

	@Override
	public List<Rating> getRatings(AID aid) {
		return null;
	}

	@Override
	public Rating addRating(AID client, AID server, int iteration, String term, float value) {
		return super.addRating(client, server, iteration, term, value);
	}

}
