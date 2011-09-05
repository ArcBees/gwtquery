
This gwt module (JsQuery) allows to export gquery to javascript so as the exported
javascript can be used as a substitute of jquery.

* Compile:
  - mvn package

* Usage
  - Copy the content of the folder target/_version_/jsquery to your webserver
  - Include the script jsquery.nocache.js in your html files instead of jquery.js


This is an experimental feature and not all of the methods in jquery are available,
performance could be worst than jquery since gwtexporter needs to compute many runtime
operations in order to figure out which method execute and to wrap gwt objects to js
objects and viceversa.

There are many advantages in exporting gquery to js, but the main goal is to use jsni 
code from jquery plugins in gwt without porting this code to java.

Right now most methods in GQuery class are exporte via JQ class, but plugin developers
could select just the methods they need for their plugins.



- Manolo
