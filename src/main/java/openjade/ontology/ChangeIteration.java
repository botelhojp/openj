package openjade.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ChangeIteration
* @author ontology bean generator
* @version 2013/03/28, 22:59:51
*/
public class ChangeIteration implements AgentAction {

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
