mvn install:install-file \
   -Dfile=lib/Blooming.jar \
   -DgroupId=local-jar \
   -DartifactId=blooming-jar \
   -Dversion=1 \
   -Dpackaging=jar \
   -DgeneratePom=true
