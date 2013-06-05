package ${packageName};

import jade.core.behaviours.Behaviour;

import java.io.InputStream;

import openjade.core.OpenAgent;
import openjade.core.SignerAgent;

import org.apache.log4j.Logger;

public class Mygent extends OpenAgent implements SignerAgent {

	private static final long serialVersionUID = 1L;

	protected static Logger log = Logger.getLogger(Mygent.class);

	protected void setup() {
		log.debug("setup: " + getAID().getLocalName());
		registerService(getAID().getLocalName());
		this.moveContainer("Hi-Container");
		addBehaviour(new MyBehaviour(this, MyBehaviour.SIGNED));
	}

	public InputStream getKeystore() {
		return Mygent.class.getResourceAsStream("/certs/" + getAID().getLocalName() + ".pfx");
	}

	public String getKeystorePassword() {
		return "123456";
	}

	@Override
	public void removeBehaviour(Behaviour _behaviour) {
		super.removeBehaviour(_behaviour);
		if (_behaviour instanceof MyBehaviour) {
			MyBehaviour behaviour = (MyBehaviour) _behaviour;
			if (behaviour.getMode().equals(MyBehaviour.SIGNED)) {
				addBehaviour(new MyBehaviour(this, MyBehaviour.CIPHER_PRIVATE_KEY));
			} else if (behaviour.getMode().equals(MyBehaviour.CIPHER_PRIVATE_KEY)) {
				addBehaviour(new MyBehaviour(this, MyBehaviour.CIPHER_PUBLIC_KEY));
			}
		}
	}
}
