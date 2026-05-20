FROM gradle:8.10.2-jdk21 AS build
WORKDIR /app
COPY gradle.properties settings.gradle.kts build.gradle.kts gradlew gradlew.bat ./
COPY gradle ./gradle
COPY src ./src
RUN gradle --no-daemon shadowJar -x test \
    && JAR=$(ls build/libs/*-all.jar | head -n1) \
    && cp "$JAR" /app/audit-service.jar

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/audit-service.jar /app/audit-service.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "/app/audit-service.jar"]
