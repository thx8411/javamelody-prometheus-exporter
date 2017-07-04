package fr.fam.melodyexporter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import fr.fam.melodyexporter.config.MelodyConfig;
import fr.fam.melodyexporter.config.MelodyLastValueGraphs;
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
    * @throws ScrapExeption ScrapExeption
    * @return mfs
    */
    private List<MetricFamilySamples> buildServerMetricFamilySamples()
            throws ScrapExeption {
        LOGGER.debug("Building samples...");

        List<MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        Map<String, Map<MelodyLastValueGraphs, Double>> scrapResults = scrapServers();
        Set<MelodyLastValueGraphs> keySet = scrapResults.values().iterator().next().keySet();

        for (MelodyLastValueGraphs graph : keySet) {

            GaugeMetricFamily gauge = new GaugeMetricFamily(NAMESPACE + "_" + graph.getParameterName(),
                "Help for " + graph.getParameterName(), Arrays.asList("application"));

            for (Application application : applications.getApplications()) {
                gauge.addMetric(Arrays.asList(application.getName()), scrapResults.get(application.getName()).get(graph));
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
        LOGGER.debug("Scrapping servers...");

        Map<String, Map<MelodyLastValueGraphs, Double>> scrapResults =
            new HashMap<String, Map<MelodyLastValueGraphs, Double>>(applications.getApplications().size());

        for (Application application : applications.getApplications()) {
            scrapResults.put(application.getName(), scraper.scrap(application));
        }
        return scrapResults;
    }
}
