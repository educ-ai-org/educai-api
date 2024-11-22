FROM maven: 3-openjdk-17 as builder
LABEL authors="diegosousa"

WORKDIR /build

COPY .

RUN mvn clean package -DskipTests -Dcheckstyle.skip=true

FROM openjdk:17-slim

WORKDIR /app

COPY --from=builder /build/target/ *. jar /app/app.jar

CMD ["java", "-jar", "app. jar"]
