FROM amazoncorretto:17-alpine-jdk
COPY out/artifacts/TinderBolt_jar/TinderBolt.jar app.jar
ARG JAR_FILE=out/artifacts/TinderBolt_jar/TinderBolt.jar
ENTRYPOINT ["java","-jar","/app.jar"]