
This gwt module (JsQuery) allows to export gquery to javascript so as the exported
javascript can be used as a substitute of jquery.

* Compile:
  - mvn package

* Usage
  - Copy the content of the folder target/_version_/jsquery to your webserver
  - Include the script jsquery.nocache.js in your html files instead of jquery.js


This is an experimental feature and not all of the methods in jquery are available yet,

Performance could be worst or best than jquery depending on the cases:
- Gwtexporter needs to compute many runtime operations in order to figure out 
  which method signature to execute and how to wrap gwt objects to js objects and viceversa.
- Once gwtexporter has selected the aproppriate method, gquery performs better in many
  cases like selectors or many operations in IE.

There are many advantages in exporting gquery to js, but the main goal is to use jsni 
code from jquery plugins in gwt without porting this code to java. So developers could
create gquery plugins taking the javascript code of the original plugin, putting it in
a jsni method, and coding in java only the wrapper methods to access the plugin.

Right now most methods in GQuery class are exported via JQ class, but plugin developers
could select just the methods they need for their plugins and reduce the final size of
the generated js code.

First working example using jsquery.js is at:
http://gwtquery.googlecode.com/svn/api/samples/zoom.html


- Manolo
