package fr.fam.melodyexporter;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import fr.fam.melodyexporter.config.MelodyConfig;
import io.prometheus.client.exporter.common.TextFormat;

/**
*
*/
public class MetricsServlet extends HttpServlet {

    /** */
    private static final long serialVersionUID = 1L;

    /** */
    private static final Logger LOGGER = Logger.getLogger(MetricsServlet.class);

    /** */
    private static MelodyConfig config = new MelodyConfig();

    /** */
    private static final MelodyCollector COLLECTOR = new MelodyCollector(config).register();

    /**
    *
    * @param request request
    * @param response response
    * @throws ServletException ServletException
    * @throws IOException IOException
    */
    protected final void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        Writer writer = response.getWriter();
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(TextFormat.CONTENT_TYPE_004);
            TextFormat.write004(writer, Collections.enumeration(COLLECTOR.collect()));
            writer.flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            LOGGER.error("Failure during scrap", e);
        } finally {
            writer.close();
        }
    }

}
