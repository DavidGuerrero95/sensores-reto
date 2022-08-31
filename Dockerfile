FROM openjdk:12
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} Sensores.jar
ENTRYPOINT ["java","-jar","/Sensores.jar"]