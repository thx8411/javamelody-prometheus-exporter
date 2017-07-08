# MelodyExporter

This is a prometheus exporter (<https://prometheus.io/docs/instrumenting/exporters>) for the JavaEE monitoring tool [JavaMelody](https://github.com/javamelody/javamelody/wiki). It was created to get a deeper insight of the data JavaMelody collects, especially over time without the need to change existing application code to full prometheus monitoring.

## Why Java?

Instead of using Go or Python this exporter uses Java because you probably already own a Java infrastructure when running JavaMelody so you can deploy this exporter right next to your application.

## How it works

This exporter uses the JavaMelody [lastValue external API](https://github.com/javamelody/javamelody/wiki/ExternalAPI#png-and-lastvalue) to pull data and transforms it to Prometheus gauges. Exporting is done via a simple Java servlet.

## How to use it
The build is maven based so a `mvn package` will create the war file in the `target` folder.

Before doing this you have to create your own `melodyexporter.properties` file and put it in your classpath (with Tomcat, use ${catalina.home}/common/classes/)

Afterwards just deploy the war file on an application server or web container of your choice. (Tested with Tomcat 7 & Payara 4.1.1, Java EE 6 or above required)

MelodyExporter also provides an exposed javamelody servlet for self monitoring.

The log level can be modified in the log4j.properties file.

All metrics use the application name as metric prefix.

### Example melodyexporter.properties

	# Timeout for metrics scraping
	javamelody.timeout=5000

	# applications to scrap in a yaml file (see yaml example)
	javamelody.applications.file=melodyexporter.yml

### Example melodyexporter.yml

	# java melody applications to scrap
	---
	#
	
	applications:
	
	#
	# Model :
	#
	#-
	#   name:
	#   url:
	#   login:
	#   password:
	#   labels:
	#     - "label1=value1"
	#     - "label2=value2"
	#     - ...
	#   metrics:
	#     - metric1
	#     - metric2
	#     - ...
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
	-
  	  name: melodyexporter
  	  url: "http://localhost:8080/melodyexporter/monitoring"
  	  login:
  	  password:
  	  labels:
    	  - "env=monitoring"
  	  metrics:
    	  - cpu
    	  - usedMemory
	
	# 
	...
	# 

CAUTION : Tabulations are forbidden in yaml files.
