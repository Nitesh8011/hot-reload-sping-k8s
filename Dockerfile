# Use OpenJDK 21 slim as the base
FROM openjdk:24-jdk

# Copy the “fat JAR” produced by Maven
COPY target/*.jar /app/sample-app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/sample-app.jar"]