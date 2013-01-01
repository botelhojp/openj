package openjade.cert;

import java.security.cert.X509Certificate;

import junit.framework.Assert;

import openjade.cert.bean.CertificateBean;
import openjade.util.OpenJadeTestUtil;

import org.junit.Test;

public class CertificateManagerTest extends OpenJadeTestUtil {
	
	

	@Test
	public void testLoadClassOfT() {
		X509Certificate cert = getX509("cert_agent_00001.pfx");
		CertificateManager cm = new CertificateManager(cert, false);
		CertificateBean cb = new CertificateBean();
		cm.load(cb);
		Assert.assertEquals("http://openjade.org/lcr/acopenjade.crl", cb.getCrlURL().get(0));
		Assert.assertEquals("agent_00001", cb.getAgentID());
		Assert.assertEquals("00000000001", cb.getPersonID());		
		Assert.assertEquals("agent@openjade.org", cb.getEmail());
		Assert.assertEquals("Leonardo Da Vinci", cb.getPersonName());
		Assert.assertEquals(1, cb.getCrlURL().size());
	}
	
	@Test
	public void testLoadObject() {
		X509Certificate cert = getX509("cert_agent_00001.pfx");
		CertificateManager cm = new CertificateManager(cert, false);
		CertificateBean cb = cm.load(CertificateBean.class);
		Assert.assertEquals("http://openjade.org/lcr/acopenjade.crl", cb.getCrlURL().get(0));
		Assert.assertEquals("agent_00001", cb.getAgentID());
		Assert.assertEquals("00000000001", cb.getPersonID());		
		Assert.assertEquals("agent@openjade.org", cb.getEmail());
		Assert.assertEquals("Leonardo Da Vinci", cb.getPersonName());
		Assert.assertEquals(1, cb.getCrlURL().size());
		
	}

}
