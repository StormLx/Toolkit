FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy the Maven wrapper and the pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies (this layer will be cached)
RUN ./mvnw dependency:go-offline

# Copy the rest of the application code
COPY src ./src

# Expose the application port and debug port
EXPOSE 8080 5005

# Run with Maven for hot reload support
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.jvmArguments=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"]
