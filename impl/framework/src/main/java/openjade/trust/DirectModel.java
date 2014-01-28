package openjade.trust;

import java.util.List;

import openjade.trust.model.Pair;

public class DirectModel extends GenericTrustModel {

	private static final long serialVersionUID = 1L;

	// define a frequencia de uso da cache
	private static final double FREQUENCI_USE_CACHE = 0.7;

	public String getName() {
		return "DirectModel";
	}

	@Override
	public List<Pair> getPairs(String[] terms) {
		if (Math.random() <= FREQUENCI_USE_CACHE) {
			return super.getPairs(terms);
		} else {
			return null;
		}
	}
}
