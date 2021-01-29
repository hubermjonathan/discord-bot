FROM maven:3.6.3-jdk-8 as build
COPY . .
RUN mvn clean compile package
