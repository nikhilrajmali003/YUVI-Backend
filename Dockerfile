# Use official Java 17 image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven wrapper and project files
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies offline
RUN ./mvnw dependency:go-offline

# Copy all source code
COPY src src

# Build the app
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Set the JAR filename
ARG JAR_FILE=target/yuviart-0.0.1-SNAPSHOT.jar

# Run the Spring Boot app
ENTRYPOINT ["java","-jar","/app/${JAR_FILE}"]
