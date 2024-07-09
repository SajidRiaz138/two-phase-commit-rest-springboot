# Docker Deployment Guide

This guide will help you build and deploy the Spring Boot order service using Docker.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Step 1: Create Dockerfile](#step-1-create-dockerfile)
- [Step 2: Build the Project](#step-2-build-the-project)
- [Step 3: Build the Docker Image](#step-3-build-the-docker-image)
- [Step 4: Run the Docker Container](#step-4-run-the-docker-container)
- [Verifying the Deployment](#verifying-the-deployment)

## Prerequisites

- Docker installed on your machine.
- Maven installed on your machine.
- Java 17 installed on your machine.

## Step 1: Create Dockerfile

Create a file named `Dockerfile` in the root of your project directory and add the following content:

```Dockerfile
# Use an official OpenJDK 17 runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the project JAR file to the container
COPY target/order-service.jar order-service.jar

# Expose the port the app runs on
EXPOSE 8081

# Run the JAR file
ENTRYPOINT ["java", "-jar", "order-service.jar"]

-----------------------------
mvn clean package
docker build -t order-service .
docker run -p 8081:8081 order-service
http://localhost:8081




