JFLAGS = -g
JC = javac
JVM = java

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	  ProjectServer.java \
	  Chat_server.java \
	  Client_GUI.java \
	  Client.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

