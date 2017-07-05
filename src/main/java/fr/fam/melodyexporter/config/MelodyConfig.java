package fr.fam.melodyexporter.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

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

    /** */
    private static Applications applications;

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
                LOGGER.info("Reading Properties...");

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
                LOGGER.debug("Reading applications file...");

                String rawApplicationsFile = props.getProperty(PROPERTY_APPLICATIONS_FILE, null);
                if (rawApplicationsFile != null) {
                    InputStream appsInputStream = Thread.currentThread()
                            .getContextClassLoader()
                            .getResourceAsStream(rawApplicationsFile);
                    if (appsInputStream != null) {
                        // Parse applications file
                        LOGGER.debug("Parsing yaml file...");

                        Yaml yaml = new Yaml(new Constructor(Applications.class));
                        applications = (Applications) yaml.load(appsInputStream);

                        LOGGER.debug("Parse done");

                        for (Application application : applications.getApplications()) {

                            // check label format : name=value
                            Pattern pattern = Pattern.compile("[a-z_A-Z0-9]*=[a-z_A-Z0-9]*");
                            for (String s : application.getLabels()) {
                                if (!pattern.matcher(s).matches()) {
                                    LOGGER.error("Wrong label : " + application.getName() + "." + s);
                                    throw new IllegalStateException("Wrong label : " + application.getName() + "." + s);
                                }
                            }

                            // check metrics
                            for (String s : application.getMetrics()) {
                               Boolean known = false;
                               for (MelodyLastValueGraphs g : MelodyLastValueGraphs.values()) {
                                   if (g.getParameterName().equals(s)) {
                                       known = true;
                                   }
                               }
                               if (!known) {
                                   LOGGER.error("Unknown metric : " + application.getName() + "." + s);
                                   throw new IllegalStateException("Unknown metric : " + application.getName() + "." + s);
                               }
                            }

                            LOGGER.debug("Loaded configuration : " + application);
                        }

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
            } finally {
                if (propsInputStream != null) {
                    propsInputStream.close();
                }
            }
        } catch (IOException e) {
            LOGGER.error("Configuration failure", e);
            throw new IllegalStateException("Configuration failure", e);
        }
        LOGGER.info("Configuration loaded");
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

    public final Applications getApplications() {
        return applications;
    }
}
