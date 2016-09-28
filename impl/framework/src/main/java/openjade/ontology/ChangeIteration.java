package openjade.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ChangeIteration
* @author ontology bean generator
* @version 2016/09/28, 00:27:58
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
