FROM openjdk:21-jdk

WORKDIR /APP

COPY ./build/libs/api*.jar /APP/api.jar

EXPOSE 8080

CMD [ "java", "-jar", "api.jar"]