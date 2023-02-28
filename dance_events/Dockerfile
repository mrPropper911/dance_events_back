#Copy *.jar from folder<target> to image and add entry poit
FROM adoptopenjdk/openjdk16:ubi
EXPOSE 8080
ARG JAR_FILE=target/*.jar
ENV DB_USERNAME=test
ENV DB_PASSWORD=test
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.datasource.username=${DB_USERNAME}", "-Dspring.datasource.password=${DB_PASSWORD}" , "-jar","/app.jar"]