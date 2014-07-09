package openjade.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: RatingAttribute
* @author ontology bean generator
* @version 2014/07/8, 21:13:29
*/
@SuppressWarnings("all")
public class RatingAttribute implements Concept {

   /**
* Protege name: name
   */
   private String name;
   public void setName(String value) { 
    this.name=value;
   }
   public String getName() {
     return this.name;
   }

   /**
   * valor da itera��o
* Protege name: value
   */
   private String value;
   public void setValue(String value) { 
    this.value=value;
   }
   public String getValue() {
     return this.value;
   }

}
