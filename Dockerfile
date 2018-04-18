FROM java:8-alpine

ADD target/graphql-demo.jar /

ENTRYPOINT ["java", "-jar", "/graphql-demo.jar"]
