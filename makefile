JAVA = java
JAVAC = javac

all: compile run

clean:
	rm -rf bin;

compile:
	mkdir -p bin;
	$(JAVAC) src/perspective/*.java -d bin;

run:
	cd ./bin/; java perspective.Engine;