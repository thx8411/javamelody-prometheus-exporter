package fr.fam.javamelodyprometheusexporter.config;

/**
*
*/
public enum JavaMelodyLastValueGraphs {

    /** */
    HTTP_HITS_RATE("httpHitsRate"),
    /** */
    HTTP_MEAN_TIMES("httpMeanTimes"),
    /** */
    HTTP_SYSTEM_ERRORS("httpSystemErrors"),
    /** */
    TOMCAT_BUSY_THREADS("tomcatBusyThreads"),
    /** */
    TOMCAT_BYTES_RECEIVED("tomcatBytesReceived"),
    /** */
    TOMCAT_BYTES_SENT("tomcatBytesSent"),
    /** */
    USED_MEMORY("usedMemory"),
    /** */
    CPU("cpu"),
    /** */
    HTTP_SESSIONS("httpSessions"),
    /** */
    ACTIVE_THREADS("activeThreads"),
    /** */
    ACTIVE_CONNECTIONS("activeConnections"),
    /** */
    USED_CONNECTIONS("usedConnections"),
    /** */
    GARBAGE_COLLECTOR("gc"),
    /** */
    THREAD_COUNT("threadCount"),
    /** */
    LOADED_CLASSES_COUNT("loadedClassesCount"),
    /** */
    USED_NON_HEAP_MEMORY("usedNonHeapMemory"),
    /** */
    USED_PHYSICAL_MEMORY_SIZE("usedPhysicalMemorySize"),
    /** */
    USED_SWAP_SPACE_SIZE("usedSwapSpaceSize"),
    /** */
    HTTP_SESSIONS_MEAN_AGE("httpSessionsMeanAge"),
    /** */
    SQL_HITS_RATE("sqlHitsRate"),
    /** */
    SQL_MEAN_TIMES("sqlMeanTimes"),
    /** */
    SQL_SYSTEM_ERRORS("sqlSystemErrors"),
    /** */
    OPEN_FILES("fileDescriptors");

    /**
    *
    */
    private String parameterName;

    /**
    *
    * @param pparameterName parameterName
    */
    private JavaMelodyLastValueGraphs(final String pparameterName) {
        this.parameterName = pparameterName;
    }

    /**
    *
    * @return parameterName
    */
    public String getParameterName() {
        return parameterName;
    }

}
