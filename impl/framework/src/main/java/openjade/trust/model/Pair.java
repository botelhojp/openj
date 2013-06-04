package openjade.trust.model;

import jade.core.AID;

public class Pair implements Comparable<Pair> {

	private AID aid;

	private float weight;

	public Pair(AID aid, float weight) {
		super();
		this.aid = aid;
		this.weight = weight;
	}

	public AID getAid() {
		return aid;
	}

	public float getWeight() {
		return weight;
	}

	public int compareTo(Pair other) {
		if (this.getWeight() < other.getWeight()) {
			return -1;
		}
		if (this.getWeight() > other.getWeight()) {
			return 1;
		}
		return 0;
	}

}
