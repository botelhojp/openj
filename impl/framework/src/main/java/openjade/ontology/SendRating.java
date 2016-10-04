package openjade.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: SendRating
* @author ontology bean generator
* @version 2016/10/3, 23:54:47
*/
public class SendRating extends RatingAction{ 

   /**
* Protege name: ratings
   */
   private List ratings = new ArrayList();
   public void addRatings(Rating elem) { 
     List oldList = this.ratings;
     ratings.add(elem);
   }
   public boolean removeRatings(Rating elem) {
     List oldList = this.ratings;
     boolean result = ratings.remove(elem);
     return result;
   }
   public void clearAllRatings() {
     List oldList = this.ratings;
     ratings.clear();
   }
   public Iterator getAllRatings() {return ratings.iterator(); }
   public List getRatings() {return ratings; }
   public void setRatings(List l) {ratings = l; }

}
