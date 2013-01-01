package openjade.cert.oid;

/**
 * Classe OID 2.16.76.1.3.3 <br>
 * <br>
 * Possui alguns atributos especificos de pessoa juridica ou equipamento: <br>
 * <b>*</b> Cadastro Nacional de Pessoa Juridica (CNPJ) da pessoa juridica titular do certificado <br>
 * 

 *
 */

public class OID_2_16_76_1_3_3 extends OIDGeneric {
	
public static final String OID = "2.16.76.1.3.3";
	
	public OID_2_16_76_1_3_3(){
	}
	
	public void initialize(){
	}
	
	/**
	 * 
	 * @return numero do Cadastro Nacional de Pessoa Juridica (CNPJ) da empresa titular do certificado
	 */
	public String getCNPJ(){
		return super.getData();
	}
}
