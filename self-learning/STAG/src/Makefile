default: build

build:
	javac -classpath ./libs/dot-parser.jar:./libs/json-parser.jar:. StagServer.java

run: build
	java -classpath ./libs/dot-parser.jar:./libs/json-parser.jar:. StagServer data2/entities.dot data2/actions.json

runmain:
	java -classpath ./libs/dot-parser.jar:./libs/json-parser.jar:. Main data2/entities.dot

clean:
	rm *.class
