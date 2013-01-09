package openjade.trust;

import java.io.Serializable;

import openjade.ontology.Rating;

public interface TrustModel extends Serializable {
	
	public String getName();

	public void addRating(Rating rating);

	public void setIteration(int iteration);

}
