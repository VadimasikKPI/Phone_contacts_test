FROM openjdk:17-jdk

COPY target/contact-0.0.1-SNAPSHOT.jar /app/contact-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "/app/contact-0.0.1-SNAPSHOT.jar"]