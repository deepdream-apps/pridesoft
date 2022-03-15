FROM openjdk:11
VOLUME /tmp
COPY target/pridesoft-localsrv-0.0.1-SNAPSHOT.jar  app.jar
COPY src/main/resources/data/keywords  data/keywords
EXPOSE 9999
ENTRYPOINT ["java",  "-Djava.security.egd=file:/dev/./urandom", "-jar",   "app.jar"]