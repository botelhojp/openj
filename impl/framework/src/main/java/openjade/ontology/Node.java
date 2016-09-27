package openjade.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Node
* @author ontology bean generator
* @version 2016/09/27, 00:08:13
*/
public class Node implements Concept {

   /**
* Protege name: nodeR
   */
   private List nodeR = new ArrayList();
   public void addNodeR(Node elem) { 
     List oldList = this.nodeR;
     nodeR.add(elem);
   }
   public boolean removeNodeR(Node elem) {
     List oldList = this.nodeR;
     boolean result = nodeR.remove(elem);
     return result;
   }
   public void clearAllNodeR() {
     List oldList = this.nodeR;
     nodeR.clear();
   }
   public Iterator getAllNodeR() {return nodeR.iterator(); }
   public List getNodeR() {return nodeR; }
   public void setNodeR(List l) {nodeR = l; }

   /**
* Protege name: nodeL
   */
   private List nodeL = new ArrayList();
   public void addNodeL(Node elem) { 
     List oldList = this.nodeL;
     nodeL.add(elem);
   }
   public boolean removeNodeL(Node elem) {
     List oldList = this.nodeL;
     boolean result = nodeL.remove(elem);
     return result;
   }
   public void clearAllNodeL() {
     List oldList = this.nodeL;
     nodeL.clear();
   }
   public Iterator getAllNodeL() {return nodeL.iterator(); }
   public List getNodeL() {return nodeL; }
   public void setNodeL(List l) {nodeL = l; }

}
