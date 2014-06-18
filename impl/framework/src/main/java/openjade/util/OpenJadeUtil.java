package openjade.util;

import jade.core.AID;
import openjade.ontology.Rating;

public class OpenJadeUtil {

	public static Rating makeRating(AID clientAID, AID serverAID, int iteration2, String term, int value) {
		Rating r = new Rating();
		r.setClient(clientAID);
		r.setServer(serverAID);
		r.setIteration(iteration2);
		r.setTerm(term);
		r.setValue(value);
		return r;
	}

}
