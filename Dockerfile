# Download Maven Wrapper files if they are not present:
# mvn -N io.takari:maven:wrapper


# Build the jar package with Maven:
# FROM adoptopenjdk/openjdk13:ubuntu-slim as build
# WORKDIR /app

# COPY mvnw .
# COPY .mvn .mvn
# COPY pom.xml .
# COPY src src

# RUN ./mvnw package -DskipTests
# RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM adoptopenjdk/openjdk13:alpine-slim
ADD /target/*.jar WineApp.jar
ENTRYPOINT ["java", "-jar", "WineApp.jar"]