package fr.fam.javamelodyprometheusexporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import fr.fam.javamelodyprometheusexporter.config.JavaMelodyLastValueGraphs;
import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;

/**
*
*/
public class JavaMelodyPrometheusCollector extends Collector {

    /**
    *
    */
    private static final Logger LOGGER = Logger
            .getLogger(JavaMelodyPrometheusCollector.class);

    /**
    *
    */
    public static final String NAMESPACE = "javamelody";

    /**
    *
    */
    private JavaMelodyScraper scraper;

    /**
    *
    */
    private String[] applications;

    /**
    *
    * @param papplications application's urls list
    */
    public JavaMelodyPrometheusCollector(final String... papplications) {
        super();
        this.scraper = new JavaMelodyScraper();
        this.applications = papplications;
    }

    /**
    */
    @Override
    public final List<MetricFamilySamples> collect() {
        try {
            return buildSingleServerMetricFamilySamples();
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
    private List<MetricFamilySamples> buildSingleServerMetricFamilySamples()
            throws ScrapExeption {
        List<MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
        Map<JavaMelodyLastValueGraphs, Double> scrapResults = scrapSingleServer();
        for (JavaMelodyLastValueGraphs graph : scrapResults.keySet()) {
            mfs.add(new GaugeMetricFamily(NAMESPACE + "_"
                    + graph.getParameterName(), "Help for "
                    + graph.getParameterName(), scrapResults.get(graph)));
        }
        return mfs;
    }

    /**
    *
    * @throws ScrapExeption ScrapExeption
    * @return scrap
    */
    private Map<JavaMelodyLastValueGraphs, Double> scrapSingleServer()
            throws ScrapExeption {
        LOGGER.debug("Scrapping single server");
        return scraper.scrap();
    }
}
