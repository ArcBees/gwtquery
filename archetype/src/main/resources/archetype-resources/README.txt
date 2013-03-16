

This is the project ${projectName} which uses the GwtQuery Library.

- Assuming you have installed maven, compile and install it just running:
$ mvn clean install

- Run it in development mode:
$ mvn gwt:run
and point your browser to http://127.0.0.1:8888/${projectName}/${projectName}.html?gwt.codesvr=127.0.0.1:9997

- Run superdev mode:
$ mvn gwt:run-codeserver
and point your browser to http://127.0.0.1:9876/${projectName}/${projectName}.html

- Import and run in Eclipse:

 The archetype generates a project ready to be used in eclipse, 
 but before importing it you have to install the following plugins:

    * Google plugin for eclipse (update-site: http://dl.google.com/eclipse/plugin/3.7 or 3.6 or 3.5)
    * Sonatype Maven plugin (update-site: http://m2eclipse.sonatype.org/site/m2e)

 Then you can import the project in your eclipse workspace:

    * File -> Import -> Existing Projects into Workspace 

 Finally you should be able to run the project in development mode and to run the gwt test unit.

    * Right click on the project -> Run as -> Web Application
    * Right click on the test class -> Run as -> GWT JUnit Test 

- Although the project has the files .classpath and .project, you could generate them running any 
 of the following commands:

$ mvn eclipse:m2eclipse  (if you like to use m2eclipse)
$ mvn eclipse:eclipse    (to use the project without m2eclipse)
