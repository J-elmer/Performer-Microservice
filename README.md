# Performer-Microservice
This is the performer microservice which is part of ReviewStars. To launch the complete application, see the [ReviewStars repository](https://github.com/J-elmer/ReviewStars).

To review this code, you can pull the repository locally.

**Installation**

*Prerequisites*:
Make sure you have Docker installed and can run Java applications.

*Steps*:

Navigate to the project directory and run

```
docker-compose up -d in your terminal
```

Once the containers are running, start the project locally (through IntelliJ for example).

You can test through postman. The application is configured to run on port 6060, so be sure to send requests to that port or change it in the application.properties file.

**Important note**

In order for the whole application to work, you should also make sure you run the [concert microservice](https://github.com/J-elmer/Concert-microservice), otherwise deleting and updating performers will not work as expected.
