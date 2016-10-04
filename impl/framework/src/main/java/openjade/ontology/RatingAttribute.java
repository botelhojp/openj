package openjade.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: RatingAttribute
* @author ontology bean generator
* @version 2016/10/3, 23:54:47
*/
public class RatingAttribute implements Concept {

   /**
   * valor da iteração
* Protege name: value
   */
   private String value;
   public void setValue(String value) { 
    this.value=value;
   }
   public String getValue() {
     return this.value;
   }

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

}
