package fr.fam.melodyexporter.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
*
*/
public class MelodyConfig {

    /** */
    private static final Logger LOGGER = Logger.getLogger(MelodyConfig.class);

    /** */
    private static final String SETTINGS_FILENAME = "melodyexporter.properties";

    /** */
    private static final String PROPERTY_TIMEOUT = "javamelody.timeout";

    /** */
    private static final String PROPERTY_APPLICATIONS = "javamelody.applications";

    /** */
    private static final int DEFAULT_TIMEOUT = 5000;

    /** */
    private static int timeout;

    /** */
    private String[] applications;

    /**
    *
    * @throws IllegalStateException IllegalStateException
    */
    public MelodyConfig() throws IllegalStateException {

        LOGGER.debug("Reading config...");

        InputStream propsInputStream = null;
        try {
            try {
                // load properties file
                Properties props = new Properties();
                propsInputStream = Thread.currentThread()
                        .getContextClassLoader()
                        .getResourceAsStream(SETTINGS_FILENAME);
                props.load(propsInputStream);

                // Get timeout
                String rawTimeout = props.getProperty(PROPERTY_TIMEOUT, null);
                if (rawTimeout != null) {
                    LOGGER.debug("Timeout found : " + rawTimeout);
                    try {
                        timeout = Integer.parseInt(rawTimeout);
                        LOGGER.info("Using timeout : " + timeout);
                    } catch (NumberFormatException e) {
                        timeout = DEFAULT_TIMEOUT;
                        LOGGER.warn("Timeout isn't a number : " + rawTimeout);
                    }
                }

                // Get applications list
                String rawApplications = props.getProperty(PROPERTY_APPLICATIONS, null);
                if (rawApplications != null) {
                    LOGGER.debug("Applications list found : " + rawApplications);
                    applications = (rawApplications.split(","));
                } else {
                    LOGGER.error("Applications list empty");
                    throw new IllegalStateException("Applications list empty");
                }
            } finally {
                if (propsInputStream != null) {
                    propsInputStream.close();
                }
            }
        } catch (IOException e) {
            LOGGER.error("Configuration failure", e);
            throw new IllegalStateException("Configuration failure", e);
        }
        LOGGER.info("Config loaded");
    }

    /**
    *
    * @return timeout
    */
    public final int getTimeout() {
        return timeout;
    }

    /**
    *
    * @return applications
    */
    public final String[] getApplications() {
        return applications;
    }
}
