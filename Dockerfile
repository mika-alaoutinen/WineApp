# Download Maven Wrapper files if they are not present:
# mvn -N io.takari:maven:wrapper


FROM adoptopenjdk/openjdk13:ubuntu-slim as build
WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM adoptopenjdk/openjdk13:ubuntu-slim
# VOLUME /tmp
COPY . /app
ADD /target/WineApp-1.0.5-SNAPSHOT.jar wineapp.jar

ARG DEPENDENCY=/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java", "-jar", "wineapp.jar"]
# ENTRYPOINT ["java","-cp","app:app/lib/*","com.mika.WineApp"]