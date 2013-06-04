package openjade.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ChangeIteration
* @author ontology bean generator
* @version 2013/04/13, 16:00:58
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
