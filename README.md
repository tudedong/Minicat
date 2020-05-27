# Minicat
web服务器应用模块一


#实现思路
#需求：
Minicat是在V3.0的版本上进行升级，实现模拟tomcat中webapps部署多个项目的功能。

#思路
我们的目标是在项目启动时，Minicat进行初始化配置加载server.xml,将V3.0中固定的项目请求和对应的处理请求servlet，
即Map<String,HttpServlet>结构，改造成V4.0的多项目，每个项目的请求和其对应的处理servlet进行初始化成Mapper组件存储。


#具体步骤
1.添加server.xml文件
2.添加部署文件目录webapps和需要部署的项目demo1、demo2
3.通过封装Mapper组件体系实现servlet初始化，即完成一个请求对应一个servlet关系的存储
即Mapper类 -> Host -> Context -> Wrapper->Servlet
3.1封装Wrapper类，要获得一个servlet，就需要一个Wrapper，Wrapper是最底层的一个容器，就代表一个servlet
3.2封装Context类，Context代表一个web应用，一个web应用中包括多个Wrapper
3.3封装Host类，Host代表虚拟主机，一个Host下有多个Context
3.4封装Mapper类，代表映射关系，tomcat中可以配置多个虚拟主机Host，即一个Mapper中包括多个Host
3.5实现请求及对应的servlet初始化存储
即server.xml -> Server（Service）-> Connector -> Engine -> Mapper类

4.测试
4.1启动项目
4.2访问路径：
demo1的静态资源：
http://localhost:8080/demo1/index.html
demo1的动态资源：
http://localhost:8080/demo1/lagou
demo2的静态资源：
http://localhost:8080/demo2/index.html
demo2的动态资源：
http://localhost:8080/demo2/lagou