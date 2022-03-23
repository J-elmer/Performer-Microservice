FROM openjdk:17-jdk-alpine
MAINTAINER jelmerdijkstra
COPY target/se_track_performer-0.0.1-SNAPSHOT.jar se_track_performer-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/se_track_performer-0.0.1-SNAPSHOT.jar"]