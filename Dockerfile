FROM openjdk:11.0.5-stretch

ADD . application/

RUN mkdir application/src/main/resources
ADD application/credentials.json application/src/main/resources/credentials.json

RUN mkdir /application/tokens

EXPOSE 8080
EXPOSE 8888

WORKDIR /application
CMD ./gradlew build && java -jar build/libs/gs-spring-boot-0.1.0.jar