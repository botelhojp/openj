package openjade.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: SendCertificate
* @author ontology bean generator
* @version 2013/03/31, 15:35:29
*/
public class SendCertificate extends MessageAction{ 

   /**
* Protege name: certificate
   */
   private Certificate certificate;
   public void setCertificate(Certificate value) { 
    this.certificate=value;
   }
   public Certificate getCertificate() {
     return this.certificate;
   }

   /**
* Protege name: aid
   */
   private AID aid;
   public void setAid(AID value) { 
    this.aid=value;
   }
   public AID getAid() {
     return this.aid;
   }

}
