package openjade.setting;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import openjade.core.OpenJadeException;

public class Settings implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static Settings config;
	private Properties prop;

	private Settings() {
		load();
	}

	public static synchronized Settings getInstance() {
		if (config == null) {
			config = new Settings();
		}
		return config;
	}

	private void load() {
		prop = loadOpenJadeProperties();
	}

	private static Properties loadOpenJadeProperties() {
		Properties openjadeProperties = new Properties();
		Properties localProperties = new Properties();
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			Enumeration<URL> urls = loader.getResources("openjade.properties");
			while (urls.hasMoreElements()) {
				URL url = (URL) urls.nextElement();
				Properties localProp = new Properties();
				localProp.load(url.openStream());
				if (localProp.containsKey("main.file")) {
					openjadeProperties = localProp;
				} else {
					localProperties = localProp;
				}
			}
			merge(openjadeProperties, localProperties);
		} catch (IOException e) {
			throw new OpenJadeException(e.getMessage(), e);
		}
		return openjadeProperties;
	}

	private static void merge(Properties principalProp, Properties mergeProp) {
		try {
			if (!principalProp.isEmpty() && !mergeProp.isEmpty()) {
				Enumeration<Object> keys = mergeProp.keys();
				while (keys.hasMoreElements()) {
					String key = (String) keys.nextElement();
					principalProp.setProperty(key, mergeProp.getProperty(key));
				}
			}
		} catch (Exception e) {
			throw new OpenJadeException(e.getMessage(), e);
		}
	}
	
	//Core
	public int getCore_InitialIteration() {
		return Integer.parseInt(prop.getProperty("core.initial.iteration"));
	}

	// Monitor
	public String getMonitor_Title() {
		return prop.getProperty("openjade.trust.gui.monitor.title");
	}

	public String getMonitor_LegendX() {
		return prop.getProperty("openjade.trust.gui.monitor.legend.x");
	}

	public String getMonitor_LegendY() {
		return prop.getProperty("openjade.trust.gui.monitor.legend.y");
	}
	
	public double getMonitorMaxValue() {
		return Double.parseDouble(prop.getProperty("openjade.trust.gui.monitor.maxvalue"));
	}

	public double getMonitorIterations() {
		return Double.parseDouble(prop.getProperty("openjade.trust.gui.monitor.iterations"));
	}

	//Trust
	public int getTrust_DirectCacheSize() {
		return Integer.parseInt(prop.getProperty("trust.direct.cache.size"));
	}
	
	//Timer
	public boolean getIterationEnabled() {
		return Boolean.parseBoolean(prop.getProperty("openjade.iteration.enabled"));
	}

	public long getIterationTimer() {
		return Long.parseLong(prop.getProperty("openjade.agent.iteration.timer"));
	}
	
	
}
