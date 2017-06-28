package de.florianschlag.javamelodyprometheusexporter.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
*/
public class JavaMelodyConfig {

	private static final Logger logger = Logger.getLogger(JavaMelodyConfig.class);

	private static final String PROPERTY_FILENAME = "javamelody.properties";
	private static final String PROPERTY_COLLECTOR_APPLICATIONS = "javamelody.collector.applications";

	private String[] collectorApplications;

	/**
	*/
	public JavaMelodyConfig() {
		InputStream propsInputStream = null;
		try {
			try {
				Properties props = new Properties();
				propsInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTY_FILENAME);
				props.load(propsInputStream);

				String rawApplications = props.getProperty(PROPERTY_COLLECTOR_APPLICATIONS, null);
				setCollectorApplications(rawApplications != null ? rawApplications.split(",") : null);
			} finally {
				if (propsInputStream != null)
					propsInputStream.close();
			}
		} catch (IOException e) {
			logger.error("Configuration failure", e);
			throw new IllegalStateException("Configuration failure", e);
		}
		logger.info("Using config: " + this.toString());
	}

	/**
	*/
	public String[] getCollectorApplications() {
		return collectorApplications;
	}

	/**
	*/
	public void setCollectorApplications(String[] collectorApplications) {
		this.collectorApplications = collectorApplications;
	}

}
