mainClass = com.chasmentertainment.arpg.App
all:

run:
	mvn compile -q exec:java -Dexec.mainClass=$(mainClass)
debug:
	mvn compile exec:java -Dexec.mainClass=$(mainClass)
compile:
	mvn compile