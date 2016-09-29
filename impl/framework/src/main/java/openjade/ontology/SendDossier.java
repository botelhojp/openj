package openjade.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: SendDossier
* @author ontology bean generator
* @version 2016/09/29, 00:21:55
*/
public class SendDossier implements AgentAction {

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

}
