package openjade.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Sign
* @author ontology bean generator
* @version 2013/04/13, 16:00:58
*/
public class Sign extends MessageAction{ 

   /**
* Protege name: pkcs7
   */
   private PKCS7Message pkcs7;
   public void setPkcs7(PKCS7Message value) { 
    this.pkcs7=value;
   }
   public PKCS7Message getPkcs7() {
     return this.pkcs7;
   }

}
