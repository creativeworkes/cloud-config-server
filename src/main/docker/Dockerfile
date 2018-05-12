# base image
FROM java:openjdk-8-jre-alpine

LABEL PROJECT="Spring Boot Cloud Config Server" \
      AUTHOR="yeqinglan@hd123.com" \
      COPYRIGHT="Shanghai HEADING Information Engineering Co., Ltd."

# developer info
MAINTAINER  "yeqinglan" <yeqinglan@hd123.com>

# current workspace
WORKDIR /opt

# log dir
VOLUME /var/log/configserver

# appliction resources
ADD deamon/start.sh  start.sh
ADD cloud-config-server.war app.war
ADD classes/application.yaml config/

# runtimes setting
ENV LANG en_US.UTF-8
RUN /sbin/apk --no-cache add bash tzdata && \
    /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    /bin/echo "Asia/Shanghai" > /etc/timezone && \
    /sbin/apk del tzdata && \
    /bin/rm -rf /var/cache && \
    /bin/chmod +x start.sh

# open image port
EXPOSE 8888

# exec command
ENTRYPOINT ["/bin/bash"]
CMD ["start.sh"]
