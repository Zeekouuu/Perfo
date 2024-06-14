FROM openjdk:17
WORKDIR /app
COPY target/Performance-1.0-SNAPSHOT.jar ./Performance.jar
EXPOSE 8084
#il faut recompliler le micro
CMD ["java", "-jar", "Performance.jar"]