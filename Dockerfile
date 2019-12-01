FROM openjdk:11.0.5-stretch

ADD . application/

RUN mkdir /application/tokens

EXPOSE 8080
EXPOSE 8888

WORKDIR /application
CMD ./gradlew build && java -jar build/libs/gs-spring-boot-0.1.0.jar