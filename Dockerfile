FROM eclipse-temurin:21-jre
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/bot.jar
ENTRYPOINT ["java", "-jar", "/app/bot.jar"]