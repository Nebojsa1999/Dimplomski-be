FROM gradle:8.7-jdk17 AS build

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle gradle
COPY gradlew .
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon || true

COPY src src

RUN ./gradlew build -x test --no-daemon

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]