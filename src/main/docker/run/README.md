# 文件列表
- application.yml: 配置文件备份内容
- init-configserver.sh: 配置服务器初始化脚本
- deploy.sh: 系统部署脚本
# 使用步骤
- 运行configserver容器（使用deploy.sh脚本）
- 拷贝文件application.yml、init-configserver.sh到对应位置（根据个人喜好而定）
- 运行init-configserver.sh脚本初始化配置服务器
