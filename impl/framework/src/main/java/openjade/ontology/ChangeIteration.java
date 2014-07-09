package openjade.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ChangeIteration
* @author ontology bean generator
* @version 2014/07/8, 21:13:29
*/
@SuppressWarnings("all")
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
