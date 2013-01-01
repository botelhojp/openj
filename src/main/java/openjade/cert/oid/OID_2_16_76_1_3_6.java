package openjade.cert.oid;
/**
 * Classe OID 2.16.76.1.3.6 <br>
 * <br>
 * Possui alguns atributos de pessoa fisica: <br>
 * <b>*</b> Numero do Cadastro Especifico do INSS (CEI) da pessoa fisica titular do certificado <br>
 * 

 */
public class OID_2_16_76_1_3_6 extends OIDGeneric {
	
	public static final String OID = "2.16.76.1.3.6";
	
	protected static final Object CAMPOS[] = {"CEI", (int)12};

	public OID_2_16_76_1_3_6(){
	}

	public void initialize(){
		super.initialize(CAMPOS);
	}
	
	/**
	 * 
	 * @return string de 12 posicoes com o numero do cadastro no INSS(CEI).
	 */
	public String getCEI(){
		return (String)properties.get("CEI");
	}

}
