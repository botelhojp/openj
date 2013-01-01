package openjade.cert.criptography;

import static org.junit.Assert.assertEquals;

import jade.util.leap.ArrayList;
import jade.util.leap.List;

import java.security.cert.X509Certificate;

import openjade.util.OpenJadeTestUtil;

import org.junit.Test;

public class CriptographyTest extends OpenJadeTestUtil {

	@Test
	public void testCryptPublicDecipherPrivate() {
		String content = "Hi OpenJade!";
		X509Certificate cert = getX509("cert_agent_00001.pfx");
		Criptography cript = Criptography.getInstance();
		
		List encode = cript.cript(content.getBytes(), cert.getPublicKey());
		byte[] decode = cript.decript(encode, getPrivateKey());
		
		assertEquals(content, new String(decode));
	}
	
	@Test
	public void testCrypPrivatetDecipherPublic() {
		String content = "Hi OpenJade!";
		X509Certificate cert = getX509("cert_agent_00001.pfx");
		Criptography cript = Criptography.getInstance();
		
		List encode = cript.cript(content.getBytes(), getPrivateKey());
		byte[] decode = cript.decript(encode, cert.getPublicKey());
		
		assertEquals(content, new String(decode));
	}
	
	@Test
	public void testSplitBytes1(){
		Criptography cript = Criptography.getInstance();
		List result = cript.splitBytes("1234567".getBytes(), 7);
		assertEquals("1234567", new String((byte[])result.get(0)));
	}
	
	@Test
	public void testSplitBytes2(){
		Criptography cript = Criptography.getInstance();
		List result = cript.splitBytes("1234567".getBytes(), 300);
		assertEquals("1234567", new String((byte[])result.get(0)));
	}

	
	@Test
	public void testSplitBytes3(){
		Criptography cript = Criptography.getInstance();
		List result = cript.splitBytes("1234567".getBytes(), 3);
		assertEquals("123", new String((byte[])result.get(0)));
		assertEquals("456", new String((byte[])result.get(1)));
		assertEquals("7", new String((byte[])result.get(2)));
	}

	
	@Test
	public void testCrypPrivatetDecipherPublic128() {
		String content = ".Hi OpenJade! Hi OpenJade! Hi OpenJade! Hi OpenJade! Hi OpenJade! Hi OpenJade! Hi OpenJade! Hi OpenJade! Hi OpenJade! ";
		System.out.println(content.length());
		System.out.println(content.getBytes().length);
		X509Certificate cert = getX509("cert_agent_00001.pfx");
		Criptography cript = Criptography.getInstance();
		
		List encode = cript.cript(content.getBytes(), getPrivateKey());
		byte[] decode = cript.decript(encode, cert.getPublicKey());
		
		assertEquals(content, new String(decode));
	}
	
	@Test
	public void testConcatBytes() {
		Criptography cript = Criptography.getInstance();
		List tokens = new ArrayList();
		tokens.add("ab".getBytes());
		tokens.add("cd".getBytes());
		byte[] result = cript.concatBytes(tokens);
		assertEquals("abcd", new String(result));
	}

	
	


}
