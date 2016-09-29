package openjade.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Node
* @author ontology bean generator
* @version 2016/09/29, 00:21:55
*/
public class Node implements Concept {

   /**
   * valor da iteração
* Protege name: value
   */
   private String value;
   public void setValue(String value) { 
    this.value=value;
   }
   public String getValue() {
     return this.value;
   }

   /**
* Protege name: nodeR
   */
   private Node nodeR;
   public void setNodeR(Node value) { 
    this.nodeR=value;
   }
   public Node getNodeR() {
     return this.nodeR;
   }

   /**
* Protege name: nodeL
   */
   private Node nodeL;
   public void setNodeL(Node value) { 
    this.nodeL=value;
   }
   public Node getNodeL() {
     return this.nodeL;
   }

}
