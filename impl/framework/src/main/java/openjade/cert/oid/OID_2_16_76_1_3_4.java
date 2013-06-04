package openjade.cert.oid;

/**
 * Classe OID 2.16.76.1.3.4 <br>
 * <br>
 * Possui alguns atributos de pessoa juridica ou equipamento: <br>
 * <b>*</b> Data de nascimento do titular "DDMMAAAA" <br>
 * <b>*</b> Cadastro de pessoa fisica (CPF) do titular <br>
 * <b>*</b> Numero de Identidade Social - NIS (PIS, PASEP ou CI) <br>
 * <b>*</b> Numero do Registro Geral (RG) do titular <br>
 * <b>*</b> Sigla do orgao expedidor do RG <br>
 * <b>*</b> UF do orgao expedidor do RG <br>
 *  

 *
 */
public class OID_2_16_76_1_3_4 extends OIDGeneric {
	
	public static final String OID = "2.16.76.1.3.4";
	
	protected static final Object CAMPOS[] = {"dtNascimento",     (int) 8,
											  "cpf",              (int)11,
											  "nis",              (int)11,
											  "rg",               (int)15,
											  "orgaoUfExpedidor", (int) 6};

	public OID_2_16_76_1_3_4(){
	}
	
	public void initialize(){
		super.initialize(CAMPOS);
	}
	
	/**
	 * 
	 * @return a data de nascimento do titular
	 */	
	public String getDataNascimento(){
		return (String)properties.get("dtNascimento");
	}

	/**
	 * 
	 * @return numero do Cadastro de Pessoa Fisica (CPF) do titular;
	 */
	public String getCPF(){
		return (String)properties.get("cpf");
	}
	
	/**
	 * 
	 * @return o numero de Identificacao Social - NIS (PIS, PASEP ou CI)
	 */
	public String getNIS(){
		return (String)properties.get("nis");
	}
	
	/**
	 * 
	 * @return numero do Registro Geral - RG do titular
	 */
	public String getRg(){
		return (String)properties.get("rg");
	}
	
	/**
	 * 
	 * @return as siglas do orgao expedidor do RG
	 */
	public String getOrgaoExpedidorRg(){

		String s = (String)properties.get("orgaoUfExpedidor").trim();
		int len = s.trim().length();
		String ret = null;
		if (len > 0){
			ret = s.substring(0, len-2);
		}
		return ret;
	}
	
	/**
	 * 
	 * @return a UF do orgao expedidor do RG 
	 */
	public String getUfExpedidorRg(){
		String s = (String)properties.get("orgaoUfExpedidor").trim();
		int len = s.trim().length();
		String ret = null;
		if (len > 0){
			ret = s.substring(len-2, len);
		}
		return ret;
		
	}

}
