FROM openjdk:8-alpine

COPY target/uberjar/wil.jar /wil/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/wil/app.jar"]
