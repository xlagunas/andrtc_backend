# ---- Build stage ----
FROM eclipse-temurin:8-jdk AS build
WORKDIR /app

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle ./
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true

COPY src ./src
RUN ./gradlew bootJar --no-daemon -x test

# ---- Runtime stage ----
FROM eclipse-temurin:8-jre
WORKDIR /app

# Run as non-root
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

COPY --from=build /app/build/libs/*.jar andrtc.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "andrtc.jar"]