FROM mdsol/java11-jdk:latest
ARG DEBIAN_FRONTEND=noninteractive
ENV TZ=Asia/Seoul
RUN apt-get install -y tzdata
RUN mkdir -p /app
WORKDIR /app
COPY target/ra-0.3.1.jar ra-0.3.1.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["java", "-jar", "ra-0.3.1.jar"]