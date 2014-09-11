package openjade.trust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jade.core.AID;

public class WitnessUtil {

	private static HashMap<AID, List<AID>>  witnesses = new HashMap<AID, List<AID>>();

	public static void addWitness(AID server, AID client) {
		if (!witnesses.containsKey(server)) {
			witnesses.put(server, new ArrayList<AID>());
		}
		if (!witnesses.get(server).contains(client)){
			witnesses.get(server).add(client);
		}
	}

	public static List<AID> getWitness(AID server) {
		return witnesses.get(server);
	}
}
