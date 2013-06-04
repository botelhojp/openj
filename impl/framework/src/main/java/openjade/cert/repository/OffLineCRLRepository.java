package openjade.cert.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import openjade.cert.CertificateValidatorException;
import openjade.cert.extension.BasicCertificate;
import openjade.cert.extension.ICPBR_CRL;
import openjade.cert.util.RepositoryUtil;



/**
 * Implementação de um repositorio Offline.
 * Neste caso apenas o file system será utilizado para recuperar as arquivos CRL.
 * Recomenda-se neste caso de haja algum serviço atualizando constantemente estas CRLs
 */
public class OffLineCRLRepository implements CRLRepository {

	private Configuration config;

	/**
	 * Construtor padrão
	 */
	public OffLineCRLRepository() {
		config = Configuration.getInstance();
	}

	/**
	 * @see CRLRepository
	 */
	public Collection<ICPBR_CRL> getX509CRL(X509Certificate certificate) {

		Collection<ICPBR_CRL> list = new ArrayList<ICPBR_CRL>();
		BasicCertificate cert = new BasicCertificate(certificate);
		List<String> ListaURLCRL;

		try {
			ListaURLCRL = cert.getCRLDistributionPoint();
		} catch (IOException e1) {
			throw new CRLRepositoryException("Could not get the CRL List from Certificate " + e1);
		}

		for (String URLCRL : ListaURLCRL) {
			ICPBR_CRL crl = getICPBR_CRL(URLCRL);
			list.add(crl);			
		}

		return list;
	}

	/**
	 * Retorna uma instância de ICPBR_CRL para uma determinada URL.
	 * @param URLCRL
	 * @return
	 */
	@SuppressWarnings("unused")
	private ICPBR_CRL getICPBR_CRL(String URLCRL) {		
		String fileNameCRL =  RepositoryUtil.urlToMD5(URLCRL);		
		File fileCRL = new File(config.getCrlPath(), fileNameCRL.toString());
		if (fileCRL.exists()) {
			ICPBR_CRL crl = null;
			try {
				crl = new ICPBR_CRL(new FileInputStream(fileCRL));
				if (crl != null) {
					if (crl.getCRL().getNextUpdate().before(new Date())) {
						throw new CRLOldCertificateException("CRL is old");
					} else {
						return crl;
					}
				}
				throw new CRLFileNotFoundException();
				
			} catch (FileNotFoundException e) {
				addFileIndex(URLCRL);
				throw new CRLFileNotFoundException("File [" + fileNameCRL + "]is not found");
			} catch (CRLException e) {
				addFileIndex(URLCRL);
				throw new CRLFileNotFoundException("File [" + fileNameCRL + "]is not found");
			} catch (CertificateException e) {
				addFileIndex(URLCRL);
				throw new CRLFileNotFoundException("File [" + fileNameCRL + "]is not found");
			}
		}else{
			addFileIndex(URLCRL);
			throw new CRLFileNotFoundException("File [" + fileNameCRL + "]is not found");
		}
	}

	/**
	 * Quando o arquivo crl não encontra-se no repositorio local, deve-se
	 * cadastrador no arquivo de indice.
	 * @param url
	 */
	public void addFileIndex(String url) {
		String fileNameCRL =  RepositoryUtil.urlToMD5(url);
		File fileIndex = new File(config.getCrlPath() , config.getCrlIndex());
		if (!fileIndex.exists()) {
			try {
				File diretory = new File(config.getCrlPath());
				diretory.mkdirs();
				fileIndex.createNewFile();
			} catch (Exception e) {
				throw new CertificateValidatorException("Error creating index file " + fileIndex, e);
			}
		}
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(fileIndex));
		} catch (Exception e) {
			throw new CertificateValidatorException("Error on load index file " + fileIndex, e);
		}
		prop.put(fileNameCRL.toString(), url);
		try {
			prop.store(new FileOutputStream(fileIndex), null);
		} catch (Exception e) {
			throw new CertificateValidatorException("Error on load index file " + fileIndex, e);
		}
	}
}
