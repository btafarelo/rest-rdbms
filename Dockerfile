FROM tomcat:jdk11-openjdk

COPY src/main/resources/ /opt/resources/

COPY target/autocrud-1.0-SNAPSHOT.war ROOT.war