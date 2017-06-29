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
    private static final Logger logger = Logger
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
    */
    public JavaMelodyPrometheusCollector(String... applications) {
        super();
        this.scraper = new JavaMelodyScraper();
        this.applications = applications;
    }

    /**
    */
    @Override
    public List<MetricFamilySamples> collect() {
        try {
            return buildSingleServerMetricFamilySamples();
        } catch (Exception e) {
            logger.error("Error while collecting data.", e);
            throw new IllegalStateException(e);
        }
    }

    /**
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
        */
    private Map<JavaMelodyLastValueGraphs, Double> scrapSingleServer()
            throws ScrapExeption {
        logger.debug("Scrapping single server");
        return scraper.scrap();
    }
}
