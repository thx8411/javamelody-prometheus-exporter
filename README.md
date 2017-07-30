# MelodyExporter

This is a prometheus exporter (<https://prometheus.io/docs/instrumenting/exporters>) for the JavaEE monitoring tool [JavaMelody](https://github.com/javamelody/javamelody/wiki). It was created to get a deeper insight of the data JavaMelody collects, especially over time without the need to change existing application code to full prometheus monitoring.

## Why Java?

Instead of using Go or Python this exporter uses Java because you probably already own a Java infrastructure when running JavaMelody so you can deploy this exporter right next to your application.

## How it works

This exporter uses the JavaMelody [lastValue external API](https://github.com/javamelody/javamelody/wiki/ExternalAPI#png-and-lastvalue) to pull data and transforms it to Prometheus gauges. Exporting is done via a simple Java servlet.

## How to use it
The build is maven based so a `mvn package` will create the war file in the `target` folder.

Before doing this you have to create your own `melodyexporter.yml` file and put it in your classpath (with Tomcat, use ${catalina.home}/common/classes/)

Afterwards just deploy the war file on an application server or web container of your choice. (tested with Tomcat 7 & Payara 4.1.1, Java EE 6 or above required)

Prometheus don't use the application's "context". MelodyExporter must be deployed using the "ROOT" context (example : http://localhost:8080/ not http://localhost:8080/melodyexporter/).
On Tomcat, just remove the "webapps/ROOT" folder and rename MelodyExporter as "ROOT.war" before deployement.

MelodyExporter also provides an exposed javamelody servlet for self monitoring.

The log level can be modified in the log4j.properties file.

All metrics use the application name as metric prefix.

### Example melodyexporter.yml

	# java melody exporter settings

	timeout: 5000

	#
	---
	# java melody applications to scrap
	
	applications:
	
	#
	# Model :
	#
	#- name:           (mandatory)
	#  url:            (mandatory)
	#  login:          (optional)
	#  password:       (optional)
	#  metrics:        (mandatory)
	#  - name: metric1 (mandatory)
	#    labels:       (optional)
	#      label1: value1
	#      label2: value2
	#      ...
	#  - name: metric2
	#  - name: metric3
	#    labels:
	#      ...
	#  - ...
	#
	# where metrics can be :
	#
	# httpHitsRate
	# httpMeanTimes
	# httpSystemErrors
	# tomcatBusyThreads
	# tomcatBytesReceived
	# tomcatBytesSent
	# usedMemory
	# cpu
	# httpSessions
	# activeThreads
	# activeConnections
	# usedConnections
	# gc
	# threadCount
	# loadedClassesCount
	# usedNonHeapMemory
	# usedPhysicalMemorySize
	# usedSwapSpaceSize
	# httpSessionsMeanAge
	# sqlHitsRate
	# sqlMeanTimes
	# sqlSystemErrors
	# fileDescriptors
	#   
	
	# predefined entry for self monitoring
	- name: melodyexporter
	  url: "http://localhost:8080/melodyexporter/monitoring"
	  login:
	  password:
	  metrics:
	  - name: cpu
	    labels:
	      env: monitoring
	  - name: usedMemory
	    labels:
	      env: monitoring
	
	# 
	...
	# 

## Notes

* To avoid logs flooding, stack traces are ignored (only the first line is reported).
In order to get the full stacke traces, remove "%throwable{1}" in the log4j.properties file.

* CAUTION : Tabulations are forbidden in yaml files.
