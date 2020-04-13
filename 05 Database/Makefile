default: builds

builds:
	javac DBServer.java

buildc:
	javac DBClient.java

runs: builds
	java DBServer

runc: buildc
	java DBClient

clean:
	rm *.class
