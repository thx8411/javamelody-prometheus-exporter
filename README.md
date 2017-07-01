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

### Example javamelody.properties

	# Comma seperated list of application's melody monitoring url
	# Credentials for basic authentification provided in urls

	# Example :

	# javamelody.collector.applications= \
	# https://user:password@app1.mydomain.com/app1/monitoring,\
	# http://app2.mydomain.com/app2/monitoring	
