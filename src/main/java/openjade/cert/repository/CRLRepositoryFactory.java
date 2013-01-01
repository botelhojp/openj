package openjade.cert.repository;

/**
 * Fábrica de Repository. 
 */
public class CRLRepositoryFactory {
	
	/**
	 * Caso o Configuratiron defina o modo online será criado um repositorio OnLineCRLRepository, caso contrário
	 * será criada um repositorio OffLineCRLRepository
	 * @return Repositório de CRL
	 */
	public static CRLRepository factoryCRLRepository(){
		Configuration conf = Configuration.getInstance();
		if (conf.isOnline()){
			return new OnLineCRLRepository();
		}else{
			return new OffLineCRLRepository();
		}
	}

}
