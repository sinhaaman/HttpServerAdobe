FROM openjdk:8-jdk-alpine

RUN apk add --update curl
RUN mkdir -p /adobe/httpserver
ARG JAR_FILE=target/HTTPServer-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} /adobe/httpserver/app.jar
COPY pages/ /adobe/httpserver/pages
WORKDIR /adobe/httpserver/
EXPOSE 8080
ENTRYPOINT ["java","-jar","/adobe/httpserver/app.jar"]
CMD ["-i", "./pages/pages_1"]