package openjade.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Certificate
* @author ontology bean generator
* @version 2013/01/1, 16:23:45
*/
public class Certificate extends ASCLMessage{ 

   /**
* Protege name: content
   */
   private Object content;
   public void setContent(Object value) { 
    this.content=value;
   }
   public Object getContent() {
     return this.content;
   }

   /**
* Protege name: algorithm
   */
   private String algorithm;
   public void setAlgorithm(String value) { 
    this.algorithm=value;
   }
   public String getAlgorithm() {
     return this.algorithm;
   }

}
