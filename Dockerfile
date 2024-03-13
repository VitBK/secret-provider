FROM eclipse-temurin:21.0.1_12-jre-jammy
RUN mkdir /app
COPY target/secret-provider-1.0.jar /app/
EXPOSE 8080 8080
ENTRYPOINT ["java","-jar","/app/secret-provider-1.0.jar"]