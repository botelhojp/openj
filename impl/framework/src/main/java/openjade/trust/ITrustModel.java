package openjade.trust;

import jade.core.AID;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.List;

import openjade.core.OpenAgent;
import openjade.ontology.Rating;
import openjade.trust.model.Pair;

public interface ITrustModel extends Serializable {

	public void addRating(Rating rating);
	
	public Enumeration<AID> getAllServer();

	public void currentIteration(int iteration);

	public List<Pair> getPairs(String[] terms);
	
	public float getValue(AID server);

	public void setAgent(OpenAgent taskAgent);

	public List<Rating> getRatings(AID aid);

	public Rating addRating(AID client, AID server, int iteration, String term, float value);

	public Reliable isReliable(AID agent);

}
