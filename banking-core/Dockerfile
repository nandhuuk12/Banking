FROM openjdk:17-jdk-slim AS builder

WORKDIR /app

# Copy Maven files
COPY pom.xml ./
COPY src ./src

# Install Maven
RUN apt-get update && apt-get install -y maven

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jre-slim

WORKDIR /app

# Copy the JAR from builder stage
COPY --from=builder /app/target/banking-core-*.jar banking-core.jar

# Create non-root user
RUN groupadd -r banking && useradd -r -g banking banking
USER banking

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "banking-core.jar"]
