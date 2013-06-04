package openjade.certbuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBMPString;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.DigestInfo;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.PolicyInformation;
import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.TBSCertificateStructure;
import org.bouncycastle.asn1.x509.Time;
import org.bouncycastle.asn1.x509.V3TBSCertificateGenerator;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509ExtensionsGenerator;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.jce.PrincipalUtil;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle.jce.provider.X509CertificateObject;
import org.bouncycastle.x509.X509V2CRLGenerator;
import org.bouncycastle.x509.extension.SubjectKeyIdentifierStructure;


public class X509CertificateBuilder {
	/** Our log4j logger. */
	private static Logger logger = Logger.getLogger(X509CertificateBuilder.class);
	
	private static final String CA_KEYSTORE_PASSWORD = "123456";
	
	private String OID_2_16_76_1_3_1 = "2.16.76.1.3.1";
	private String OID_2_16_76_1_3_6 = "2.16.76.1.3.6";
	private String OID_2_16_76_1_3_5 = "2.16.76.1.3.5";
	private String OID_0_16_76_1_3_1 = "0.16.76.1.3.1";
	private String OID_2_16_76_1_3_3 = "2.16.76.1.3.3";

	/**
	 * This holds the certificate of the CA used to sign the new certificate.
	 * The object is created in the constructor.
	 */
	private X509Certificate caCert;
	/**
	 * This holds the private key of the CA used to sign the new certificate.
	 * The object is created in the constructor.
	 */
	private RSAPrivateCrtKeyParameters caPrivateKey;

	private boolean useBCAPI;

	private String personID;
	
	private String agentID;

	private String personEmail;

	private String url;

	public X509CertificateBuilder(InputStream caKeystore, String caKeystoreAlias, boolean useBCAPI, String personID, String _agentID, String personEmail, String url) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException, InvalidKeyException, NoSuchProviderException, SignatureException {
		this.useBCAPI = useBCAPI;
		this.personID = personID;
		this.agentID = _agentID;
		
		this.personEmail = personEmail;
		
		this.url = url;

		logger.debug("Loading CA certificate and private key from file '" + caKeystore + "', using alias '" + caKeystoreAlias + "' with " + (this.useBCAPI ? "Bouncycastle lightweight API" : "JCE API"));
		KeyStore caKs = KeyStore.getInstance("PKCS12");
		caKs.load(caKeystore, CA_KEYSTORE_PASSWORD.toCharArray());

		// load the key entry from the keystore
		Key key = caKs.getKey(caKeystoreAlias, CA_KEYSTORE_PASSWORD.toCharArray());
		if (key == null) {
			throw new CertBuilderException("Got null key from keystore!");
		}
		RSAPrivateCrtKey privKey = (RSAPrivateCrtKey) key;
		caPrivateKey = new RSAPrivateCrtKeyParameters(privKey.getModulus(), privKey.getPublicExponent(), privKey.getPrivateExponent(), privKey.getPrimeP(), privKey.getPrimeQ(), privKey.getPrimeExponentP(), privKey.getPrimeExponentQ(), privKey.getCrtCoefficient());
		// and get the certificate
		caCert = (X509Certificate) caKs.getCertificate(caKeystoreAlias);
		if (caCert == null) {
			throw new CertBuilderException("Got null cert from keystore!");
		}
		logger.debug("Successfully loaded CA key and certificate. CA DN is '" + caCert.getSubjectDN().getName() + "'");
		caCert.verify(caCert.getPublicKey());
		logger.debug("Successfully verified CA certificate with its own public key.");
	}

	@SuppressWarnings("resource")
	public boolean createCertificate(String dn, int validityDays, String exportFile, String exportPassword) throws IOException, InvalidKeyException, SecurityException, SignatureException, NoSuchAlgorithmException, DataLengthException, CryptoException, KeyStoreException, NoSuchProviderException, CertificateException, InvalidKeySpecException {
		logger.debug("Generating certificate for distinguished subject name '" + dn + "', valid for " + validityDays + " days");
		SecureRandom sr = new SecureRandom();

		PublicKey pubKey;
		PrivateKey privKey;

		logger.debug("Creating RSA keypair");
		// generate the keypair for the new certificate
		if (useBCAPI) {
			RSAKeyPairGenerator gen = new RSAKeyPairGenerator();
			gen.init(new RSAKeyGenerationParameters(BigInteger.valueOf(3), sr, 1024, 80));
			AsymmetricCipherKeyPair keypair = gen.generateKeyPair();
			logger.debug("Generated keypair, extracting components and creating public structure for certificate");
			RSAKeyParameters publicKey = (RSAKeyParameters) keypair.getPublic();
			RSAPrivateCrtKeyParameters privateKey = (RSAPrivateCrtKeyParameters) keypair.getPrivate();
			// used to get proper encoding for the certificate
			RSAPublicKeyStructure pkStruct = new RSAPublicKeyStructure(publicKey.getModulus(), publicKey.getExponent());
			logger.debug("New public key is '" + new String(Hex.encodeHex(pkStruct.getEncoded())) + ", exponent=" + publicKey.getExponent() + ", modulus=" + publicKey.getModulus());
			// JCE format needed for the certificate - because getEncoded() is
			// necessary...
			pubKey = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(publicKey.getModulus(), publicKey.getExponent()));
			// and this one for the KeyStore
			privKey = KeyFactory.getInstance("RSA").generatePrivate(new RSAPrivateCrtKeySpec(publicKey.getModulus(), publicKey.getExponent(), privateKey.getExponent(), privateKey.getP(), privateKey.getQ(), privateKey.getDP(), privateKey.getDQ(), privateKey.getQInv()));
		} else {
			// this is the JSSE way of key generation
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(1024, sr);
			KeyPair keypair = keyGen.generateKeyPair();
			privKey = keypair.getPrivate();
			pubKey = keypair.getPublic();
		}

		Calendar expiry = Calendar.getInstance();
		expiry.add(Calendar.DAY_OF_YEAR, validityDays);

		X509Name x509Name = new X509Name("CN=" + dn + " , OU=Open Jade , OU=Open Jade A3 , OU=Open Jade");

		V3TBSCertificateGenerator certGen = new V3TBSCertificateGenerator();
		certGen.setSerialNumber(new DERInteger(BigInteger.valueOf(System.currentTimeMillis() ^ 2)));
		certGen.setIssuer(PrincipalUtil.getSubjectX509Principal(caCert));
		certGen.setSubject(x509Name);

		DERObjectIdentifier sigOID = X509Functions.getAlgorithmOID("SHA1WithRSAEncryption");
		AlgorithmIdentifier sigAlgId = new AlgorithmIdentifier(sigOID, new DERNull());
		certGen.setSignature(sigAlgId);

		certGen.setSubjectPublicKeyInfo(new SubjectPublicKeyInfo((ASN1Sequence) new ASN1InputStream(new ByteArrayInputStream(pubKey.getEncoded())).readObject()));
		certGen.setStartDate(new Time(new Date(System.currentTimeMillis())));
		certGen.setEndDate(new Time(expiry.getTime()));

		certGen.setExtensions(getCertGen());

		logger.debug("Certificate structure generated, creating SHA1 digest");
		// attention: hard coded to be SHA1+RSA!
		SHA1Digest digester = new SHA1Digest();
		AsymmetricBlockCipher rsa = new PKCS1Encoding(new RSAEngine());

		TBSCertificateStructure tbsCert = certGen.generateTBSCertificate();

		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		DEROutputStream dOut = new DEROutputStream(bOut);
		dOut.writeObject(tbsCert);
		dOut.close();

		// and now sign
		byte[] signature;
		if (useBCAPI) {
			byte[] certBlock = bOut.toByteArray();
			// first create digest
			logger.debug("Block to sign is '" + new String(Hex.encodeHex(certBlock)) + "'");
			digester.update(certBlock, 0, certBlock.length);
			byte[] hash = new byte[digester.getDigestSize()];
			digester.doFinal(hash, 0);
			// and sign that
			rsa.init(true, caPrivateKey);
			DigestInfo dInfo = new DigestInfo(new AlgorithmIdentifier(X509ObjectIdentifiers.id_SHA1, null), hash);
			byte[] digest = dInfo.getEncoded(ASN1Encodable.DER);
			signature = rsa.processBlock(digest, 0, digest.length);
		} else {
			// or the JCE way
			PrivateKey caPrivKey = KeyFactory.getInstance("RSA").generatePrivate(new RSAPrivateCrtKeySpec(caPrivateKey.getModulus(), caPrivateKey.getPublicExponent(), caPrivateKey.getExponent(), caPrivateKey.getP(), caPrivateKey.getQ(), caPrivateKey.getDP(), caPrivateKey.getDQ(), caPrivateKey.getQInv()));

			Signature sig = Signature.getInstance(sigOID.getId());
			sig.initSign(caPrivKey, sr);
			sig.update(bOut.toByteArray());
			signature = sig.sign();
		}
		logger.debug("SHA1/RSA signature of digest is '" + new String(Hex.encodeHex(signature)) + "'");

		// and finally construct the certificate structure
		ASN1EncodableVector v = new ASN1EncodableVector();

		v.add(tbsCert);
		v.add(sigAlgId);
		v.add(new DERBitString(signature));

		// v.add(pf);

		// 1.3.6.1.4.1.311.20.2.3

		// ASN1Encodable ab = new DERObjectIdentifier("2.16.76.1.3.1");
		// ab.
		// ASN1Encodable[] novo = {ab};

		X509CertificateStructure structure = new X509CertificateStructure(new DERSequence(v));

		X509CertificateObject clientCert = new X509CertificateObject(structure);
		logger.debug("Verifying certificate for correct signature with CA public key");
		clientCert.verify(caCert.getPublicKey());

		// and export as PKCS12 formatted file along with the private key and
		// the CA certificate
		logger.debug("Exporting certificate in PKCS12 format");

		PKCS12BagAttributeCarrier bagCert = clientCert;
		bagCert.setBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_friendlyName, new DERBMPString("Certificate for IPSec WLAN access"));
		bagCert.setBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_localKeyId, new SubjectKeyIdentifierStructure(pubKey));

		KeyStore store = KeyStore.getInstance("PKCS12");

		store.load(null, null);

		X509Certificate[] chain = new X509Certificate[2];
		// first the client, then the CA certificate
		chain[0] = clientCert;
		chain[1] = caCert;

		store.setKeyEntry("Private key for IPSec WLAN access", privKey, exportPassword.toCharArray(), chain);

		FileOutputStream fOut = new FileOutputStream(exportFile);

		store.store(fOut, exportPassword.toCharArray());
		
		logger.info("  [" + exportFile + "] generated");

		return true;
	}

	/**
	 * Inclusao de extensões. Dados do certificado para ICP Brasil
	 * 
	 * @return
	 */
	private X509Extensions getCertGen() {
		X509ExtensionsGenerator certGen = new X509ExtensionsGenerator();
		certGen.addExtension(X509Extensions.SubjectAlternativeName, false, new DEREncodable() {
			public DERObject getDERObject() {
				try {
					ASN1EncodableVector nameVector = new ASN1EncodableVector();
					// person id
					String value = "01011980" + personID + "00000000000000000000000000";
					add(nameVector, OID_2_16_76_1_3_1, value);
					value = "0000000000000000000";
					add(nameVector, OID_2_16_76_1_3_6, value);
					value = "0000000000000000000";
					add(nameVector, OID_2_16_76_1_3_5, value);
					add(nameVector, null, personEmail);
					
					add(nameVector, OID_0_16_76_1_3_1, agentID);
					
					add(nameVector, OID_2_16_76_1_3_3, "XXXXXXXXXXXXXXX");

					return new GeneralNames(new DERSequence(nameVector)).getDERObject();

				} catch (Throwable e) {
					throw new CertBuilderException("Erro to set extensions for ICP Brasil", e);
				}
			}

			private void add(ASN1EncodableVector nameVector, String oid, String value) {

				if (oid != null) {
					ASN1EncodableVector v1 = new ASN1EncodableVector();
					v1.add(new DEROctetString(value.getBytes()));

					ASN1EncodableVector v2 = new ASN1EncodableVector();

					v2.add(new DERObjectIdentifier(oid));
					v2.add(new GeneralName(0, new DERSequence(v1)));
					nameVector.add(new GeneralName(0, new DERSequence(v2)));
				} else {
					nameVector.add(new GeneralName(1, new DEROctetString(value.getBytes())));
				}

			}
		});
		
		

		certGen.addExtension(X509Extensions.CertificatePolicies, false, new DEREncodable() {

			public DERObject getDERObject() {
				// Informa que o certificado é A3
				PolicyInformation pi = new PolicyInformation(new DERObjectIdentifier("2.16.76.1.2.3.4"));
				ASN1EncodableVector nameVector = new ASN1EncodableVector();
				nameVector.add(pi);
				return new GeneralNames(new DERSequence(nameVector)).getDERObject();
			}

		});
		


		certGen.addExtension(X509Extensions.KeyUsage, false, new DEREncodable() {
			@SuppressWarnings("unused")
			public int keyUsage;

			public static final int digitalSignature = (1 << 7);
			public static final int nonRepudiation = (1 << 6);
			public static final int keyEncipherment = (1 << 5);
			public static final int dataEncipherment = (1 << 4);
			public static final int keyAgreement = (1 << 3);
			public static final int keyCertSign = (1 << 2);
			public static final int cRLSign = (1 << 1);
			public static final int encipherOnly = (1 << 0);
			public static final int decipherOnly = (1 << 15);

			public DERObject getDERObject() {
				return new KeyUsage(digitalSignature | nonRepudiation | keyEncipherment | dataEncipherment | keyAgreement | keyCertSign | cRLSign | encipherOnly | decipherOnly);
			}

		});

		certGen.addExtension(X509Extensions.CRLDistributionPoints, false, new DEREncodable() {
			public DERObject getDERObject() {
				GeneralName gn = new GeneralName(GeneralName.uniformResourceIdentifier,	new DERIA5String(url));
				GeneralNames gns = new GeneralNames(new DERSequence(gn));
				DistributionPointName dpn = new DistributionPointName(0, gns);
				DistributionPoint distp = new DistributionPoint(dpn, null, null);
				return new DERSequence(distp);
			}

		});

		return certGen.generate();
	}

	public static X509CRL generateCRL(X509Certificate caCert, PrivateKey signatureKey, BigInteger serialNumber) throws CRLException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
		X509V2CRLGenerator crlGen = new X509V2CRLGenerator();
		crlGen.setIssuerDN(caCert.getSubjectX500Principal());
		GregorianCalendar currentDate = new GregorianCalendar();
		GregorianCalendar nextDate = new GregorianCalendar(currentDate.get(Calendar.YEAR) + 1, (currentDate.get(Calendar.MONTH) + 1) % 12, currentDate.get(Calendar.DAY_OF_MONTH));
		crlGen.setThisUpdate(currentDate.getTime());
		crlGen.setNextUpdate(nextDate.getTime());
		crlGen.setSignatureAlgorithm("SHA1withRSAEncryption");
		if (serialNumber != null)
			crlGen.addCRLEntry(serialNumber, currentDate.getTime(), CRLReason.superseded);
		return crlGen.generate(signatureKey, "BC");
	}

}
