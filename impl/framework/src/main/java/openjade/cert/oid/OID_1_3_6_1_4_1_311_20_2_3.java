package openjade.cert.oid;
/**
 * Class OID 1.3.6.1.4.1.311.20.2.3 <br>
 * 
 * <br>
 * UPN attribute: Used by micro$oft/window$ for logon with SmartCard, <br>
 * Some Certificate Autority has included this attribute in their certificates.
 * Is NOT a ICP-BRASIL pattern attribute
 * <b>*</b> <br>
 * 
 * <br>
 * Atributo conhecido como UPN: Utilizado pela micro$oft para logon com SmartCard, <br>
 * Presente em alguns cartoes de determinadas autoridades.
 * NAO eh padrao da ICP-BRASIL
 * <b>*</b> <br>
 * 

 *
 */
public class OID_1_3_6_1_4_1_311_20_2_3 extends OIDGeneric {
	
	public static final String OID = "1.3.6.1.4.1.311.20.2.3";
	
	public OID_1_3_6_1_4_1_311_20_2_3(){
	}
	
	public void initialize(){
	}
	
	/**
	 * 
	 * @return UPN 
	 */
	public String getUPN(){
		return super.getData();
	}

}
