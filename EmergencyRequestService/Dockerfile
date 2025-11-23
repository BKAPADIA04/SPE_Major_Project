# =============================
# 1) Build Stage (Maven + JDK)
# =============================
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven files first (for caching)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies (cached layer)
RUN mvn -B dependency:go-offline

# Copy the rest of the project
COPY src ./src

# Build application
RUN mvn -B clean package -DskipTests


# =============================
# 2) Run Stage (small runtime)
# =============================
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy only the final packaged jar
COPY --from=build /app/target/*.jar app.jar

# Expose the port your app runs on
EXPOSE 8081

# Boot the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
