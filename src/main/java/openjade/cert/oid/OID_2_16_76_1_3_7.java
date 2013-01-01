package openjade.cert.oid;

/**
 * Classe OID 2.16.76.1.3.7 <br>
 * <br>
 * Possui alguns atributos de pessoa juridica: <br>
 * <b>*</b> Numero do Cadastro Especifico do INSS (CEI) da pessoa juridica titular do certificado <br>
 * 

 *
 */

public class OID_2_16_76_1_3_7 extends OIDGeneric {
	
	public static final String OID = "2.16.76.1.3.7";
	
	protected static final Object CAMPOS[] = {"CEI",   (int) 12};

	public OID_2_16_76_1_3_7(){
	}
	
	public void initialize(){
		super.initialize(CAMPOS);
	}
	
	/**
	 * 
	 * @return Numero do Cadastro Especifico do INSS (CEI) da pessoa juridica titular do certificado
	 */
	public String getCEI(){
		return (String)properties.get("CEI");
	}

}
