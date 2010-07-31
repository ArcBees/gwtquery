

This is the archetype to create new GwtQuery applications.

- Assuming you have installed maven, compile and install it just running:
$ mvn clean install

- To use the archetype run:
$ mvn archetype:generate  -DarchetypeGroupId=com.googlecode.gwtquery \
                          -DarchetypeArtifactId=gquery-archetype  \
                          -DarchetypeVersion=0.1
                          -DartifactId=myproject \
                          -DpluginName=MyProject 

- Then change to the folder myplugin and run:
$ mvn clean install

- To run the application just run:
$ mvn gwt:run
