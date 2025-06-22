FROM tomcat:11.0.7-jdk21-temurin

COPY target/jlink.war /usr/local/tomcat/webapps/