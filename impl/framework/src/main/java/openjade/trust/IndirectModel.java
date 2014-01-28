package openjade.trust;

import openjade.core.OpenAgent;
import openjade.ontology.Rating;

public class IndirectModel extends GenericTrustModel {

	private static final long serialVersionUID = 1L;

	public String getName() {
		return "IndirectModel";
	}
	
	@Override
	public void setIteration(int _iteration) {
		super.setIteration(_iteration);
	}

	@Override
	public void addRating(Rating rating) {
		super.addRating(rating);
//		if (rating.getClient().equals(myAgent.getAID())){
//			//Envia sua avaliacao ao grupo de listener
//			SendRating sendRating = new SendRating();
//			jade.util.leap.List ratingList = new jade.util.leap.ArrayList();
//			ratingList.add(rating);
//			sendRating.setRating(ratingList);
//			myAgent.sendMessage(OpenAgent.TRUSTMODEL_REPUTATION_LISTENER, ACLMessage.AGREE, sendRating, OpenJadeOntology.getInstance());
//		}		
	}
	
	@Override
	public void setAgent(OpenAgent _agent) {
		super.setAgent(_agent);
		myAgent.addService(OpenAgent.TRUSTMODEL_REPUTATION_LISTENER);
	}
}
