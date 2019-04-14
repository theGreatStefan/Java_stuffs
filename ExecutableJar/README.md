How to make the start file:
	- add "package start;" to .java file
	- $javac -d . HelloWorld.java

How to make the manifest.mf file:
	- "Main-class: start.HelloWorld"

How to create the .jar file:
	- $jar -cmf manifest.mf hello.jar start

