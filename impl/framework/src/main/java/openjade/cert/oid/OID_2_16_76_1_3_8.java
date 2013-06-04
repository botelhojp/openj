package openjade.cert.oid;

/**
 * Classe OID 2.16.76.1.3.8 <br>
 * <br>
 * Possui alguns atributos de equipamento: <br>
 * <b>*</b> Nome empresarial constante do Cadastro Nacional de Pessoa Juridica (CNPJ),
 * sem abreviacoes, se o certificado for de pessoa juridica<br>
 * 

 *
 */

public class OID_2_16_76_1_3_8 extends OIDGeneric {
	
	public static final String OID = "2.16.76.1.3.8";
	
	public OID_2_16_76_1_3_8(){
	}
	
	public void initialize(){

	}
	
	/**
	 * 
	 * @return Nome empresarial constante do Cadastro Nacional de Pessoa Juridica (CNPJ),
	 *  sem abreviacoes, se o certificado for de pessoa juridica
	 */
	public String getNome(){
		return super.getData();
	}

}
