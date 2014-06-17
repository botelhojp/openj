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
import jade.util.leap.Serializable;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
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

import openjade.cert.CacheKey;
import openjade.cert.CertificateManager;
import openjade.cert.bean.CertificateBean;
import openjade.cert.criptography.Criptography;
import openjade.core.annotation.OnChangeIteration;
import openjade.core.annotation.OnGetUtility;
import openjade.core.annotation.ReceiveMatchMessage;
import openjade.core.behaviours.BehaviourException;
import openjade.core.behaviours.LoaderKeystoreBehaviour;
import openjade.core.behaviours.ReceiveMessageBehaviour;
import openjade.core.behaviours.RegisterServiceBehaviour;
import openjade.keystore.loader.implementation.KeyStoreLoaderImpl;
import openjade.ontology.ChangeIteration;
import openjade.ontology.Encipher;
import openjade.ontology.EncryptedMessage;
import openjade.ontology.OpenJadeOntology;
import openjade.ontology.PKCS7Message;
import openjade.ontology.Rating;
import openjade.ontology.SendRating;
import openjade.ontology.Sign;
import openjade.signer.PKCS7Reader;
import openjade.signer.PKCS7Signer;
import openjade.trust.ITrustModel;

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

	public static final String MAIN_CONTAINER = "Main-Container";

	public static final String LISTENER_TIMER = "openjade.service.listener.timer";
	public static final String SERVICE_TRUST_MONITOR = "openjade.trust.monitor";
	public static final String TIMER_LISTENER = "openjade.timer.listener";
	public static final String TRUSTMODEL_REPUTATION_LISTENER = "openjade.trustmodel.reputation.listener";
	
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
	protected int iteration;
	protected ITrustModel trustModel;
	protected ArrayList<String> services;

	public OpenAgent() {
		super();
		services = new ArrayList<String>();
		cacheKey = new CacheKey();
		codec = new LEAPCodec();
		openJadeOntology = OpenJadeOntology.getInstance();
		openJadeMT = MessageTemplate.and(MessageTemplate.MatchLanguage(codec.getName()), MessageTemplate.MatchOntology(openJadeOntology.getName()));
		addBehaviour(new LoaderKeystoreBehaviour(this));
		addBehaviour(new ReceiveMessageBehaviour(this));
	}
	
	public void searchWitnesses(AID server){
		log.debug("searchWitnesses not implemented");
	}
	
	protected void loadTrustModel(Class<ITrustModel> clazz){
		try {
			trustModel = clazz.newInstance();
			trustModel.setAgent(this);
		} catch (InstantiationException e) {
			throw new OpenJadeException("not load trust model", e);
		} catch (IllegalAccessException e) {
			throw new OpenJadeException("not load trust model", e);
		}		
	}

	public void loadKeystore() {
		if (this instanceof SignerAgent && store == null) {
			InputStream keystore = ((SignerAgent) this).getKeystore();
			String password = ((SignerAgent) this).getKeystorePassword();
			if (keystore == null) {
				throw new OpenJadeException("keystore can't is null");
			}
			if (password == null) {
				throw new OpenJadeException("keystorePassword can't is null");
			}
			loadKeyStore(keystore, password);
			signer = new PKCS7Signer(store, password);
		}

		if (!(this instanceof SignerAgent) && signer == null) {
			signer = new PKCS7Signer(null, null);
		}
	}

	@ReceiveMatchMessage(action = ChangeIteration.class, ontology = OpenJadeOntology.class)
	public final void changeIteration(ACLMessage message, ContentElement ce) {
		iteration = ((ChangeIteration) ce).getIteration();
		callOnChangeInteration();
		if (trustModel != null) {
			trustModel.currentIteration(iteration);
			java.util.List<AID> aids = getAIDByService(OpenAgent.SERVICE_TRUST_MONITOR);
			if (!aids.isEmpty()) {
				SendRating sendRating = new SendRating();
				Float utility = callOnGetUtility(iteration);
				if (utility != null) {
					Rating rating = trustModel.addRating(getAID(), getAID(), iteration, trustModel.getClass().getName(), utility);
					jade.util.leap.List ratingList = new jade.util.leap.ArrayList();
					ratingList.add(rating);
					sendRating.setRating(ratingList);

					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					msg.setSender(getAID());
					msg.addReceiver(aids.get(0));
					fillContent(msg, sendRating, getCodec(), OpenJadeOntology.getInstance());
					sendMessage(msg);
				}
			}
		}
	}
	
	
	private void callOnChangeInteration() {
		try {
			Method[] methods = getClass().getMethods();
			for (Method method : methods) {
				method.setAccessible(true);
				if (method.isAnnotationPresent(OnChangeIteration.class)) {
					OnChangeIteration onChangeIteration = method.getAnnotation(OnChangeIteration.class);
					if (onChangeIteration.delay() > 0) {
						Thread.sleep(onChangeIteration.delay());
					}
					method.invoke(this);
				}
			}
		} catch (Exception e) {
			throw new BehaviourException(e.getMessage(), e);
		}
	}

	private Float callOnGetUtility(long iteration) {
		try {
			Method[] methods = getClass().getMethods();
			for (Method method : methods) {
				method.setAccessible(true);
				if (method.isAnnotationPresent(OnGetUtility.class)) {
					return (Float) method.invoke(this, iteration);
				}
			}
			return null;
		} catch (Exception e) {
			throw new BehaviourException(e.getMessage(), e);
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

	public void sendMessage(ACLMessage msg) {
		if (this instanceof SignerAgent) {
			signerAndSend(msg);
		} else {
			this.send(msg);
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

	private void signerAndSend(ACLMessage _message) {
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
			return (PrivateKey) store.getKey(alias, ((SignerAgent) this).getKeystorePassword().toCharArray());
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

	public void createAgent(String nickName, String className, Object[] args) {
		try {
			ContainerController cc = getContainerController();
			AgentController ac = cc.createNewAgent(nickName, className, args);
			ac.start();
		} catch (StaleProxyException e) {
			throw new OpenJadeException("createAgent", e);
		}
	}
	
	public void addService(String service){
		services.add(service);
	}

	public void registerService(String... service) {
		addBehaviour(new RegisterServiceBehaviour(this, service));
	}
	
	public String showRating(Rating rt) {		
		return rt.getIteration() + ":" + rt.getClient().getLocalName() + ">" + rt.getServer().getLocalName() + ":" + rt.getTerm()+ ":" + rt.getValue();
	}

	
	public void registerService() {
		String[] _services = new String[services.size()];
		for (int i = 0; i < services.size(); i++) {
			_services[i] = services.get(i);
		}
		registerService(_services);
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

	public ITrustModel getTrustModel() {
		return trustModel;
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

	private void sendMessage(String[] listService, int performative, String conversationId, Serializable object, String content) {
		for (String service : listService) {
			java.util.List<AID> aids = getAIDByService(service);
			for (AID aid : aids) {
				sendMessage(aid, performative, conversationId, object, content);
			}
		}
	}

	public void sendMessage(java.util.List<AID> agentList, int performative, String conversationId, Serializable object, String content) {
		for (AID aid : agentList) {
			sendMessage(aid, performative, conversationId, object, content);
		}
	}

	public void sendMessage(String[] listService, int performative, String conversationId) {
		sendMessage(listService, performative, conversationId, null, "");
	}

	public void sendMessage(String[] listService, int performative, String conversationId, String content) {
		sendMessage(listService, performative, conversationId, null, content);
	}

	public void sendMessage(String[] agentList, int performative, String conversationId, Serializable object) {
		sendMessage(agentList, performative, conversationId, object, "");

	}

	public void sendMessage(String _service, int performative, String conversationId) {
		sendMessage(_service, performative, conversationId, null, "");
	}

	public void sendMessage(String _service, int performative, String conversationId, String content) {
		sendMessage(_service, performative, conversationId, null, content);
	}

	private void sendMessage(String _service, int performative, String conversationId, Serializable object, String content) {
		String[] service = { _service };
		sendMessage(service, performative, conversationId, object, content);
	}

	public void sendMessage(String _service, int performative, String conversationId, Serializable object) {
		sendMessage(_service, performative, conversationId, object, "");
	}

	public void sendMessage(AID to, int performative, String conversationId, String content) {
		sendMessage(to, performative, conversationId, null, content);
	}

	/**
	 * Enviar uma mensagem composto por um Concept do tipo AgentAction
	 * @param to Destinatário da mensagem
	 * @param action Ação
	 * @param ontolory Ontologia
	 */
	public void sendMessage(AID to, int performative, AgentAction action, Ontology ontolory) {
		ACLMessage message = new ACLMessage(performative);
		message.setSender(this.getAID());
		message.addReceiver(to);
		fillContent(message, action, getCodec(), ontolory);
		sendMessage(message);
	}
	
	public void sendMessage(AID to, int performative, String conversationId, AgentAction action, Ontology ontolory) {
		ACLMessage message = new ACLMessage(performative);
		message.setSender(this.getAID());
		message.addReceiver(to);
		message.setConversationId(conversationId);
		fillContent(message, action, getCodec(), ontolory);
		sendMessage(message);
	}
	
	/**
	 * Enviar uma mensagem composto por um Concept do tipo AgentAction
	 * @param to Destinatário da mensagem
	 * @param action Ação
	 * @param ontolory Ontologia
	 */
	public void sendMessage(String service, int performative, AgentAction action, Ontology ontolory) {
		java.util.List<AID> aids = getAIDByService(service);
		for (AID aid : aids) {
			sendMessage(aid, performative, action, ontolory);
		}
	}
	
	
	public void sendMessage(String service, int performative, String conversationId, AgentAction action, Ontology ontolory) {
		java.util.List<AID> aids = getAIDByService(service);
		for (AID aid : aids) {
			sendMessage(aid, performative, conversationId, action, ontolory);
		}
	}


	public void sendMessage(AID to, int performative, String conversationId, Serializable object) {
		sendMessage(to, performative, conversationId, object, null);
	}

	private void sendMessage(AID to, int performative, String conversationId, Serializable object, String content) {
		try {
			ACLMessage message = new ACLMessage(performative);
			message.setConversationId(conversationId);
			message.setSender(this.getAID());
			message.addReceiver(to);
			if (object != null) {
				message.setContentObject(object);
			} else {
				message.setContent(content);
			}
			sendMessage(message);
		} catch (IOException e) {
			throw new OpenJadeException(e.getMessage(), e);
		}
	}
}
