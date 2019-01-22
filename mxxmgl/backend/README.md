### 梅县项目管理 后台api

#### 注意事项：  
打成war包步骤：   
1. pom.xml添加依赖 目的是去掉内嵌的tomcat   
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
    <scope>provided</scope>
</dependency>
```
2. main添加继承SpringBootServletInitializer 
```
public class BackendApplication extends SpringBootServletInitializer
```
3. 修改jar为war    
```
<packaging>war</packaging>
<!--<packaging>jar</packaging>-->
```
4. 修改文件上传下载地址
```
// String fileLocation = "D:\\pms\\"+uploadFileName;
    String fileLocation = "/usr/local/pms/"+uploadFileName;

// String path  = "D:\\pms\\";
   String path = "/usr/local/pms";
```
打包jar并部署到服务器 
1. 打包命令
```
mvn clean package -DskipTests -P prod
```
2. Linux启动服务
```
nohup java -jar backend.jar >dev/null 2>/dev/null &
```
3 .Linux中tomcat服务
```
启动服务：service tomcat restart
         usr/local/tomcat/bin  ./startup.sh
停止服务： usr/local/tomcat/bin  ./shutdown.sh
查看端口情况：netstat -tunlp | grep 8080
停止进程： kill -9 进程号
```


