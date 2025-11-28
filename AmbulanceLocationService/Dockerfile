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
RUN ./mvnw -B dependency:go-offline || mvn -B dependency:go-offline

# Copy src only after dependency cache is built
COPY src ./src

# Build JAR
RUN ./mvnw -B clean package -DskipTests || mvn -B clean package -DskipTests


# =============================
# 2) Run Stage (super lightweight)
# =============================
# Use JRE instead of JDK → MUCH smaller
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy only the JAR file from the build stage
COPY --from=build /app/target/*jar app.jar

# Runtime port
EXPOSE 8083

# Better: Enable memory optimizations for containers
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
