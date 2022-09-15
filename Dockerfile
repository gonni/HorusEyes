FROM openjdk:8-jdk-alpine
RUN mkdir -p /app
WORKDIR /app
COPY target/ra-0.3.1.jar ra-0.3.1.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["java", "-jar", "ra-0.3.1.jar"]