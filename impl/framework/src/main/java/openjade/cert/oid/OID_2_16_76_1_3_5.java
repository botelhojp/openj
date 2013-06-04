package openjade.cert.oid;

/**
 * Classe OID 2.16.76.1.3.5 <br>
 * <br>
 * Possui alguns atributos de pessoa fisica: <br>
 * <b>*</b> Numero de inscricao do Titulo de Eleitor <br>
 * <b>*</b> Zona Eleitoral <br>
 * <b>*</b> Secao <br>
 * <b>*</b> Municipio do titulo <br>
 * <b>*</b> UF do titulo <br>
 * 

 *
 */
public class OID_2_16_76_1_3_5 extends OIDGeneric {
	

	public static final String OID = "2.16.76.1.3.5";
	
	protected static final Object CAMPOS[] = {"titulo",      (int)12,
		                                      "zona",        (int) 3,
		                                      "secao",       (int) 4,
		                                      "municipioUf", (int)22};

	public OID_2_16_76_1_3_5(){
	}
	
	public void initialize(){
		
		super.initialize(CAMPOS);
		
	}
	/**
	 * 
	 * @return String de 12 posicoes com o numero do Titulo de eleitor
	 */
	public String getTitulo(){
		return (String)properties.get("titulo");
	}
	
	/**
	 * 
	 * @return String de 3 posicoes com o numero da zona eleitoral
	 */
	public String getZona(){
		return (String)properties.get("zona");
	}
	
	/**
	 * 
	 * @return String de 4 posicoes com o numero da secao eleitoral
	 */
	public String getSecao(){
		return (String)properties.get("secao");
	}
	
	/**
	 * 
	 * @return String com o nome do municipio
	 */
	public String getMunicipioTitulo(){
		String s = (String)properties.get("municipioUf").trim();
		int len = s.trim().length();
		String ret = null;
		if (len > 0){
			ret = s.substring(0, len-2);
		}
		return ret;

	}
	
	/**
	 * 
	 * @return String com a UF correspondente.
	 */
	public String getUFTitulo(){
		String s = (String)properties.get("municipioUf").trim();
		int len = s.trim().length();
		String ret = null;
		if (len > 0){
			ret = s.substring(len-2, len);
		}
		return ret;
	}
	
}
