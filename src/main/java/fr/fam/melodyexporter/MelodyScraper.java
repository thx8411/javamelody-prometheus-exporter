package fr.fam.melodyexporter;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import fr.fam.melodyexporter.config.MelodyConfig;
import fr.fam.melodyexporter.config.Application;

/**
*/
public class MelodyScraper {

    /** */
    private static final Logger LOGGER = Logger.getLogger(MelodyScraper.class);

    /** */
    private MelodyConfig config;

    /** */
    private static final String LAST_VALUE_BASE_URL = "?part=lastValue";

    /** */
    private static final String GRAPH_PARAMETER = "&graph=";

    /**
    *
    * @param pconfig Configuration
    */
    public MelodyScraper(final MelodyConfig pconfig) {
        config = pconfig;
    }

    /**
    *
    * @param application application
    * @throws ScrapException ScrapException
    * @return result
    */
    public final Map<String, Double> scrap(final Application application)
            throws ScrapException {

        Map<String, Double> result = new LinkedHashMap<String, Double>(application.getMetrics().length);

        for (String graph : application.getMetrics()) {
            result.put(graph, -1.0);
        }

        String downloadLastValueData = downloadLastValueData(application, buildLastValueUrl(application));

        if (downloadLastValueData != null) {

            StringTokenizer rawResultTokens = new StringTokenizer(downloadLastValueData, ",");

            for (String graph : result.keySet()) {
                try {
                    String token = rawResultTokens.nextToken();
                    Double value = Double.parseDouble(token);
                    if (!value.isNaN()) {
                        result.put(graph, value);
                    }
                } catch (NoSuchElementException e) {
                    LOGGER.warn("Missing metric, using -1.0");
                }
            }
        } else {
            LOGGER.warn("Missing metrics, using -1.0");
        }

        return result;
    }

    /**
    *
    * @param application application
    * @param url url
    * @throws ScrapException ScrapException
    * @return string
    */
    private String downloadLastValueData(final Application application, final String url) throws ScrapException {
        Header authHeader;

        try {
                        LOGGER.debug("Get metrics " + url);
            Request request = Request.Get(url)
                    .connectTimeout(config.getTimeout())
                    .socketTimeout(config.getTimeout());

            // authentification
            if (application.getLogin() != null && application.getPassword() != null) {
                authHeader = new BasicHeader("Authorization", "Basic"
                    + buildBasicAuthHeaderValue(application.getLogin(), application.getPassword()));
                request = request.addHeader(authHeader);
            }

            HttpResponse response = request.execute().returnResponse();
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity());
            } else {
                LOGGER.warn("HTTP-Response code was " + responseCode + " for " + url);
                return null;
            }
        } catch (IOException e) {
            throw new ScrapException("Exception while downloading: " + url, e);
        }
    }

    /**
    *
    * @param login login
    * @param password password
    * @return base64 encoded credentials
    */
    private String buildBasicAuthHeaderValue(final String login, final String password) {
        String credentials = login + ":" + password;
        return Base64.encodeBase64String(credentials.getBytes());
    }

    /**
    *
    * @param application application
    * @throws ScrapException ScrapException
    * @return string
    */
    private String buildLastValueUrl(final Application application) {

        StringBuilder sBuilder = new StringBuilder(application.getUrl());

        sBuilder.append(LAST_VALUE_BASE_URL);
        sBuilder.append(GRAPH_PARAMETER);
        int size = application.getMetrics().length;

        for (String m : application.getMetrics()) {
            sBuilder.append(m);
            if (--size > 0) {
                sBuilder.append(",");
            }
        }
        return sBuilder.toString();
    }
}
