package openjade.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ChangeIteration
* @author ontology bean generator
* @version 2014/06/23, 00:40:32
*/
public class ChangeIteration extends TimerAction{ 

   /**
* Protege name: iteration
   */
   private int iteration;
   public void setIteration(int value) { 
    this.iteration=value;
   }
   public int getIteration() {
     return this.iteration;
   }

}
