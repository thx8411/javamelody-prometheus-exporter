package fr.fam.melodyexporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import fr.fam.melodyexporter.config.MelodyConfig;
import fr.fam.melodyexporter.config.Applications;
import fr.fam.melodyexporter.config.Application;
import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;

/**
*
*/
public class MelodyCollector extends Collector {

    /** */
    private static final Logger LOGGER = Logger.getLogger(MelodyCollector.class);

    /** */
    public static final String NAMESPACE = "javamelody";

    /** */
    private MelodyScraper scraper;

    /** */
    private MelodyConfig config;

    /** */
    private Applications applications;

    /**
    *
    * @param pconfig configuration
    */
    public MelodyCollector(final MelodyConfig pconfig) {
        super();
        config = pconfig;
        applications = config.getApplications();
        scraper = new MelodyScraper(config);
    }

    /**
    *
    * @throws IllegalStateException
    */
    @Override
    public final List<MetricFamilySamples> collect() throws IllegalStateException {
        LOGGER.debug("Collect...");

        try {
            return buildServerMetricFamilySamples();
        } catch (Exception e) {
            LOGGER.error("Error while collecting data.", e);
            throw new IllegalStateException(e);
        }
    }

    /**
    *
    * @throws ScrapException ScrapException
    * @return mfs
    */
    private List<MetricFamilySamples> buildServerMetricFamilySamples()
            throws ScrapException {
        LOGGER.debug("Building samples...");

        List<MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        Map<String, Map<String, Double>> scrapResults = scrapServers();
        Set<String> keySet = scrapResults.values().iterator().next().keySet();

        for (String graph : keySet) {

             for (Application application : applications.getApplications()) {

                // extract labels
                List<String> labelNames = new ArrayList<String>();
                List<String> labelValues = new ArrayList<String>();
                labelNames.add("application");
                labelValues.add(application.getName());
                for (String s : application.getLabels()) {
                    labelNames.add(s.split("=")[0]);
                    labelValues.add(s.split("=")[1]);
                }

                GaugeMetricFamily gauge = new GaugeMetricFamily(NAMESPACE + "_" + graph,
                    "Help for " + graph, labelNames);

                gauge.addMetric(labelValues, scrapResults.get(application.getName()).get(graph));
                mfs.add(gauge);
            }
        }
        return mfs;
    }

    /**
    *
    * @throws ScrapException ScrapException
    * @return scrap
    */
    private Map<String, Map<String, Double>> scrapServers()
            throws ScrapException {
        LOGGER.debug("Scrapping servers...");

        Map<String, Map<String, Double>> scrapResults =
            new HashMap<String, Map<String, Double>>(applications.getApplications().size());

        for (Application application : applications.getApplications()) {
            scrapResults.put(application.getName(), scraper.scrap(application));
        }
        return scrapResults;
    }
}
