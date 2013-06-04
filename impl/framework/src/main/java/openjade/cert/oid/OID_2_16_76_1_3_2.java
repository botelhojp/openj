package openjade.cert.oid;
/**
 * Classe OID 2.16.76.1.3.2 <br>
 * <br>
 * Possui alguns atributos especificos de pessoa juridica ou equipamento: <br>
 * <b>*</b> Nome do responsavel pelo certificado <br>
 * 

 */
public class OID_2_16_76_1_3_2 extends OIDGeneric{

public static final String OID = "2.16.76.1.3.2";
	
	public OID_2_16_76_1_3_2(){
	}
	
	public void initialize(){
	}
	
	/**
	 * 
	 * @return nome do responsavel pelo certificado
	 */
	public String getNome(){
		return super.getData();
	}
}
