#!bin/bash

echo -e "\n"
echo -e "======================================="
echo -e "     请选择您要做的操作 "
echo -e "       1、使用数据库配置授权"
echo -e "       2、使用内存配置授权"
echo -e "======================================="

echo -e "\n"
echo "请输入你的选择（序号）:"
read choice

echo -e "\n正在拷贝配置文件"
\cp ./application.yml ./application.yaml

if [ $choice -eq 1 ]
then
   echo -e "你的选择为：使用数据库配置授权"
   echo -e "\n请输入数据库连接地址（IP）："
   read ip
   echo -e "\n请输入数据库名称："
   read database
   echo -e "\n请输入数据库连接用户名："
   read username
   echo -e "\n请输入数据库连接密码："
   read password

   echo -e "\n正在生成对应的配置文件"
   perl -p -i -e "s/{host}/$ip/g"           ./application.yaml
   perl -p -i -e "s/{database}/$database/g" ./application.yaml
   perl -p -i -e "s/{username}/$username/g" ./application.yaml
   perl -p -i -e "s/{password}/$password/g" ./application.yaml
   perl -p -i -e "s/{enable}/false/g"        ./application.yaml

   echo -e "\n配置文件已生成，正在拷贝到docker容器"
   status=`docker ps | grep configserver`
   if [ "$status" = "" ]
   then
     docker start configserver && \
     docker exec -it --tty=false configserver \
            bash -c 'cat > /opt/config/application.yaml' < ./application.yaml
   else
     docker exec -it --tty=false configserver \
            bash -c 'cat > /opt/config/application.yaml' < ./application.yaml
   fi

   echo -e "配置文件拷贝完成，正在重启容器"
   docker restart configserver
   exit
fi

if [ $choice -eq 2 ]
then
   echo -e "你的选择为：使用内存配置授权"
   echo -e "\n请输入数据库连接地址（IP）："
   read ip
   echo -e "\n请输入数据库名称："
   read database
   echo -e "\n请输入数据库连接用户名："
   read username
   echo -e "\n请输入数据库连接密码："
   read password

   echo -e "\n正在生成对应的配置文件"
   perl -p -i -e "s/{host}/$ip/g"           ./application.yaml
   perl -p -i -e "s/{database}/$database/g" ./application.yaml
   perl -p -i -e "s/{username}/$username/g" ./application.yaml
   perl -p -i -e "s/{password}/$password/g" ./application.yaml
   perl -p -i -e "s/{enable}/true/g"       ./application.yaml

   echo -e "\n配置文件已生成，正在拷贝到docker容器"
   status=`docker ps | grep configserver`
   if [ "$status" = "" ]
   then
     docker start configserver && \
     docker exec -it --tty=false configserver \
            bash -c 'cat > /opt/config/application.yaml' < ./application.yaml
   else
     docker exec -it --tty=false configserver \
            bash -c 'cat > /opt/config/application.yaml' < ./application.yaml
   fi

   echo -e "配置文件拷贝完成，正在重启容器"
   docker restart configserver
   exit
fi

echo -e "\n您的选择不正确，byebye ^_^ ...\n"
exit
