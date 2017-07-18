package fr.fam.melodyexporter.config;

import java.util.List;

/**
*
*/
public class Applications {

    /** */
    private List<Application> applications;

    /**
    *
    * @return name
    */
    public final List<Application> getApplications() {
        return applications;
    }

    /**
    *
    * @param papplications applications
    */
    public final void setApplications(final List<Application> papplications) {
        applications = papplications;
    }

    /**
    *
    * @return string
    */
    @Override
    public final String toString() {
        StringBuilder s = new StringBuilder();

        s.append("Applications{");

        for (Application a : applications) {
            s.append(a.toString());
            s.append(", ");
        }

        s.append("}");

        return s.toString();
    }

}
