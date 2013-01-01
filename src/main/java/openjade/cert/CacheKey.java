package openjade.cert;

import jade.core.AID;
import jade.util.leap.Serializable;

import java.security.Key;
import java.util.HashMap;

public class CacheKey implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private HashMap<AID, Key> keys = new HashMap<AID, Key>();

	public void put(AID aid, Key key) {
		keys.put(aid, key);
	}

	public Key get(AID aid) {
		return keys.get(aid);
	}
}
