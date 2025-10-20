# --------------------------------------------
#  Use an official lightweight Java runtime
# --------------------------------------------
FROM openjdk:17-jdk-slim

# --------------------------------------------
# 2️⃣Set working directory inside the container
# --------------------------------------------
WORKDIR /app

# --------------------------------------------
# 3️⃣copy the JAR file built by Maven into the container
# --------------------------------------------
# This assumes your JAR is created inside target/
COPY target/catalog-service-0.0.1-SNAPSHOT.jar app.jar

# --------------------------------------------
# Expose the port your Spring Boot app runs on
# --------------------------------------------
EXPOSE 8080

# --------------------------------------------
# Run the application
# --------------------------------------------
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

