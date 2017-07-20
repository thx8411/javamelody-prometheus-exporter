package fr.fam.melodyexporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import fr.fam.melodyexporter.config.MelodyConfig;
import fr.fam.melodyexporter.config.Applications;
import fr.fam.melodyexporter.config.Application;
import fr.fam.melodyexporter.config.Metric;
import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;

/**
*
*/
public class MelodyCollector extends Collector {

    /** */
    private static final Logger LOGGER = Logger.getLogger(MelodyCollector.class);

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
        LOGGER.debug("Building samples...");

        List<MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();

        // for each app
        for (Application application : applications.getApplications()) {
            // scrap metrics
            Map<String, Double> result = scraper.scrap(application);

            // for each metric
            for (Map.Entry<String, Double> entry : result.entrySet()) {
                // look for metric and labels
                Metric m = application.getMetric(entry.getKey());
                if (m == null) {
                    LOGGER.error("Missing metric " + entry.getKey());
                    throw new IllegalStateException("Missing metric " + entry.getKey());
                }
                List<String> labelKeys = new ArrayList<String>();
                List<String> labelValues = new ArrayList<String>();
                for (Map.Entry<String, String> e : m.getLabels().entrySet()) {
                    labelKeys.add(e.getKey());
                    labelValues.add(e.getValue());
                }

                // create gauge
                GaugeMetricFamily gauge = new GaugeMetricFamily(application.getName() + "_" + entry.getKey(),
                    "Help for " + entry.getKey(), labelKeys);

                // populate gauge
                gauge.addMetric(labelValues, entry.getValue());
                mfs.add(gauge);
            }
        }
        return mfs;
    }

}
