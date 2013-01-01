package openjade.cert.repository;

/**
 * Entidade responsavel por guardar as configuracoes necessarias ao uso do repositorio
 */
public class Configuration {

	/** Chave do System para definir modo online ou offline  */
	public static final String MODE_ONLINE = "security.certificate.repository.online";
	
	/** Chave do System para definir local de armazenamento do arquivo de index das crls  */
	public static final String CRL_INDEX = "security.certificate.repository.crl.index";

	/** Chave do System para definir local de armazenamento do arquivo de index das crls  */
	public static final String CRL_PATH = "security.certificate.repository.crl.path";
	
	private String crlIndex;
	private String crlPath;
	private boolean isOnline;

	public static Configuration instance = new Configuration();

	/**
	 * Verifica se há variavéis no System. Caso haja, seta nas variaveis de classes
	 * do contrário usa os valores padrões 
	 */
	private Configuration() {
		String mode_online = (String) System.getProperties().get(MODE_ONLINE);
		if (mode_online == null || mode_online.equals("")) {
			setOnline(true);
		} else {
			setOnline(Boolean.valueOf(mode_online));
		}
		crlIndex = (String) System.getProperties().get(CRL_INDEX);
		if (crlIndex == null || crlIndex.equals("")) {
			setCrlIndex(".crl_index");
		}
		
		crlPath = (String) System.getProperties().get(CRL_PATH);
		if (crlPath == null || crlPath.equals("")) {
			setCrlPath("/tmp/crls");
		}	
	}

	/**
	 * Retorna instância única
	 * @return
	 */
	public static Configuration getInstance() {
		return instance;
	}

	/**
	 * Retorna o local onde está armazenado o arquivo de indice de crl
	 * @return
	 */
	public String getCrlIndex() {
		return crlIndex;
	}

	/**
	 * Modificador padrão
	 * @param crlIndex
	 */
	public void setCrlIndex(String crlIndex) {
		this.crlIndex = crlIndex;
	}

	/**
	 * Retorna se o repositório está no modo online ou offline
	 * @return se true (online) se false (offline)
	 */
	public boolean isOnline() {
		return isOnline;
	}

	/**
	 * Modificador padrão
	 * @param isOnline
	 */
	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	/**
	 * Caminho onde será armazenado o repositório de CRLs
	 * @return
	 */
	public String getCrlPath() {
		return crlPath;
	}
	
	
	/**
	 * Modificador padrão
	 * @param crlPath
	 */
	public void setCrlPath(String crlPath) {
		this.crlPath = crlPath;
	}
	
	
}
