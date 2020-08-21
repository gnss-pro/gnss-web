### 技术架构
JAVA：JDK 1.8+  
框架：SpringBoot 2.x、Spring Data JPA、Hibernate Validation、Caffeine缓存、SnowFlake ID生成器  
日志：Slf4j、Logback   
中间件：RabbitMQ、Redis  
数据库：demo版采用H2，可自行更换其他主流数据库  
接口文档：Swagger   
代码规约：遵循《阿里巴巴Java开发手册》标准

### 特性
1. 支持JT808-2011、JT808-2019、JT1078、苏标主动安全指令。
2. 前后端分离，采用Swagger管理接口，支持在线调试。
3. 跨平台开发和部署，使用内嵌的容器无需部署到Tomcat。
4. 采用SpringMVC开发Restful接口，异常统一拦截处理，支持分页，采用Hibernate Validation校验接口参数。
5. 所有数据表的ID统一由SnowFlake ID生成器生成，取代传统的使用整型自增或者UUID的方式，确保ID是整型有顺序和安全性，不会影响查询性能。
6. 采用Spring Data JPA兼容所有主流数据库，无需编写SQL，可随时更换数据库而不用修改代码。
7. 采用性能最高的Caffeine作为一级本地缓存，Redis作为二级分布式缓存，大大提高了性能。目前已将车辆信息、服务器状态信息缓存到Redis，数据库或者Web后台未启动不会影响网关正常运行。
8. 下发给终端的指令统一处理，接口已分JT808、JT1078、主动安全三大类。
9. 终端上传的数据分多个MQ主题订阅处理，目前订阅了主动安全附件、JT808日志、JT808多媒体文件、终端状态信息、终端上传数据、指令上行。
10. 目前实现了车辆信息维护、压力测试位置数量统计、JT1078实时音视频和录像回放、位置存储、报警存储、多媒体文件存储(JT808拍照)、FTP上传文件查询存储、主动安全报警和附件查询。
11. 预留了JT809企业网关接口，支持数据上传多个上级平台，后续再加。  

PS：此项目只实现终端数据的订阅和处理，测试页面在src/main/reources/static中，登录、公司机构、权限、报表等功能请自行实现。测试界面做的丑请谅解，仅做测试接口使用。  

### 环境搭建
JDK 1.8+  
maven 3.6+(配置阿里云镜像加速请参考：**https://maven.aliyun.com/mvn/guide**)  
开发工具建议使用IntelliJ IDEA  

以下步骤以苹果Mac系统开发为例：  
(1) 下载项目：git clone https://github.com/gnss-pro/gnss-web.git    
(2) 打开IDEA，导入项目  
(3) 安装插件Lombok，点击Preferences -> Plugins -> 查询Lombok并安装重启IDEA   
(4) 设置Java编译版本，点击Preferences -> Compiler -> Java Compiler -> 右边的Target bytecode version选择1.8  
(5) 点击File -> Project Structure -> Project -> 右边Project SDK选择1.8，Modules -> 右边Module SDK选择1.8  
(6) 设置maven环境，点击Preferences -> Build Tools -> Maven -> 右边配置Maven home directory和User settings file  
(7) 打开IDEA的maven工具栏，双击clean，然后双击package则在target目录生成jar文件

### 项目运行
(1) 请确保中间件RabbitMQ和Redis已经启动。  
(2) 选择GnssWebApplication，运行main方法启动项目。默认dev环境，会使用application-dev.yml的配置。  
(3) 打开浏览器输入：**http://localhost:9999/gnss-web/static/jt808Test.html**  