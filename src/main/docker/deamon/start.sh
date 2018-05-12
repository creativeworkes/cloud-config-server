#########################################################################
# File Name: start.sh
# Author: yeqinglan
# Mail: yeqinglan@hd123.com
# Created Time: 2017-05-25 11:30:00
#########################################################################
#!/bin/sh

APP_BIN="/opt/app.war"

JAVA_OPTS="-ea \
           -server \
           -Xmx1024M -Xms1024M \
           -Dfile.encoding=UTF-8 \
           -Djava.security.egd=file:/dev/./urandom"

YAML_OPTS="--spring.config.location=/opt/config/application.yaml"

# start service
exec /usr/bin/java ${JAVA_OPTS} -jar ${APP_BIN} ${YAML_OPTS}
