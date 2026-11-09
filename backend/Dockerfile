
# Stage 1: Build Spring Boot application
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Run application
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the TiDB CA certificate
COPY certs/tidb-ca.pem /tmp/tidb-ca.pem

# Import certificate into Java trust store
RUN keytool -importcert -noprompt -trustcacerts \
    -alias tidb-ca \
    -file /tmp/tidb-ca.pem \
    -keystore "$JAVA_HOME/lib/security/cacerts" \
    -storepass changeit \
    && rm /tmp/tidb-ca.pem

# Copy Spring Boot JAR
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
