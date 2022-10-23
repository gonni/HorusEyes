FROM openjdk:8-jdk-alpine
ENV TZ=Asia/Seoul
RUN mkdir -p /app
WORKDIR /app
COPY target/ra-0.3.1.jar ra-0.3.1.jar
ENTRYPOINT ["java", "-jar", "ra-0.3.1.jar"]