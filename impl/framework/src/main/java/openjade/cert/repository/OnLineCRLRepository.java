package openjade.cert.repository;

import java.io.File;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.List;

import openjade.cert.CertificateValidatorException;
import openjade.cert.extension.BasicCertificate;
import openjade.cert.extension.ICPBR_CRL;
import openjade.cert.util.RepositoryUtil;



/**
 * Representa um repositório online.
 * Neste caso não ha necessidade de um serviço para atualização das CRL.
 * O Repositório deve primeiramente buscar a arquivo no file system, caso o mesmo
 * não se encontre ou ja esteja expirado ele obterá a CRL a partir de sua URL.
 */
public class OnLineCRLRepository implements CRLRepository {
	
	private OffLineCRLRepository repositoryOff;
	
	/**
	 * Construtor padrão
	 */
	public OnLineCRLRepository(){
		repositoryOff = new OffLineCRLRepository();		
	}

	public Collection<ICPBR_CRL> getX509CRL(X509Certificate certificate) {
		
		try {
			return repositoryOff.getX509CRL(certificate);
		} catch (CRLFileNotFoundException e) {
			try {
				updateRepository(certificate);
				return repositoryOff.getX509CRL(certificate);
			} catch (Throwable t) {
				throw new CRLRepositoryException("Error on getX509CRL", t);
			}
		} catch (CRLOldCertificateException e) {
			try {
				updateRepository(certificate);
				return repositoryOff.getX509CRL(certificate);
			} catch (Throwable t) {
				throw new CRLRepositoryException("Error on getX509CRL", t);
			}
		}
	}

	/**
	 * Atualiza o arquivo de indice e obtem do arquivos CRL do certificado
	 * @param X509Certificate
	 */
	private void updateRepository(X509Certificate certificate) {
		Configuration config = Configuration.getInstance();
		List<String> ListaURLCRL = getListaURLCRL(certificate);
		for (String URLCRL : ListaURLCRL) {
			repositoryOff.addFileIndex(URLCRL);
			File fileCLR = new File(config.getCrlPath(), RepositoryUtil.urlToMD5(URLCRL));
			RepositoryUtil.saveURL(URLCRL, fileCLR);
		}
	}

	/**
	 * Retorna todas as urls de CRL de um determiando certificado
	 * @param certificate
	 * @return
	 */
	public List<String> getListaURLCRL(X509Certificate certificate) {
		BasicCertificate cert = new BasicCertificate(certificate);
		List<String> ListaURLCRL;
		try {
			ListaURLCRL = cert.getCRLDistributionPoint();
		} catch (IOException e1) {
			throw new CertificateValidatorException("Could not get the CRL List from Certificate " + e1);
		}
		return ListaURLCRL;
	}

	


}
