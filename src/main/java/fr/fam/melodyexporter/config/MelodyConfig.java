package fr.fam.melodyexporter.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.List;

import org.yaml.snakeyaml.Yaml;

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
    private static final String PROPERTY_APPLICATIONS_FILE = "javamelody.applications.file";

    /** */
    private static final String PROPERTY_APPLICATIONS = "javamelody.applications";

    /** */
    private static final int DEFAULT_TIMEOUT = 5000;

    /** */
    private static int timeout;

    private static List<Application> applications;

// obsolete, will be removed
    /** */
//    private String[] applications;
//

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

                // Get applications file and open it
                String rawApplicationsFile = props.getProperty(PROPERTY_APPLICATIONS_FILE, null);
                if (rawApplicationsFile != null) {
                    InputStream appsInputStream = Thread.currentThread()
                            .getContextClassLoader()
                            .getResourceAsStream(rawApplicationsFile);
                    if (appsInputStream != null) {
                        // Parse applications file
                        Yaml yaml = new Yaml();
                        applications = (List<Application>) yaml.load(appsInputStream);
                        LOGGER.debug("Loaded configuration : " + applications);
                        //
                        // TO DO
                        //
                        appsInputStream.close();
                        LOGGER.info("Applications file <" + rawApplicationsFile + "> loaded");
                    } else {
                        LOGGER.error("Can't open applications file : " + rawApplicationsFile);
                        throw new IllegalStateException("Can't open applications file");
                    }
                } else {
                    LOGGER.error("Applications file name missing");
                    throw new IllegalStateException("Applications file name missing");
                }

// obsolete, will be removed

/*
                // Get applications list
                String rawApplications = props.getProperty(PROPERTY_APPLICATIONS, null);
                if (rawApplications != null) {
                    LOGGER.debug("Applications list found : " + rawApplications);
                    applications = (rawApplications.split(","));
                } else {
                    LOGGER.error("Applications list empty");
                    throw new IllegalStateException("Applications list empty");
                }
*/

// end of obsolete

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

    public final List<Application> getApplications() {
        return applications;
    }


// obsolete, will be removed
    /**
    *
    * @return applications
    */
/*
    public final String[] getApplications() {
        return applications;
    }
*/
//
}
