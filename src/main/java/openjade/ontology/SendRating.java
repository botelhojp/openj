package openjade.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: SendRating
* @author ontology bean generator
* @version 2013/04/13, 16:00:58
*/
public class SendRating extends RatingAction{ 

   /**
* Protege name: rating
   */
   private List rating = new ArrayList();
   public void addRating(Rating elem) { 
     List oldList = this.rating;
     rating.add(elem);
   }
   public boolean removeRating(Rating elem) {
     List oldList = this.rating;
     boolean result = rating.remove(elem);
     return result;
   }
   public void clearAllRating() {
     List oldList = this.rating;
     rating.clear();
   }
   public Iterator getAllRating() {return rating.iterator(); }
   public List getRating() {return rating; }
   public void setRating(List l) {rating = l; }

}
