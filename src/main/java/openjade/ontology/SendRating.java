package openjade.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: SendRating
* @author ontology bean generator
* @version 2013/01/8, 14:06:37
*/
public class SendRating implements AgentAction {

   /**
* Protege name: rating
   */
   private Rating rating;
   public void setRating(Rating value) { 
    this.rating=value;
   }
   public Rating getRating() {
     return this.rating;
   }

}
