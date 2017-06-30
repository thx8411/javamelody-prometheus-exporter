package fr.fam.melodyexporter.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
*/
public class MelodyConfig {

    /**
    *
    */
    private static final Logger LOGGER = Logger
            .getLogger(MelodyConfig.class);

    /**
    *
    */
    private static final String PROPERTY_FILENAME = "javamelody.properties";

    /**
    *
    */
    private static final String PROPERTY_COLLECTOR_APPLICATIONS = "javamelody.collector.applications";

    /**
    *
    */
    private String[] collectorApplications;

    /**
    *
    * @throws IllegalStateException IllegalStateException
    */
    public MelodyConfig() {

        LOGGER.debug("Reading config...");

        InputStream propsInputStream = null;
        try {
            try {
                Properties props = new Properties();
                propsInputStream = Thread.currentThread()
                        .getContextClassLoader()
                        .getResourceAsStream(PROPERTY_FILENAME);
                props.load(propsInputStream);

                String rawApplications = props.getProperty(
                        PROPERTY_COLLECTOR_APPLICATIONS, null);
                if (rawApplications != null) {
                    LOGGER.debug("Applications list found : " + rawApplications);
                    setCollectorApplications(rawApplications.split(","));
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
        LOGGER.info("Using config: " + this.toString());
    }

    /**
    *
    * @return collectorApplications
    */
    public final String[] getCollectorApplications() {
        return collectorApplications;
    }

    /**
    *
    * @param pcollectorApplications pcollectorApplications
    */
    public final void setCollectorApplications(final String[] pcollectorApplications) {
        this.collectorApplications = pcollectorApplications;
    }
}