package fr.fam.melodyexporter.config;

import java.util.Map;

/**
*
*/
public class Metric {
    /** */
    private String name;

    /** */
    private Map<String, String> labels;

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
    * @return labels
    */
    public final Map<String, String> getLabels() {
        return labels;
    }

    /**
    *
    * @param plabels application labels
    */
    public final void setLabels(final Map<String, String> plabels) {
        labels = plabels;
    }

    /**
    *
    * @return string
    */
    @Override
    public final String toString() {
        StringBuilder s = new StringBuilder();

        s.append("name=" + name + ", ");

        // browsing labels
        s.append("labels={");
        for (Map.Entry<String, String> e : labels.entrySet()) {
            s.append("[");
            s.append(e.getKey());
            s.append(", ");
            s.append(e.getValue());
            s.append("], ");
        }
        s.append("}");

        return s.toString();
    }
}
