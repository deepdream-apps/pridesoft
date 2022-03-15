FROM openjdk:11
VOLUME /tmp
COPY target/pridesoft-web-0.0.1-SNAPSHOT.jar  app.jar
ENV JAVA_OPTS = ""
EXPOSE 8085
ENTRYPOINT ["java",  "-Djava.security.egd=file:/dev/./urandom", "-jar",   "app.jar"]