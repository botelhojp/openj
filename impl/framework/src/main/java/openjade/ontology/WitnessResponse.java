package openjade.ontology;

import jade.core.AID;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

/**
* Protege name: WitnessResponse
* @author ontology bean generator
* @version 2014/07/8, 11:38:13
*/
public class WitnessResponse extends WitnessAction{ 

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
