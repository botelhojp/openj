package openjade.ontology;


/**
* Protege name: Sign
* @author ontology bean generator
* @version 2014/07/8, 11:38:13
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
