package fr.fam.melodyexporter.config;

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
    private String[] labels;

    /** */
    private String[] metrics;

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
    * @return labels
    */
    public final String[] getLabels() {
        return labels;
    }

    /**
    *
    * @param plabels application labels
    */
    public final void setLabels(final String[] plabels) {
        labels = plabels;
    }

    /**
    *
    * @return metrics
    */
    public final String[] getMetrics() {
        return metrics;
    }

    /**
    *
    * @param pmetrics application metrics
    */
    public final void setMetrics(final String[] pmetrics) {
        metrics = pmetrics;
    }

    /**
    *
    * @return string
    */
    @Override
    public final String toString() {
        StringBuilder s = new StringBuilder();

        s.append("Applications{");
        s.append("name=" + name + ", ");
        s.append("url=" + url + ", ");
        s.append("login=" + login + ", ");
        s.append("password=" + "*******" + ", ");

        // browsing labels
        s.append("labels=[");
        for(String l : labels) {
            s.append(l);
            s.append(", ");
        }
        s.append("]");
        s.append(", ");

        // browsing metrics
        s.append("metrics=[");
        for(String m : metrics) {
            s.append(m);
            s.append(", ");
        }
        s.append("]");
        s.append("}");

        return s.toString();
    }
}
