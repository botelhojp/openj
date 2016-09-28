package openjade.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Dossier
* @author ontology bean generator
* @version 2016/09/28, 00:27:58
*/
public class Dossier implements Concept {

   /**
* Protege name: tree
   */
   private MerkleTree tree;
   public void setTree(MerkleTree value) { 
    this.tree=value;
   }
   public MerkleTree getTree() {
     return this.tree;
   }

   /**
* Protege name: ratings
   */
   private List ratings = new ArrayList();
   public void addRatings(Rating elem) { 
     List oldList = this.ratings;
     ratings.add(elem);
   }
   public boolean removeRatings(Rating elem) {
     List oldList = this.ratings;
     boolean result = ratings.remove(elem);
     return result;
   }
   public void clearAllRatings() {
     List oldList = this.ratings;
     ratings.clear();
   }
   public Iterator getAllRatings() {return ratings.iterator(); }
   public List getRatings() {return ratings; }
   public void setRatings(List l) {ratings = l; }

}
