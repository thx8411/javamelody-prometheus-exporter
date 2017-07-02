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
    private static final String PROPERTY_COLLECTOR_APPLICATIONS = "javamelody.collector.applications";

    /** */
    private String[] collectorApplications;

    /**
    *
    * @throws IllegalStateException IllegalStateException
    */
    public MelodyConfig() throws IllegalStateException {

        LOGGER.debug("Reading config...");

        InputStream propsInputStream = null;
        try {
            try {
                Properties props = new Properties();
                propsInputStream = Thread.currentThread()
                        .getContextClassLoader()
                        .getResourceAsStream(SETTINGS_FILENAME);
                props.load(propsInputStream);

                String rawApplications = props.getProperty(PROPERTY_COLLECTOR_APPLICATIONS, null);
                if (rawApplications != null) {
                    LOGGER.debug("Applications list found : " + rawApplications);
                    collectorApplications = (rawApplications.split(","));
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
    * @return collectorApplications
    */
    public final String[] getCollectorApplications() {
        return collectorApplications;
    }
}
