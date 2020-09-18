Running
-
    mvn clean package && docker run -ti --rm -p 8080:8080 -p 8787:8787 -v $(pwd)/target/rest-rdbms-1.0-SNAPSHOT.war:/usr/local/tomcat/webapps/ROOT.war -e JPDA_ADDRESS=8787 -e JPDA_TRANSPORT=dt_socket tomcat:jdk8-openjdk /usr/local/tomcat/bin/catalina.sh jpda run