package openjade.setting;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import openjade.core.OpenJadeException;

import org.xml.sax.SAXException;

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
		openjadeProperties = loadProperties("internal-openjade.properties");
		localProperties = loadProperties("openjade.properties");
		merge(openjadeProperties, localProperties);
		return openjadeProperties;
	}

	private static Properties loadProperties(String fileName) {
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			Enumeration<URL> urls = loader.getResources(fileName);
			while (urls.hasMoreElements()) {
				URL url = (URL) urls.nextElement();
				Properties localProp = new Properties();
				localProp.load(url.openStream());
				return localProp;
			}
		} catch (IOException e) {
			throw new OpenJadeException(e.getMessage(), e);
		}
		return new Properties();
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

	// Core
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

	// Trust
	public int getTrust_DirectCacheSize() {
		return Integer.parseInt(prop.getProperty("trust.direct.cache.size"));
	}

	// Timer
	public boolean getIterationEnabled() {
		return Boolean.parseBoolean(prop.getProperty("openjade.iteration.enabled"));
	}

	public long getIterationTimer() {
		return Long.parseLong(prop.getProperty("openjade.agent.iteration.timer"));
	}

	public List<String[]> getBoot() {
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			Enumeration<URL> urls = loader.getResources(prop.getProperty("openjade.boot.xml"));
			while (urls.hasMoreElements()) {
				URL url = (URL) urls.nextElement();
				InputStream input = url.openStream();
				SAXParserFactory factory = SAXParserFactory.newInstance();
				BootHandler handler = new BootHandler();
				SAXParser saxParser = factory.newSAXParser();
				saxParser.parse(input, handler);
				input.close();
				return handler.getArgs();
			}
		} catch (IOException e) {
			throw new OpenJadeException(e.getMessage(), e);
		} catch (ParserConfigurationException e) {
			throw new OpenJadeException(e.getMessage(), e);
		} catch (SAXException e) {
			throw new OpenJadeException(e.getMessage(), e);
		}
		return null;
	}

}
