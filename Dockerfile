FROM openjdk:21
WORKDIR /app
COPY target/stock-management-back-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
