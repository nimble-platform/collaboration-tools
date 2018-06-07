FROM tomcat

RUN rm -rf /usr/local/tomcat/webapps/*

ADD ./target/collaboration-service.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]
