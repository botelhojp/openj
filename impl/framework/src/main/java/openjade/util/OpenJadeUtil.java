package openjade.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import jade.core.AID;
import openjade.ontology.Dossier;
import openjade.ontology.MerkleTree;
import openjade.ontology.Node;
import openjade.ontology.Rating;

public class OpenJadeUtil {

	private static Gson gson = new Gson();

	public static Rating makeRating(String clientAID, String serverAID, int round, jade.util.leap.List ra,
			String value) {
		Rating r = new Rating();
		r.setClient(new AID(clientAID, true));
		r.setServer(new AID(serverAID, true));
		r.setRound(round);
		r.setAttributes(ra);
		r.setValue(value);
		return r;
	}

	public static Rating makeRating(String clientAID, String serverAID, int round, String value) {
		return makeRating(clientAID, serverAID, round, null, value);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List convertList(jade.util.leap.List list) {
		if (list == null) {
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
		if (list == null) {
			return null;
		}
		jade.util.leap.List rt = new jade.util.leap.ArrayList();
		for (int i = 0; i < list.size(); i++) {
			rt.add(list.get(i));
		}
		return rt;
	}

	public static String md5(Rating rt) {
		return md5(gson.toJson(rt));
	}

	public static String md5(String input) {
		String md5 = null;
		if (null == input)
			return null;
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(input.getBytes(), 0, input.length());
			md5 = new BigInteger(1, digest.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md5;
	}

	public static void insert(Dossier d, Rating r) {
		d.addRatings(r);
		r.setIndex(d.getRatings().size()-1);
		d.setTree(makeTree(d.getRatings()));
	}

	private static MerkleTree makeTree(jade.util.leap.List ratings) {
		List<Node> hash = new ArrayList<Node>();
		for (int i = 0; i < ratings.size(); i++) {
			Rating rt = (Rating) ratings.get(i);
			Node node = new Node();
			node.setValue(md5(rt));
			hash.add(node);
		}
		List<Node> aux = hash;
		hash = new ArrayList<Node>();

		while (aux.size() > 1) {
			if (aux.size() % 2 != 0) {
				aux.add(aux.get(aux.size() - 1));
			}
			for (int i = 0; i < aux.size(); i = i + 2) {
				Node node = new Node();
				node.setNodeL(aux.get(i));
				node.setNodeR(aux.get(i + 1));
				node.setValue(md5(node.getNodeL().getValue() + node.getNodeR().getValue()));
				hash.add(node);
			}
			aux = hash;
			hash = new ArrayList<Node>();
		}
		MerkleTree mt = new MerkleTree();
		mt.setNode(aux.get(0));
		return mt;
	}

	public static boolean valid(Dossier dossier, Rating rt) {
		md5(rt);
		Node node = dossier.getTree().getNode();
		
		return false;
	}
}
