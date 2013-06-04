package openjade.cert.repository;

import java.security.cert.X509Certificate;
import java.util.Collection;

import openjade.cert.extension.ICPBR_CRL;



/**
 * Representa um repositorio de CRL 
 */
public interface CRLRepository {
	
	/**
	 * Retorna uma lista de CRL para IPC Brasil para de determinado certificado
	 * @param certificate Certificado
	 * @return Lista de ICPBR_CRL
	 */
	public Collection<ICPBR_CRL> getX509CRL(X509Certificate certificate);

}
