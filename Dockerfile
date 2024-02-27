FROM openjdk:17.0.2-jdk-slim-buster
COPY build build
ENTRYPOINT ["java","-jar","/build/libs/HotelBooking-0.0.1-SNAPSHOT.jar"]