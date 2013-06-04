package openjade.cert.extension;

import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import openjade.cert.oid.OIDGeneric;
import openjade.cert.oid.OID_0_16_76_1_3_1;
import openjade.cert.oid.OID_2_16_76_1_3_1;
import openjade.cert.oid.OID_2_16_76_1_3_2;
import openjade.cert.oid.OID_2_16_76_1_3_3;
import openjade.cert.oid.OID_2_16_76_1_3_4;
import openjade.cert.oid.OID_2_16_76_1_3_5;
import openjade.cert.oid.OID_2_16_76_1_3_6;
import openjade.cert.oid.OID_2_16_76_1_3_7;
import openjade.cert.oid.OID_2_16_76_1_3_8;


/**
 * Class Certificate Extra <br>
 * <br>
 * 
 * Extra Informations for ICP-BRASIL (DOC-ICP-04) Certificates.
 * Abstracts the rules to PESSOA FISICA, PESSOA JURIDICA and EQUIPAMENTO/APLICAÇÃO 
 * 

*/

public class CertificateExtra {
	
	private static final Integer ZERO = new Integer(0);
	private static final Integer UM = new Integer(1);
	
	private String email = null;
	private Map<String, OIDGeneric> extras = new HashMap<String, OIDGeneric>();

	/**
	 *  
	 * @param certificate
	 */
	public CertificateExtra(X509Certificate certificate) {
		try {
			if(certificate.getSubjectAlternativeNames() == null) {
				return;
			}
			for (List<?> list : certificate.getSubjectAlternativeNames()){
				if(list.size() != 2) {
					throw new Exception("the size of extra informations on certificate is not correct.");
				}
				
				Object e1, e2;
				
				e1 = list.get(0);
				e2 = list.get(1);
				
				if(! (e1 instanceof Integer)) {
					throw new Exception("Is not java.lang.Integer type.");
				}
				
				Integer tipo = (Integer)e1;

				if(tipo.equals(ZERO)) {
					byte[] data = (byte[]) e2;
					OIDGeneric oid = OIDGeneric.getInstance(data);
					extras.put(oid.getOid(), oid);
				} else if(tipo.equals(UM)) {
					email = (String)e2;
				}
			}
		} catch (CertificateParsingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Check if it is "ICP-BRASIL Pessoa Fisica" Certificate
	 * 
	 */
	public boolean isCertificatePF(){
		return extras.get("2.16.76.1.3.1") != null;
	}
	
	/**
	 * Check if it is "ICP-BRASIL Pessoa Juridica" Certificate
	 * 
	 */
	public boolean isCertificatePJ(){
		return extras.get("2.16.76.1.3.7") != null;
	}
	
	/**
	 * Check if it is "ICP-BRASIL Equipment" Certificate
	 * 
	 */
	public boolean isCertificateEquipment(){
		return extras.get("2.16.76.1.3.8") != null;
	}
	
	/**
	 * Class OID 2.16.76.1.3.1 <br>
	 * <br>
	 * Has some "ICP-BRASIL Pessoa Fisica"  attributes<br>
	 * <b>*</b> Data de nascimento do titular "DDMMAAAA" <br>
	 * <b>*</b> Cadastro de pessoa fisica (CPF) do titular <br>
	 * <b>*</b> Numero de Identidade Social - NIS (PIS, PASEP ou CI) <br>
	 * <b>*</b> Numero do Registro Geral (RG) do titular <br>
	 * <b>*</b> Sigla do orgao expedidor do RG <br>
	 * <b>*</b> UF do orgao expedidor do RG <br>
	 * 
	 * @return OID_2_16_76_1_3_1
	 */
	public OID_2_16_76_1_3_1 getOID_2_16_76_1_3_1(){
		return (OID_2_16_76_1_3_1)extras.get("2.16.76.1.3.1");
	}
	
	/**
	 * Class OID 2.16.76.1.3.5 <br>
	 * <br>
	 * Has some "ICP-BRASIL Fisica"  attributes<br>
	 * <b>*</b> Numero de inscricao do Titulo de Eleitor <br>
	 * <b>*</b> Zona Eleitoral <br>
	 * <b>*</b> Secao <br>
	 * <b>*</b> Municipio do titulo <br>
	 * <b>*</b> UF do titulo <br>
	 * 
	 * @return OID_2_16_76_1_3_5
	 */
	public OID_2_16_76_1_3_5 getOID_2_16_76_1_3_5(){
		return (OID_2_16_76_1_3_5)extras.get("2.16.76.1.3.5");
	}

	/**
	 * Class OID 2.16.76.1.3.6 <br>
	 * <br>
	 * Has some "ICP-BRASIL Pessoa Fisica"  attributes<br>
	 * <b>*</b> Numero do Cadastro Especifico do INSS (CEI) da pessoa fisica titular do certificado <br>
	 * 
	 * @return OID_2_16_76_1_3_6
	 */
	public OID_2_16_76_1_3_6 getOID_2_16_76_1_3_6(){
		return (OID_2_16_76_1_3_6)extras.get("2.16.76.1.3.6");
	}
	
	/**
	 * Class OID 2.16.76.1.3.2 <br>
	 * <br>
	 * Has some "ICP-BRASIL Pessoa Juridica and Equipment"  attributes<br>
	 * <b>*</b> Nome do responsavel pelo certificado <br>
	 * 
	 * @return OID_2_16_76_1_3_2
	 */
	public OID_2_16_76_1_3_2 getOID_2_16_76_1_3_2(){
		return (OID_2_16_76_1_3_2)extras.get("2.16.76.1.3.2");
	}
	
	/**
	 * Class OID 2.16.76.1.3.3 <br>
	 * <br>
	 * Has some "ICP-BRASIL Pessoa Juridica and Equipment"  attributes<br>
	 * <b>*</b> Cadastro Nacional de Pessoa Juridica (CNPJ) da pessoa juridica titular do certificado <br>
	 * 
	 * @return OID_2_16_76_1_3_3
	 */
	public OID_2_16_76_1_3_3 getOID_2_16_76_1_3_3(){
		return (OID_2_16_76_1_3_3)extras.get("2.16.76.1.3.3");
	}
	
	/**
	 * Class OID 2.16.76.1.3.4 <br>
	 * <br>
	 * Has some "ICP-BRASIL Pessoa Juridica and Equipment"  attributes<br>
	 * <b>*</b> Data de nascimento do titular "DDMMAAAA" <br>
	 * <b>*</b> Cadastro de pessoa fisica (CPF) do titular <br>
	 * <b>*</b> Numero de Identidade Social - NIS (PIS, PASEP ou CI) <br>
	 * <b>*</b> Numero do Registro Geral (RG) do titular <br>
	 * <b>*</b> Sigla do orgao expedidor do RG <br>
	 * <b>*</b> UF do orgao expedidor do RG <br>
	 * 
	 * @return OID_2_16_76_1_3_4
	 */
	public OID_2_16_76_1_3_4 getOID_2_16_76_1_3_4(){
		return (OID_2_16_76_1_3_4)extras.get("2.16.76.1.3.4");
	}
	
	/**
	 * Class OID 2.16.76.1.3.7 <br>
	 * <br>
	 * Has some "ICP-BRASIL Pessoa Juridica"  attributes<br>
	 * <b>*</b> Numero do Cadastro Especifico do INSS (CEI) da pessoa juridica titular do certificado <br>
	 * 
	 * @return OID_2_16_76_1_3_7
	 */
	public OID_2_16_76_1_3_7 getOID_2_16_76_1_3_7(){
		return (OID_2_16_76_1_3_7)extras.get("2.16.76.1.3.7");
	}
	
	/**
	 * Class OID 2.16.76.1.3.8 <br>
	 * <br>
	 * Has some "ICP-BRASIL Equipment"  attributes<br>
	 * <b>*</b> Nome empresarial constante do Cadastro Nacional de Pessoa Juridica (CNPJ),
	 * sem abreviacoes, se o certificado for de pessoa juridica<br>
	 * 
	 * @return OID_2_16_76_1_3_8
	 */
	public OID_2_16_76_1_3_8 getOID_2_16_76_1_3_8(){
		return (OID_2_16_76_1_3_8)extras.get("2.16.76.1.3.8");
	}
	
	public OID_0_16_76_1_3_1 getOID_0_16_76_1_3_1(){
		return (OID_0_16_76_1_3_1)extras.get(OID_0_16_76_1_3_1.OID);
	}

	
	/**
	 * 
	 * @return the e-mail for certificate.
	 */
	public String getEmail(){
		return email;
	}

}
