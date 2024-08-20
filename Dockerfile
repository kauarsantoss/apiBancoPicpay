#
# Build Package
#
FROM maven:3.8.3-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage4
#
FROM openjdk:17-jdk-slim
COPY --from=build /target/dbBancoPicpay-0.0.1-SNAPSHOT.jar apipicpay.jar
EXPOSE 8000
ENTRYPOINT ["java","-jar","apipicpay.jar"]
