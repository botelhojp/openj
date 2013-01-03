package openjade.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Encipher
* @author ontology bean generator
* @version 2013/01/2, 10:37:57
*/
public class Encipher implements AgentAction {

   /**
* Protege name: signMode
   */
   private int signMode;
   public void setSignMode(int value) { 
    this.signMode=value;
   }
   public int getSignMode() {
     return this.signMode;
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

   /**
* Protege name: provider
   */
   private String provider;
   public void setProvider(String value) { 
    this.provider=value;
   }
   public String getProvider() {
     return this.provider;
   }

   /**
* Protege name: message
   */
   private EncryptedMessage message;
   public void setMessage(EncryptedMessage value) { 
    this.message=value;
   }
   public EncryptedMessage getMessage() {
     return this.message;
   }

}
