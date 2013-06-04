package openjade.signer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.security.KeyStore;

import junit.framework.Assert;
import openjade.cert.bean.CertificateBean;
import openjade.keystore.loader.KeyStoreLoader;
import openjade.keystore.loader.implementation.KeyStoreLoaderImpl;

import org.bouncycastle.util.encoders.Base64;
import org.junit.Test;

public class PKCS7SignerTest {

	protected final String keyStorePassword = "123456";

	@Test
	public void testSigner() throws Exception {
		InputStream is = PKCS7SignerTest.class.getResourceAsStream("/certs/cert_agent_00001.pfx");
		KeyStoreLoader storeLoader = new KeyStoreLoaderImpl(is);
		KeyStore store = storeLoader.getKeyStore("123456");
		
		PKCS7Signer signer = new PKCS7Signer(store, keyStorePassword);
		byte[] signedBytes = signer.sign("hi open jade");
		
		//verify signature
		String result = new String(Base64.encode(signedBytes));
		assertEquals(2728, result.length());		
		assertTrue(signer.verify(signedBytes));

		//verify data
		PKCS7Reader reader = new PKCS7Reader(signedBytes);
		Assert.assertEquals("hi open jade", reader.getDataToString());

		//verify certificate
		
		CertificateBean cb = reader.getCertificateBean();		
		Assert.assertEquals("http://openjade.org/lcr/acopenjade.crl", cb.getCrlURL().get(0));
		Assert.assertEquals("00000000001", cb.getPersonID());
		Assert.assertEquals("agent_00001", cb.getAgentID());

		Assert.assertEquals("agent@openjade.org", cb.getEmail());
		Assert.assertEquals("Leonardo Da Vinci", cb.getPersonName());
		Assert.assertEquals(1, cb.getCrlURL().size());

	}
}
