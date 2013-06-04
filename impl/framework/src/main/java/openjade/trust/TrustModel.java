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
	
	public float getValue(AID server);

	public void setAgent(OpenAgent taskAgent);

	public List<Rating> getRatings(AID aid);

	public Rating addRating(AID client, AID server, int iteration, String term, float value);

}