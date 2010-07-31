

This is the project ${projectName} which uses the GwtQuery Library.

- Assuming you have installed maven, compile and install it just running:
$ mvn clean install

- Run it in development mode:
$ mvn gwt:run

- Import and run in Eclipse:

 The archetype generates a project ready to be used in eclipse, 
 but before importing it you have to install the following plugins:

    * Google plugin for eclipse (update-site: http://dl.google.com/eclipse/plugin/3.6)
    * Sonatype Maven plugin (update-site: http://m2eclipse.sonatype.org/site/m2e)
    * Subversion plugin (update-site: http://subclipse.tigris.org/update_1.6.x) 

 Then you can import the project in your eclipse workspace:

    * File -> Import -> Existing Projects into Workspace 

 Finally you should be able to run the project in development mode and to run the gwt test unit.

    * Right click on the project -> Run as -> Web Application
    * Right click on the test class -> Run as -> GWT JUnit Test 
