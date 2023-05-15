FROM gradle:7.6.1-jdk17-alpine AS builder

WORKDIR /usr/app/
ENV AWS_KEY is_not_a_real_key
ENV AWS_SECRET is_not_a_real_SECRET

COPY . .

RUN gradle build

FROM eclipse-temurin:17.0.6_10-jre-alpine

COPY --from=builder /usr/app/build/libs/*.jar /opt/app/application.jar

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

RUN ls /opt/app/
CMD java -jar /opt/app/application.jar