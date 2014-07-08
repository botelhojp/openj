package openjade.util;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import openjade.ontology.Rating;

public class OpenJadeUtil {

	public static Rating makeRating(AID clientAID, AID serverAID, String attributes, String value) {
		Rating r = new Rating();
		r.setClient(clientAID);
		r.setServer(serverAID);
		r.setAttributes(attributes);
		r.setValue(value);		
		return r;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List convertList(jade.util.leap.List list) {
		if (list == null){
			return null;
		}
		List rt = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			rt.add(list.get(i));
		}
		return rt;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static jade.util.leap.List convertList(List list) {
		if (list == null){
			return null;
		}
		jade.util.leap.List rt = new jade.util.leap.ArrayList();
		for (int i = 0; i < list.size(); i++) {
			rt.add(list.get(i));
		}
		return rt;
	}
}
