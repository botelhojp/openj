package openjade.agent;

import java.io.InputStream;

import openjade.core.OpenAgent;
import openjade.core.SignerAgent;
import openjade.core.behaviours.ChangeterationBehaviour;
import openjade.setting.Settings;

import org.apache.log4j.Logger;

public class TimerAgent extends OpenAgent implements SignerAgent {

	protected static Logger log = Logger.getLogger(TimerAgent.class);

	private static final long serialVersionUID = 1L;

	private String keystore;

	private String keystorePassword;

	protected void setup() {
		keystore = "/certs/tm.pfx";
		keystorePassword = "openjade";
		moveContainer(OpenAgent.MAIN_CONTAINER);
		log.debug("setup: " + getAID().getLocalName());
		addBehaviour(new ChangeterationBehaviour(this, Settings.getInstance().getIterationTimer()));
	}

	public InputStream getKeystore() {
		return TimerAgent.class.getResourceAsStream(keystore);
	}

	public String getKeystorePassword() {
		return keystorePassword;
	}
}
