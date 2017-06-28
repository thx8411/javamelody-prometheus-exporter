package fr.fam.javamelodyprometheusexporter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;

import fr.fam.javamelodyprometheusexporter.config.JavaMelodyLastValueGraphs;

/**
*/
public class JavaMelodyScraper {

	private static final Logger logger = Logger
			.getLogger(JavaMelodyScraper.class);

	private static final int TIMEOUT = 5000;

	private static final String LAST_VALUE_BASE_URL = "?part=lastValue";
	private static final String GRAPH_PARAMETER = "&graph=";
	private static final String APPLICATION_PARAMETER = "&application=";

	/**
        */
	public Map<JavaMelodyLastValueGraphs, Double> scrap() throws ScrapExeption {
		return scrap(null, JavaMelodyLastValueGraphs.values());
	}

	/**
        */
	public Map<JavaMelodyLastValueGraphs, Double> scrap(String application)
			throws ScrapExeption {
		return scrap(application, JavaMelodyLastValueGraphs.values());
	}

	/**
        */
	public Map<JavaMelodyLastValueGraphs, Double> scrap(
			JavaMelodyLastValueGraphs... graphs) throws ScrapExeption {
		return scrap(null, graphs);
	}

	/**
        */
	public Map<JavaMelodyLastValueGraphs, Double> scrap(String application,
			JavaMelodyLastValueGraphs... graphs) throws ScrapExeption {
		Map<JavaMelodyLastValueGraphs, Double> result = new LinkedHashMap<JavaMelodyLastValueGraphs, Double>(
				graphs.length);
		for (JavaMelodyLastValueGraphs graph : graphs) {
			result.put(graph, -1.0);
		}
		String downloadLastValueData = downloadLastValueData(buildLastValueUrl(
				application, result.keySet()));
		StringTokenizer rawResultTokens = new StringTokenizer(
				downloadLastValueData, ",");
		for (JavaMelodyLastValueGraphs graph : result.keySet()) {
			String token = rawResultTokens.nextToken();
			double value = Double.parseDouble(token);
			result.put(graph, value);
		}
		return result;
	}

	/**
        */
	private String downloadLastValueData(String url) throws ScrapExeption {
		try {
			Request request = Request.Get(url).connectTimeout(TIMEOUT)
					.socketTimeout(TIMEOUT);
			HttpResponse response = request.execute().returnResponse();
			int responseCode = response.getStatusLine().getStatusCode();
			if (responseCode == 200) {
				return EntityUtils.toString(response.getEntity());
			}
			throw new ScrapExeption("HTTP-Response code was " + responseCode);
		} catch (IOException e) {
			throw new ScrapExeption("Exception while downloading: " + url, e);
		}
	}

	/**
        */
	private String buildLastValueUrl(String application,
			Set<JavaMelodyLastValueGraphs> graphs) {
		StringBuilder sBuilder = new StringBuilder(application);

		sBuilder.append(LAST_VALUE_BASE_URL);
		sBuilder.append(GRAPH_PARAMETER);
		int size = graphs.size();
		for (JavaMelodyLastValueGraphs graph : graphs) {
			sBuilder.append(graph.getParameterName());
			if (--size > 0) {
				sBuilder.append(",");
			}
		}
		if (application != null) {
			sBuilder.append(APPLICATION_PARAMETER);
		}
		return sBuilder.toString();
	}
}
