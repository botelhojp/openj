package openjade.agent;

import java.io.InputStream;

import openjade.core.OpenAgent;
import openjade.core.behaviours.SendIterationBehaviour;

import org.apache.log4j.Logger;

public class TimerAgent extends OpenAgent {

	protected static Logger log = Logger.getLogger(TimerAgent.class);

	private static final long serialVersionUID = 1L;

	private String keystore;

	private String keystorePassword;

	protected void setup() {
		keystore = "/certs/agent_timer_001.pfx";
		keystorePassword = "123456";
		moveContainer(OpenAgent.MAIN_CONTAINER);
		log.debug("setup: " + getAID().getLocalName());
		addBehaviour(new SendIterationBehaviour(this, 5000));
	}

	@Override
	protected InputStream getKeystore() {
		return TimerAgent.class.getResourceAsStream(keystore);
	}

	@Override
	protected String getKeystorePassword() {
		return keystorePassword;
	}
}
