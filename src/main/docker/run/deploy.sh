#########################################################################
# File Name: deploy.sh
# Author: 叶青蓝
# mail: yeqinglan@hd123.com
# Created Time: 2017-06-07 20:00:00
#########################################################################
#!/bin/bash

docker pull dockerhub.hd123.com/cloud-config-server:$1

docker rm -f configserver
docker run -d -p 8888:8888  \
	-v /var/log/configserver:/var/log/configserver \
	-v /hdapp/configs:/opt/configs \
	--name configserver \
	dockerhub.hd123.com/cloud-config-server:$1
