package openjade.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: WitnessResponse
* @author ontology bean generator
* @version 2016/09/28, 00:27:58
*/
public class WitnessResponse extends WitnessAction{ 

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
* Protege name: witnesses
   */
   private List witnesses = new ArrayList();
   public void addWitnesses(AID elem) { 
     List oldList = this.witnesses;
     witnesses.add(elem);
   }
   public boolean removeWitnesses(AID elem) {
     List oldList = this.witnesses;
     boolean result = witnesses.remove(elem);
     return result;
   }
   public void clearAllWitnesses() {
     List oldList = this.witnesses;
     witnesses.clear();
   }
   public Iterator getAllWitnesses() {return witnesses.iterator(); }
   public List getWitnesses() {return witnesses; }
   public void setWitnesses(List l) {witnesses = l; }

}
