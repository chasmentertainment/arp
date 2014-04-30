all:

run:
	gradlew -q desktop:run
test:
	gradlew desktop:test
clean:
	gradlew desktop:clean
package:
	gradlew desktop:dist
debug-run:
	gradlew desktop:run --debug
