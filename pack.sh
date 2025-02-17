mkdir bin
javac -cp lib/jsch-0.1.55.jar src/main/java/org/example/*.java -encoding UTF8 -d bin/
cd bin
mkdir lib
cp ../lib/jsch-0.1.55.jar lib/
jar cfm app.jar ../src/main/resources/META-INF/MANIFEST.MF org/
