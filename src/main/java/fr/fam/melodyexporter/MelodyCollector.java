package fr.fam.melodyexporter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import fr.fam.melodyexporter.config.MelodyLastValueGraphs;
import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;

/**
*
*/
public class MelodyCollector extends Collector {

    /**
    *
    */
    private static final Logger LOGGER = Logger
            .getLogger(MelodyCollector.class);

    /**
    *
    */
    public static final String NAMESPACE = "javamelody";

    /**
    *
    */
    private MelodyScraper scraper;

    /**
    *
    */
    private String[] applications;

    /**
    *
    * @param papplications application's urls list
    */
    public MelodyCollector(final String... papplications) {
        super();
        this.scraper = new MelodyScraper();
        this.applications = papplications;
    }

    /**
    */
    @Override
    public final List<MetricFamilySamples> collect() {
        try {
            return buildServerMetricFamilySamples();
        } catch (Exception e) {
            LOGGER.error("Error while collecting data.", e);
            throw new IllegalStateException(e);
        }
    }

    /**
    *
    * @throws ScrapExeption ScrapExeption
    * @return mfs
    */
    private List<MetricFamilySamples> buildServerMetricFamilySamples()
            throws ScrapExeption {
        List<MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        Map<String, Map<MelodyLastValueGraphs, Double>> scrapResults = scrapServers();
        Set<MelodyLastValueGraphs> keySet = scrapResults.values().iterator().next().keySet();
        for (MelodyLastValueGraphs graph : keySet) {
            GaugeMetricFamily gauge = new GaugeMetricFamily(NAMESPACE + "_" + graph.getParameterName(),
                "Help for " + graph.getParameterName(), Arrays.asList("application"));
            for (String application : applications) {
                gauge.addMetric(Arrays.asList(application), scrapResults.get(application).get(graph));
            }
            mfs.add(gauge);
        }
        return mfs;
    }

    /**
    *
    * @throws ScrapExeption ScrapExeption
    * @return scrap
    */
    private Map<String, Map<MelodyLastValueGraphs, Double>> scrapServers()
            throws ScrapExeption {
        LOGGER.debug("Scrapping collector server for application: " + Arrays.toString(applications) + "...");
        Map<String, Map<MelodyLastValueGraphs, Double>> scrapResults =
            new HashMap<String, Map<MelodyLastValueGraphs, Double>>(applications.length);
        for (String application : applications) {
            scrapResults.put(application, scraper.scrap(application));
        }
        return scrapResults;
    }
}
