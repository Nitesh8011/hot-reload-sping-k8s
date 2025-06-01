# Use OpenJDK 21 slim as the base
FROM openjdk:21-jdk

# (Optional) Create a non-root user
RUN groupadd --system appgroup && useradd --system --gid appgroup appuser

USER appuser

# Copy the “fat JAR” produced by Maven
COPY target/*.jar /app/sample-app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/sample-app.jar"]