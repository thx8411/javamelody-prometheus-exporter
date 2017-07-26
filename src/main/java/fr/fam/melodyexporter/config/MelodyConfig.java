package fr.fam.melodyexporter.config;

import java.io.InputStream;
import java.util.Scanner;

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
    private static final String SETTINGS_FILENAME = "melodyexporter.yml";

    /** */
    private static Settings settings;

    /** */
    private static Applications applications;

    /**
    *
    * @throws IllegalStateException IllegalStateException
    */
    public MelodyConfig() throws IllegalStateException {
        LOGGER.debug("Reading configuration...");

        try {
            // Get applications file and open it
            InputStream settingsInputStream = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(SETTINGS_FILENAME);

            // Parse settings file
            LOGGER.debug("Parsing yaml file...");

            // split documents in yaml file
            String[] documents = yamlSplit(settingsInputStream);
            settingsInputStream.close();

            // load settings (document 0)
            Yaml yamlSettings = new Yaml(new Constructor(Settings.class));
            settings = (Settings) yamlSettings.load(documents[0]);
            LOGGER.info("Settings loaded");

            // load applications (document 1)
            Yaml yamlApplications = new Yaml(new Constructor(Applications.class));
            applications = (Applications) yamlApplications.load(documents[1]);
            LOGGER.info("Applications loaded : " + applications.getApplications().size());
            LOGGER.debug("Applications : " + applications.toString());

            checkApplications();

        } catch (Exception e) {
                LOGGER.error("Can't read settings file : " + SETTINGS_FILENAME);
                LOGGER.error("Exception : " + e, e);
                throw new IllegalStateException("Can't read settings file");
        }
    }

    /**
    *
    * @return timeout
    */
    public final int getTimeout() {
        return settings.getTimeout();
    }

    /**
    *
    * @return applications
    */

    public final Applications getApplications() {
        return applications;
    }

    /**
    * Splits the yaml documents into separated strings.
    *
    * @param in inputStream
    * @return array of documents
    * @throws IllegalStateException IllegalStateException
    */
    private String[] yamlSplit(final InputStream in) throws IllegalStateException {
        Scanner s = new Scanner(in).useDelimiter("\\A");

        String buffer;
        if (s.hasNext()) {
            buffer = s.next();
        } else {
            buffer = "";
        }

        String[] documents = buffer.split("---");

        if (documents.length >= 2 && documents[0] != null && documents[1] != null) {
            return documents;
        } else {
            LOGGER.error("Bad yaml syntax : file should contain 2 documents");
            throw new IllegalStateException("Bad yaml syntax : file should contain 2 documents");
        }
    }

    /**
    *
    * @throws IllegalStateException IllegalStateException
    */
    private void checkApplications() throws IllegalStateException {

        for (Application application : applications.getApplications()) {

            // check metrics
            for (Metric m : application.getMetrics()) {
                Boolean known = false;
                for (MelodyLastValueGraphs g : MelodyLastValueGraphs.values()) {
                    if (g.getParameterName().equals(m.getName())) {
                        known = true;
                    }
                }
                if (!known) {
                    LOGGER.error("Unknown metric : " + application.getName() + "." + m.getName());
                    throw new IllegalStateException("Unknown metric : " + application.getName() + "." + m.getName());
                }
            }
        }
    }

}
