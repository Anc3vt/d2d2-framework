#!/bin/bash
mvn install:install-file \
   -Dfile=lib/Blooming.jar \
   -DgroupId=local-jar \
   -DartifactId=blooming-jar \
   -Dversion=1 \
   -Dpackaging=jar \
   -DgeneratePom=true

mvn install:install-file \
   -Dfile=lib/vorbisspi-1.0.3.3.jar \
   -DgroupId=local-jar \
   -DartifactId=vorbisspi \
   -Dversion=1.0.3.3 \
   -Dpackaging=jar \
   -DgeneratePom=true
