FROM openjdk:11
VOLUME /tmp
COPY target/pridesoft-remotesrv-0.0.1-SNAPSHOT.jar  app.jar
ENV JAVA_OPTS = ""
EXPOSE 10000
ENTRYPOINT ["java",  "-Djava.security.egd=file:/dev/./urandom", "-jar",   "app.jar"]