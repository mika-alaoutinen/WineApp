# Build jar package with Maven:
FROM maven:3.8-adoptopenjdk-16 as build
WORKDIR /app

COPY pom.xml .
COPY src src

RUN mvn clean package -f /app/pom.xml -B -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Create the Docker image:
FROM adoptopenjdk/openjdk16:alpine-slim

# Run application as user "spring"
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy dependencies from build stage
ARG DEPENDENCY=/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","com.mika.WineApp.WineApp"]
