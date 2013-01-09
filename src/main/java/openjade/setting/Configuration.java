package openjade.setting;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import openjade.core.OpenJadeException;

public class Configuration {

	private static Configuration config;
	private Properties prop;

	private Configuration() {
		load();
	}

	public static synchronized Configuration getInstance() {
		if (config == null) {
			config = new Configuration();
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

	public String getMonitorTitle() {
		return prop.getProperty("trust.gui.monitor.title");
	}

	public String getMonitorLegendX() {
		return prop.getProperty("trust.gui.monitor.legend.x");
	}

	public String getMonitorLegendY() {
		return prop.getProperty("trust.gui.monitor.legend.y");
	}
}
