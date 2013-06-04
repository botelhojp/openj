package openjade.trust;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;

import openjade.ontology.OpenJadeOntology;
import openjade.ontology.Rating;
import openjade.ontology.RequestRating;

public class IndirectModel extends GenericTrustModel {

	private static final long serialVersionUID = 1L;

	private List<AID> pairs = new ArrayList<AID>();

	public String getName() {
		return "IndirectModel";
	}
	
	
	@Override
	public void setIteration(int _iteration) {
		super.setIteration(_iteration);
		if (iteration > 1 && iteration % 5 == 0) {
			for (AID client : pairs) {
				for (AID server : pairs) {
					if (!client.equals(server)) {
						RequestRating requestRating = new RequestRating();
						requestRating.setAid(server);
						ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
						msg.setSender(myAgent.getAID());
						msg.addReceiver(client);
						myAgent.fillContent(msg, requestRating, myAgent.getCodec(), OpenJadeOntology.getInstance());
						myAgent.sendMessage(msg);
					}
				}

			}
		}
	}

	@Override
	public void addRating(Rating rating) {
		super.addRating(rating);
		addPair(rating.getServer());
	}

	private void addPair(AID pair) {
		if (!pairs.contains(pair)) {
			pairs.add(pair);
			log.debug("addPair: " + pair.toString());
		}
	}
}
