package openjade.signer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.security.cert.CertificateExpiredException;
import javax.security.cert.CertificateNotYetValidException;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSSignedDataParser;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.CMSTypedStream;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;

public class PKCS7Signer {

	private static final String SIGNATUREALGO = "SHA1withRSA";
	private KeyStore keystore;
	private String alias;
	protected String password;

	public PKCS7Signer(KeyStore keystore, String password) {
		this.keystore = keystore;
		this.password = password;
	}

	private CMSSignedDataGenerator setUpProvider() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		this.alias = (String) keystore.aliases().nextElement();
		Certificate[] certchain = (Certificate[]) keystore.getCertificateChain(alias);
		final List<Certificate> certlist = new ArrayList<Certificate>();
		for (int i = 0, length = certchain == null ? 0 : certchain.length; i < length; i++) {
			certlist.add(certchain[i]);
		}
		Store certstore = new JcaCertStore(certlist);
		Certificate cert = keystore.getCertificate(alias);
		ContentSigner signer = new JcaContentSignerBuilder(SIGNATUREALGO).setProvider("BC")
				.build((PrivateKey) (keystore.getKey(alias, password.toCharArray())));
		CMSSignedDataGenerator generator = new CMSSignedDataGenerator();
		generator.addSignerInfoGenerator(
				new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().setProvider("BC").build())
						.build(signer, (X509Certificate) cert));
		generator.addCertificates(certstore);
		return generator;
	}

	public byte[] signPkcs7(final byte[] content) {
		try {
			CMSSignedDataGenerator signatureGenerator = setUpProvider();
			CMSTypedData cmsdata = new CMSProcessableByteArray(content);
			CMSSignedData signeddata = signatureGenerator.generate(cmsdata, true);
			return signeddata.getEncoded();
		} catch (Exception e) {
			throw new SignerException(e);
		}
	}

	public byte[] sign(String content) throws Exception {
		return signPkcs7(content.getBytes("UTF-8"));
	}

	public boolean verify(byte[] signedBytes) {
		return verify(signedBytes, null);
	}

	@SuppressWarnings("all")
	public boolean verify(byte[] signedBytes, String agentID) {
		boolean verify = true;
		try {
			CMSSignedDataParser sp = new CMSSignedDataParser(
					new JcaDigestCalculatorProviderBuilder().setProvider("BC").build(), signedBytes);
			sp.getSignedContent().drain();
			Store certStore = sp.getCertificates();
			SignerInformationStore signers = sp.getSignerInfos();
			Collection c = signers.getSigners();
			Iterator it = c.iterator();
			while (it.hasNext()) {
				SignerInformation signer2 = (SignerInformation) it.next();
				Collection certCollection = certStore.getMatches(signer2.getSID());
				Iterator certIt = certCollection.iterator();
				X509CertificateHolder cert = (X509CertificateHolder) certIt.next();
				if (!signer2.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(cert))) {
					verify = false;
				}
				if (agentID != null && !cert.getSubject().toString().contains(":" + agentID)) {
					verify = false;
				}
			}
		} catch (OperatorCreationException e) {
			throw new SignerException(e);
		} catch (CMSException e) {
			throw new SignerException(e);
		} catch (IOException e) {
			throw new SignerException(e);
		} catch (java.security.cert.CertificateException e) {
			throw new SignerException(e);
		}
		return verify;
	}

	public void verifySign(byte[] signedData, byte[] bPlainText) throws Exception {
		InputStream is = new ByteArrayInputStream(bPlainText);
		CMSSignedDataParser sp = new CMSSignedDataParser(new CMSTypedStream(is), signedData);
		CMSTypedStream signedContent = sp.getSignedContent();

		signedContent.drain();

		// CMSSignedData s = new CMSSignedData(signedData);
		Store certStore = sp.getCertificates();

		SignerInformationStore signers = sp.getSignerInfos();
		Collection c = signers.getSigners();
		Iterator it = c.iterator();
		while (it.hasNext()) {
			SignerInformation signer = (SignerInformation) it.next();
			Collection certCollection = certStore.getMatches(signer.getSID());

			Iterator certIt = certCollection.iterator();

			X509CertificateHolder certHolder = (X509CertificateHolder) certIt.next();

			if (!signer.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(certHolder))) {
				throw new RuntimeException("Verification FAILED! ");

			} else {
			}

		}
	}

}
