# Use an official OpenJDK runtime as a parent image
FROM openjdk:17

# Set the working directory in the container
WORKDIR /app

# Copy the jar file into the container at /app
COPY target/logisticService-0.0.1-SNAPSHOT.jar /app/app.jar

# Make port 8081 available to the world outside this container
EXPOSE 8383

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
