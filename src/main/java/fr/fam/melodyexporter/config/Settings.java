package fr.fam.melodyexporter.config;

import org.apache.log4j.Logger;

/**
*
*/
public class Settings {

    /** */
    private static final Logger LOGGER = Logger.getLogger(Settings.class);

    /** */
    private int timeout;

    /**
    *
    * @return timeout
    */
    public final int getTimeout() {
        return timeout;
    }

    /**
    *
    * @param ptimeout timeout
    */
    public final void setTimeout(final int ptimeout) {
        timeout = ptimeout;
        LOGGER.info("timeout set to : " + timeout);
    }
}
