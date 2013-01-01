package openjade.core;

import jade.content.AgentAction;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.leap.LEAPCodec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.KillContainer;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.lang.acl.ACLCodec;
import jade.lang.acl.ACLCodec.CodecException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.StringACLCodec;
import jade.util.leap.List;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

import javax.crypto.Cipher;

import openjade.agent.CMS;
import openjade.cert.CacheKey;
import openjade.cert.CertificateManager;
import openjade.cert.bean.CertificateBean;
import openjade.cert.criptography.Criptography;
import openjade.core.behaviours.BehaviourException;
import openjade.core.behaviours.LoaderKeystoreBehaviour;
import openjade.core.behaviours.RegisterServiceBehaviour;
import openjade.keystore.loader.implementation.KeyStoreLoaderImpl;
import openjade.ontology.Encipher;
import openjade.ontology.EncryptedMessage;
import openjade.ontology.OpenJadeOntology;
import openjade.ontology.PKCS7Message;
import openjade.ontology.Sign;
import openjade.signer.PKCS7Reader;
import openjade.signer.PKCS7Signer;

import org.apache.log4j.Logger;

/**
 * Representation of agents that have the ability to communicate through a
 * secure process based on asymmetric cryptography.
 * 
 * @author vanderson botelho
 */
public abstract class OpenAgent extends Agent {

	private static final long serialVersionUID = 1L;

	protected static Logger log = Logger.getLogger(OpenAgent.class);
	private static final String ENCODE = "UTF-8";
	private static final String DEFAULT_ALGORITHM = "RSA/ECB/PKCS1Padding";
	private static final String DEFAULT_PROVIDER = "com.sun.crypto.provider.SunJCE";

	protected KeyStore store;
	public X509Certificate certificate;
	protected CertificateBean certificateBean;
	protected PKCS7Signer signer;
	protected PKCS7Reader reader;
	protected Codec codec;
	protected Ontology openJadeOntology;
	protected String alias;
	protected CacheKey cacheKey;
	protected MessageTemplate openJadeMT;
	protected Provider provider;
	protected AID cms;

	public OpenAgent() {
		super();
		cacheKey = new CacheKey();
		codec = new LEAPCodec();
		openJadeOntology = OpenJadeOntology.getInstance();
		openJadeMT = MessageTemplate.and(MessageTemplate.MatchLanguage(codec.getName()), MessageTemplate.MatchOntology(openJadeOntology.getName()));
		addBehaviour(new LoaderKeystoreBehaviour(this));
	}

	protected abstract InputStream getKeystore();

	protected abstract String getKeystorePassword();

	public void loadKeystore() {
		if (store == null) {
			InputStream keystore = getKeystore();
			String password = getKeystorePassword();
			if (keystore == null) {
				throw new OpenJadeException("keystore can't is null");
			}
			if (password == null) {
				throw new OpenJadeException("keystorePassword can't is null");
			}
			loadKeyStore(keystore, password);
			signer = new PKCS7Signer(store, password);
		}
	}
	
	@Override
	public void addBehaviour(Behaviour b) {
		super.addBehaviour(b);
	}

	private void loadKeyStore(InputStream keystore, String password) {
		try {
			store = (new KeyStoreLoaderImpl(keystore)).getKeyStore(password);
			alias = (String) store.aliases().nextElement();
			certificate = (X509Certificate) store.getCertificate(alias);
			CertificateManager certManager = new CertificateManager(certificate, false);
			certificateBean = new CertificateBean();
			certManager.load(certificateBean);
			validAID();
			log.debug("certificate loader por agent [" + certificateBean.getAgentID() + "]");
		} catch (KeyStoreException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void validAID() {
		if (certificateBean.getAgentID() == null) {
			throw new OpenJadeException("Agent ID is Null. Agent ID is requerired");
		}
		if (!certificateBean.getAgentID().equals(getAID().getLocalName())) {
			throw new OpenJadeException("AID incompatible. Expected AID for the certificate: [" + certificateBean.getAgentID() + "]");
		}
	}

	public void signerAndSend(ACLMessage _message) {
		log.debug("signing message: " + _message.toString());
		PKCS7Message pkcs7Message = new PKCS7Message();
		pkcs7Message.setContent(this.signer.signPkcs7(_message.toString().getBytes()));
		Sign signMessage = new Sign();
		signMessage.setPkcs7(pkcs7Message);
		fillContent(_message, signMessage, codec, openJadeOntology);
		send(_message);
	}

	private void cipherKeyAndSend(ACLMessage _message, int keyMode) {
		log.debug("cipher message: " + _message.toString());
		Criptography cript = Criptography.getInstance(getInstanceProvider(DEFAULT_PROVIDER), DEFAULT_ALGORITHM);
		Key key = null;
		if (keyMode == Cipher.PRIVATE_KEY) {
			key = getPrivateKey();
		} else {
			AID aid = (AID) _message.getAllReceiver().next();
			key = cacheKey.get(aid);
			if (key == null) {
				throw new OpenJadeException("Key not found for agent [" + aid.getLocalName() + "]");
			}
		}
		List encode = cript.cript(_message.clone().toString().getBytes(), key);

		EncryptedMessage cipherMessage = new EncryptedMessage();
		cipherMessage.setKey(certificate.getPublicKey().getEncoded());
		cipherMessage.setKeyAlgorithm(certificate.getPublicKey().getAlgorithm());
		cipherMessage.setListContent(encode);

		Encipher encipher = new Encipher();
		encipher.setAlgorithm(DEFAULT_ALGORITHM);
		encipher.setProvider(DEFAULT_PROVIDER);
		encipher.setSignMode(keyMode);
		encipher.setMessage(cipherMessage);

		fillContent(_message, encipher, codec, openJadeOntology);

		send(_message);
	}

	public Provider getInstanceProvider(String clazz) {
		try {
			if (provider == null) {
				provider = (Provider) Class.forName(clazz).newInstance();
			}
			return provider;
		} catch (Exception e) {
			throw new OpenJadeException(e.getMessage(), e);
		}
	}

	public void cipherMyKeyAndSend(ACLMessage _message) {
		cipherKeyAndSend(_message, Cipher.PRIVATE_KEY);
	}

	public void cipherSenderKeyAndSend(ACLMessage _message) {
		cipherKeyAndSend(_message, Cipher.PUBLIC_KEY);
	}

	public void fillContent(ACLMessage _message, AgentAction _action, Codec _codec, Ontology _ontology) {
		try {
			_message.setLanguage(_codec.getName());
			_message.setOntology(_ontology.getName());
			getContentManager().registerLanguage(_codec);
			getContentManager().registerOntology(_ontology);
			getContentManager().fillContent(_message, _action);
		} catch (Exception e) {
			throw new OpenJadeException(e.getMessage(), e);
		}
	}

	private PrivateKey getPrivateKey() {
		try {
			return (PrivateKey) store.getKey(alias, getKeystorePassword().toCharArray());
		} catch (UnrecoverableKeyException e) {
			throw new OpenJadeException(e.getMessage(), e);
		} catch (KeyStoreException e) {
			throw new OpenJadeException(e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			throw new OpenJadeException(e.getMessage(), e);
		}
	}

	public ACLMessage blockingReceiveMessage() {
		ACLMessage message = blockingReceive();
		if (message != null && openJadeMT.match(message)) {
			ContentElement ce = extractContent(message, codec, openJadeOntology);
			if (ce instanceof Sign) {
				return decodeSignerMessage(message, (Sign) ce);
			}
			if (ce instanceof Encipher) {
				return decodeCipherMessage(message, (Encipher) ce);
			}
		}
		return message;
	}

	public ACLMessage receiveMessage() {
		ACLMessage message = receive();
		if (message != null && openJadeMT.match(message)) {
			ContentElement ce = extractContent(message, codec, openJadeOntology);
			if (ce instanceof Sign) {
				return decodeSignerMessage(message, (Sign) ce);
			}
			if (ce instanceof Encipher) {
				return decodeCipherMessage(message, (Encipher) ce);
			}
		}
		return message;
	}

	public ACLMessage receiveMessage(MessageTemplate mt) {
		ACLMessage message = receiveMessage();
		if (message != null && (mt == null || mt.match(message))) {
			return message;
		}
		return null;
	}

	private ACLMessage decodeCipherMessage(ACLMessage message, Encipher cipher) {
		Criptography cript = Criptography.getInstance(getInstanceProvider(cipher.getProvider()), cipher.getAlgorithm());
		try {
			Key key = null;
			if (cipher.getSignMode() == Cipher.PRIVATE_KEY) {
				X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec((byte[]) cipher.getMessage().getKey());
				X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(x509EncodedKeySpec.getEncoded());
				KeyFactory keyFactory = KeyFactory.getInstance(cipher.getMessage().getKeyAlgorithm());
				key = keyFactory.generatePublic(publicKeySpec);
			} else {
				key = getPrivateKey();
			}
			ACLCodec codec = new StringACLCodec();
			byte[] byteMessage = cript.decript(cipher.getMessage().getListContent(), key);
			try {
				return codec.decode(byteMessage, ENCODE);
			} catch (CodecException e) {
				throw new OpenJadeException(e.getMessage(), e);
			}
		} catch (Exception e) {
			throw new OpenJadeException(e.getMessage(), e);
		}
	}

	public ACLMessage decodeSignerMessage(ACLMessage message, Sign sign) {
		try {
			if (sign.getPkcs7().getContent() != null) {
				byte[] pkcs7 = (byte[]) sign.getPkcs7().getContent();
				if (signer.verify(pkcs7)) {
					PKCS7Reader reader = new PKCS7Reader(pkcs7);
					cacheKey.put(message.getSender(), reader.getX509Certificate().getPublicKey());
					ACLCodec codec = new StringACLCodec();
					return codec.decode(reader.getDataToString().getBytes(), ENCODE);
				} else {
					throw new OpenJadeException("invalid assignature");
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new OpenJadeException(e.getMessage(), e);
		}
	}

	public ContentElement extractContent(ACLMessage _message, Codec _codec, Ontology _ontology) {
		try {			
			getContentManager().registerLanguage(_codec);
			getContentManager().registerOntology(_ontology);
			return getContentManager().extractContent(_message);
		} catch (Exception e) {
			throw new OpenJadeException(e.getMessage(), e);
		}
	}

	protected void shutdown() {
		log.info("shutdown");
		Codec codec = new SLCodec();
		Ontology jmo = JADEManagementOntology.getInstance();
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(jmo);
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(getAMS());
		msg.setLanguage(codec.getName());
		msg.setOntology(jmo.getName());
		try {
			getContentManager().fillContent(msg, new Action(getAID(), new ShutdownPlatform()));
			send(msg);
		} catch (Exception e) {
			throw new OpenJadeException("try shutdown", e);
		}
	}

	public synchronized void moveContainer(String to) {
		try {
			String from = getContainerController().getContainerName();
			if (!to.equals(from)) {
				ContainerID cid = new ContainerID(to, null);
				doMove(cid);
				KillContainer kill = new KillContainer();
				kill.setContainer(new ContainerID(from, null));
				Codec codec = new SLCodec();
				Ontology jmo = JADEManagementOntology.getInstance();
				getContentManager().registerLanguage(codec);
				getContentManager().registerOntology(jmo);
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.addReceiver(getAMS());
				msg.setLanguage(codec.getName());
				msg.setOntology(jmo.getName());
				getContentManager().fillContent(msg, new Action(getAID(), kill));
				send(msg);
			}
		} catch (Exception e) {
			throw new OpenJadeException("moveContainer", e);
		}
	}

	public AID findCMS() {
		try {
			DFAgentDescription description = new DFAgentDescription();
			ServiceDescription service = new ServiceDescription();
			service.setType(CMS.SERVICE_TYPE);
			description.addServices(service);
			DFAgentDescription[] result = DFService.search(this, description);
			if (result != null && result.length > 0) {
				return result[0].getName();
			}
			return null;
		} catch (FIPAException e) {
			throw new BehaviourException(e.getMessage(), e);
		}
	}

	public void createAgent(String nickName, String className, Object[] args) {
		try {
			ContainerController cc = getContainerController();
			AgentController ac = cc.createNewAgent(nickName, className, args);
			ac.start();
		} catch (StaleProxyException e) {
			throw new OpenJadeException("createAgent", e);
		}
	}

	public void registerService(String service) {
		addBehaviour(new RegisterServiceBehaviour(this, service));
	}

	public X509Certificate getCertificate() {
		return certificate;
	}

	public Codec getCodec() {
		return codec;
	}

	public Ontology getOpenJadeOntology() {
		return openJadeOntology;
	}

	public AID getCms() {
		return cms;
	}

	public void setCms(AID cms) {
		this.cms = cms;
	}


	public java.util.List<AID> getAIDByService(String service) {
		try {
			java.util.List<AID> list = new ArrayList<AID>();
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType(service);
			dfd.addServices(sd);
			DFAgentDescription[] results;
			results = DFService.search(this, dfd);
			for (DFAgentDescription dfAgentDescription : results) {
				if (!dfAgentDescription.getName().equals(getAID())) {
					list.add(dfAgentDescription.getName());
				}
			}
			return list;
		} catch (FIPAException e) {
			throw new OpenJadeException(e.getMessage(), e);
		}
	}

}
