package openjade.ontology;

import jade.core.AID;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

/**
* Protege name: Rating
* @author ontology bean generator
* @version 2014/07/8, 21:13:29
*/
@SuppressWarnings("all")
public class Rating extends ASCLMessage{ 

   /**
* Protege name: client
   */
   private AID client;
   public void setClient(AID value) { 
    this.client=value;
   }
   public AID getClient() {
     return this.client;
   }

   /**
* Protege name: server
   */
   private AID server;
   public void setServer(AID value) { 
    this.server=value;
   }
   public AID getServer() {
     return this.server;
   }

   /**
* Protege name: round
   */
   private int round;
   public void setRound(int value) { 
    this.round=value;
   }
   public int getRound() {
     return this.round;
   }

   /**
   * valor da itera��o
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
* Protege name: attributes
   */
   private List attributes = new ArrayList();
   public void addAttributes(RatingAttribute elem) { 
     List oldList = this.attributes;
     attributes.add(elem);
   }
   public boolean removeAttributes(RatingAttribute elem) {
     List oldList = this.attributes;
     boolean result = attributes.remove(elem);
     return result;
   }
   public void clearAllAttributes() {
     List oldList = this.attributes;
     attributes.clear();
   }
   public Iterator getAllAttributes() {return attributes.iterator(); }
   public List getAttributes() {return attributes; }
   public void setAttributes(List l) {attributes = l; }

}
