FROM gradle:9.1.0-jdk25-alpine AS builder

WORKDIR /usr/app/
ENV AWS_KEY is_not_a_real_key
ENV AWS_SECRET is_not_a_real_SECRET
ENV GOOGLE_CLOUD_CREDENTIALS is_not_a_real_key
ENV GOOGLE_CLOUD_PROJECT is_not_a_real_key

COPY . .

RUN gradle build --exclude-task test

FROM eclipse-temurin:25-jdk-alpine

COPY --from=builder /usr/app/build/libs/*.jar /opt/app/application.jar

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

RUN ls /opt/app/
CMD java -jar /opt/app/application.jar
