package openjade.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: MerkleTree
* @author ontology bean generator
* @version 2016/09/27, 00:08:13
*/
public class MerkleTree implements Concept {

   /**
* Protege name: node
   */
   private List node = new ArrayList();
   public void addNode(Node elem) { 
     List oldList = this.node;
     node.add(elem);
   }
   public boolean removeNode(Node elem) {
     List oldList = this.node;
     boolean result = node.remove(elem);
     return result;
   }
   public void clearAllNode() {
     List oldList = this.node;
     node.clear();
   }
   public Iterator getAllNode() {return node.iterator(); }
   public List getNode() {return node; }
   public void setNode(List l) {node = l; }

}
