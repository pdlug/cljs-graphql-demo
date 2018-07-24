FROM openjdk:8-jre-alpine

ADD target/graphql-demo.jar /

ENTRYPOINT ["java", "-jar", "/graphql-demo.jar"]
