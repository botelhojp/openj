package openjade.trust;

import jade.core.AID;

import java.io.Serializable;
import java.util.List;

import openjade.core.OpenAgent;
import openjade.ontology.Rating;
import openjade.trust.model.Pair;

public interface TrustModel extends Serializable {

	public String getName();

	public void addRating(Rating rating);

	public void setIteration(int iteration);

	public List<Pair> getPairs(String[] terms);

	public void setAgent(OpenAgent taskAgent);

	public List<Rating> getRatings(AID aid);

}
