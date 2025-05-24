FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy the Maven wrapper and the pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies (this layer will be cached)
RUN ./mvnw dependency:go-offline

# Copy the rest of the application code
COPY src ./src

# Package the application (without running tests for faster build in dev)
# Spring Boot DevTools will be included, enabling hot reload
RUN ./mvnw package -DskipTests

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/*.jar"]
