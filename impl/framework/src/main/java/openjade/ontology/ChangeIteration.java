package openjade.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ChangeIteration
* @author ontology bean generator
* @version 2016/09/29, 00:21:55
*/
public class ChangeIteration extends TimerAction{ 

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

}
