# =============================
# 1) Build Stage (Maven + JDK)
# =============================
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven files first for dependency caching
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Pre-download dependencies for faster builds
RUN mvn -B dependency:go-offline

# Copy the rest of your source code
COPY src ./src

# Build the application JAR
RUN mvn -B clean package -DskipTests


# =============================
# 2) Run Stage (lightweight JRE)
# =============================
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the packaged JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port (adjust if your app uses a different one)
EXPOSE 8082

# Start the Spring Boot service
ENTRYPOINT ["java", "-jar", "app.jar"]
