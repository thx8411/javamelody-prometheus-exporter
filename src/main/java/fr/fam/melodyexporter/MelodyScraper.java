package fr.fam.melodyexporter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;

import fr.fam.melodyexporter.config.MelodyConfig;
import fr.fam.melodyexporter.config.MelodyLastValueGraphs;
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
    * @throws ScrapExeption ScrapExeption
    * @return scrap
    */
    public final Map<MelodyLastValueGraphs, Double> scrap(final Application application)
            throws ScrapExeption {
        return scrap(application, MelodyLastValueGraphs.values());
    }

    /**
    *
    * @param application application
    * @param graphs graphs
    * @throws ScrapExeption ScrapExeption
    * @return result
    */
    public final Map<MelodyLastValueGraphs, Double> scrap(final Application application,
            final MelodyLastValueGraphs... graphs) throws ScrapExeption {

        Map<MelodyLastValueGraphs, Double> result = new LinkedHashMap<MelodyLastValueGraphs, Double>(graphs.length);

        for (MelodyLastValueGraphs graph : graphs) {
            result.put(graph, -1.0);
        }

        String downloadLastValueData = downloadLastValueData(buildLastValueUrl(application, result.keySet()));
        StringTokenizer rawResultTokens = new StringTokenizer(downloadLastValueData, ",");

        for (MelodyLastValueGraphs graph : result.keySet()) {
            String token = rawResultTokens.nextToken();
            Double value = Double.parseDouble(token);
            if (!value.isNaN()) {
                result.put(graph, value);
            }
        }
        return result;
    }

    /**
    *
    * @param url url
    * @throws ScrapExeption ScrapExeption
    * @return string
    */
    private String downloadLastValueData(final String url) throws ScrapExeption {
        try {
                        LOGGER.debug("Get metrics " + url);
            Request request = Request.Get(url).connectTimeout(config.getTimeout())
                    .socketTimeout(config.getTimeout());
            HttpResponse response = request.execute().returnResponse();
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity());
            }
            throw new ScrapExeption("HTTP-Response code was " + responseCode);
        } catch (IOException e) {
            throw new ScrapExeption("Exception while downloading: " + url, e);
        }
    }

    /**
    *
    * @param application application
    * @param graphs graphs
    * @throws ScrapExeption ScrapExeption
    * @return string
    */
    private String buildLastValueUrl(final Application application,
            final Set<MelodyLastValueGraphs> graphs) {

        StringBuilder sBuilder = new StringBuilder(application.getUrl());

        sBuilder.append(LAST_VALUE_BASE_URL);
        sBuilder.append(GRAPH_PARAMETER);
        int size = graphs.size();

        for (MelodyLastValueGraphs graph : graphs) {
            sBuilder.append(graph.getParameterName());
            if (--size > 0) {
                sBuilder.append(",");
            }
        }
        return sBuilder.toString();
    }
}
