FROM openjdk:11
ADD target/ReportingService.jar ReportingService.jar
EXPOSE 9005
ENTRYPOINT ["java","-jar","ReportingService.jar"]
