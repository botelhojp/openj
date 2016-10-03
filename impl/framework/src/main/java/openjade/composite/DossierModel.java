package openjade.composite;

import static openjade.util.OpenJadeUtil.md5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import openjade.ontology.Dossier;
import openjade.ontology.MerkleTree;
import openjade.ontology.Node;
import openjade.ontology.Rating;

public class DossierModel {

	private HashMap<String, Node> nodes;
	private Dossier dossier;
	
	public DossierModel(){
		dossier = new Dossier();
	}

	public void insert(Rating rt) {
		dossier.addRatings(rt);
		rt.setIndex(dossier.getRatings().size()-1);
		dossier.setTree(makeTree(dossier.getRatings()));
		
	}
	
	private MerkleTree makeTree(jade.util.leap.List ratings) {
		nodes = new HashMap<String, Node>();
		List<Node> hash = new ArrayList<Node>();
		for (int i = 0; i < ratings.size(); i++) {
			Rating rt = (Rating) ratings.get(i);
			Node node = new Node();
			node.setValue(md5(rt));
			hash.add(node);
			nodes.put(node.getValue(), node);
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
				nodes.put(node.getValue(), node);
			}
			aux = hash;
			hash = new ArrayList<Node>();
		}
		MerkleTree mt = new MerkleTree();
		mt.setNode(aux.get(0));
		return mt;
	}

	public  boolean valid(Rating rt) {
		Node node = getNode(md5(rt));
		if (node == null){
			return false;
		}
		return true;
	}

	public Node getNode(String hash) {
		return nodes.get(hash);
	}
	
	public Dossier getModel(){
		return dossier;
	}

	public jade.util.leap.List getRatings() {
		return dossier.getRatings();
	}

	public MerkleTree getTree() {
		return dossier.getTree();
	}

	public void setRatings(jade.util.leap.List list) {
		dossier.setRatings(list);
	}

	public void setTree(MerkleTree tree) {
		dossier.setTree(tree);
	}

}
