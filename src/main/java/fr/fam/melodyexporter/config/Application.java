package fr.fam.melodyexporter.config;

import java.util.List;

/**
*
*/
public class Application {
    /** */
    private String name;

    /** */
    private String url;

    /** */
    private String login;

    /** */
    private String password;

    /** */
    private List<Metric> metrics;

    /**
    *
    * @return name
    */
    public final String getName() {
        return name;
    }

    /**
    *
    * @param pname application name
    */
    public final void setName(final String pname) {
        name = pname;
    }

    /**
    *
    * @return url
    */
    public final String getUrl() {
        return url;
    }

    /**
    *
    * @param purl application url
    */
    public final void setUrl(final String purl) {
        url = purl;
    }

    /**
    *
    * @return login
    */
    public final String getLogin() {
        return login;
    }

    /**
    *
    * @param plogin application login
    */
    public final void setLogin(final String plogin) {
        login = plogin;
    }

    /**
    *
    * @return password
    */
    public final String getPassword() {
        return password;
    }

    /**
    *
    * @param ppassword application password
    */
    public final void setPassword(final String ppassword) {
        password = ppassword;
    }

    /**
    *
    * @return metrics
    */
    public final List<Metric> getMetrics() {
        return metrics;
    }

    /**
    *
    * @return metric
    */
    public final Metric getMetric(String pname) {
        for (Metric m : metrics) {
            if (m.getName().equals(pname)) {
                return m;
            }
        }
        // if not found
        return null;
    }

    /**
    *
    * @param pmetrics application metrics
    */
    public final void setMetrics(final List<Metric> pmetrics) {
        metrics = pmetrics;
    }

    /**
    *
    * @return string
    */
    @Override
    public final String toString() {
        StringBuilder s = new StringBuilder();

        s.append("name=" + name + ", ");
        s.append("url=" + url + ", ");
        s.append("login=" + login + ", ");
        s.append("password=" + "*******" + ", ");

        // browsing metrics
        s.append("metrics={");
        for (Metric m : metrics) {
            s.append(m.toString());
            s.append(", ");
        }
        s.append("}");

        return s.toString();
    }
}
