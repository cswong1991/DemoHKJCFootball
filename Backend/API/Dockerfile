FROM maven:3.8.5-openjdk-11-slim AS builder

WORKDIR /builder
COPY ./src ./src
COPY ./pom.xml ./pom.xml
RUN mvn clean package -DskipTests

FROM openjdk:11-jre-slim

WORKDIR /runner
COPY --from=builder /builder/target ./target
COPY ./wait-for-it.sh ./wait-for-it.sh

ENTRYPOINT ["./wait-for-it.sh", "hkjcfootball-mysql:3306", "--"]
CMD ["java", "-jar", "./target/hkjcfootball-0.0.1-SNAPSHOT.jar"]
