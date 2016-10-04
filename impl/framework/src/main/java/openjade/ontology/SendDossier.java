package openjade.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: SendDossier
* @author ontology bean generator
* @version 2016/10/3, 23:54:47
*/
public class SendDossier extends MessageAction{ 

   /**
* Protege name: dossier
   */
   private Dossier dossier;
   public void setDossier(Dossier value) { 
    this.dossier=value;
   }
   public Dossier getDossier() {
     return this.dossier;
   }

   /**
* Protege name: signature
   */
   private PKCS7Message signature;
   public void setSignature(PKCS7Message value) { 
    this.signature=value;
   }
   public PKCS7Message getSignature() {
     return this.signature;
   }

}
