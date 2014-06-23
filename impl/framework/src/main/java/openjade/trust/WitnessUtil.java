package openjade.trust;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;

public class WitnessUtil {

	private static List<AID> witnesses = new ArrayList<AID>();

	public static void addWitness(AID clientAID) {
		if (!witnesses.contains(clientAID)) {
			witnesses.add(clientAID);
		}
	}

	public static List<AID> getWitness(double range) {
		range = (range >= 1.0) ? 1.0 : range;
		return witnesses.subList(0, ((int) (witnesses.size() * range)) - 1);
	}
}
