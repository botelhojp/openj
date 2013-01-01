package openjade.cert.criptography;

import jade.util.leap.*;
import jade.util.leap.Serializable;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.sun.crypto.provider.SunJCE;

public class Criptography implements Serializable {
	
	public static Criptography instance;

	private static final long serialVersionUID = 1L;
	
	private static final int MAXSIZE = 117;;
	
	private Cipher cipher;

	
	public synchronized static Criptography getInstance(){
		if (instance == null){
			instance = new Criptography(new SunJCE(),"RSA/ECB/PKCS1Padding");
		}
		return instance;
	}
	
	public synchronized static Criptography getInstance(Provider _provider, String _algorithm){
		if (instance == null){
			instance = new Criptography(_provider, _algorithm);
		}
		return instance;
	}

	private Criptography(Provider _provider, String _algorithm) {
		try {
			cipher = Cipher.getInstance(_algorithm, _provider);
		} catch (NoSuchAlgorithmException e) {
			throw new CriptographyException(e.getMessage(), e);
		} catch (NoSuchPaddingException e) {
			throw new CriptographyException(e.getMessage(), e);
		}
	}
	
	public byte[] decript(List content, Key key) {
		try {
			this.cipher.init(Cipher.DECRYPT_MODE, key);
			List results = new ArrayList();
			for (int i = 0; i < content.size(); i++) {
				results.add(this.cipher.doFinal( (byte[])content.get(i) ) );
			}
			return concatBytes(results);
		} catch (InvalidKeyException e) {
			throw new CriptographyException(e.getMessage(), e);
		} catch (IllegalBlockSizeException e) {
			throw new CriptographyException(e.getMessage(), e);
		} catch (BadPaddingException e) {
			throw new CriptographyException(e.getMessage(), e);
		}
	}


	public List cript(byte[] content, Key key) {
		try {
			this.cipher.init(Cipher.ENCRYPT_MODE, key);
			List tokens = splitBytes(content, MAXSIZE);
			List results = new ArrayList();
			for (int i = 0; i < tokens.size(); i++) {
				results.add(this.cipher.doFinal( (byte[])tokens.get(i) ));
			}
			return results;			
		} catch (InvalidKeyException e) {
			throw new CriptographyException(e.getMessage(), e);
		} catch (IllegalBlockSizeException e) {
			throw new CriptographyException(e.getMessage(), e);
		} catch (BadPaddingException e) {
			throw new CriptographyException(e.getMessage(), e);
		}
	}

	public byte[] concatBytes(List tokens) {
		if (tokens == null || tokens.size() <= 0){
			return null;
		}		
		int tam = ((tokens.size()-1) * ((byte[]) tokens.get(0)).length) + ((byte[]) tokens.get(tokens.size() - 1)).length;
		int index = 0;
		byte[] result = new byte[tam];
		
		for (int i = 0; i < tokens.size(); i++) {
			for (byte b : (byte[])tokens.get(i)) {
				result[index++] = b;
			}			
		}
		return result;
	}

	public List splitBytes(byte[] bytes, int size) {
		List result = new ArrayList();
		
		int tokens = bytes.length / size;
		int rest = bytes.length % size;
		int index = 0;
		for(int token = 0 ; token < tokens; token++){
			byte[] byteToken = new byte[size];
			for(int i = 0 ; i < size; i++){
				byteToken[i] = bytes[index++];
			}			
			result.add(byteToken);
		}
		byte[] byteToken = new byte[rest];
		for(int i = 0 ; i < rest; i++){
			byteToken[i] = bytes[index++];
		}			
		result.add(byteToken);
		return result;
	}
}
