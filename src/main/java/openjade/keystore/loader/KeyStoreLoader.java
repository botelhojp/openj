package openjade.keystore.loader;

import java.security.KeyStore;

/**
 * Especificação de um KeyStoreLoader.
 * A maioria dos providers pedem o PIN number na hora de
 * acessar o recurso.
 * Um PIN Number é a senha de acesso ao token/smart card.
 */
public interface KeyStoreLoader {
	
	public KeyStore getKeyStore(String pinNumber);

}
