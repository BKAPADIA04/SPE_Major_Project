# =============================
# 1) Build Stage (Maven + JDK)
# =============================
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven wrapper + config first (cache dependencies)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies (cached)
RUN mvn -B dependency:go-offline

# Copy actual source code
COPY src ./src

# Build JAR
RUN mvn -B clean package -DskipTests


# =============================
# 2) Run Stage (lightweight runtime)
# =============================
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy only the fat JAR from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Spring Boot default port (change if needed)
EXPOSE 8083

# Run the service
ENTRYPOINT ["java", "-jar", "app.jar"]
