package openjade.ontology;


/**
* Protege name: ChangeIteration
* @author ontology bean generator
* @version 2014/07/8, 11:38:13
*/
public class ChangeIteration extends TimerAction{ 
	private static final long serialVersionUID = 1L;
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
