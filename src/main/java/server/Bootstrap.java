package server;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import servlet.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Minicat的主类
 */
public class Bootstrap {

    /**定义socket监听的端口号*/
    private int port = 8080;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Minicat启动需要初始化展开的一些操作
     */
    public void start() throws Exception {

        // V4.0 加载并解析相关的配置，server.xml
        Service service = loadServer();
        // V3.0 加载解析相关的配置，web.xml
        //loadServlet();

        // 定义一个线程池
        int corePoolSize = 10;
        int maximumPoolSize =50;
        long keepAliveTime = 100L;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(50);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler
        );

        /*
            完成Minicat 1.0版本
            需求：浏览器请求http://localhost:8080,返回一个固定的字符串到页面"Hello Minicat!"
         */
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("=====>>>Minicat start on port：" + port);

        /*while(true) {
            Socket socket = serverSocket.accept();
            // 有了socket，接收到请求，获取输出流
            OutputStream outputStream = socket.getOutputStream();
            String data = "Hello Minicat!";
            String responseText = HttpProtocolUtil.getHttpHeader200(data.getBytes().length) + data;
            outputStream.write(responseText.getBytes());
            socket.close();
        }*/

        /**
         * 完成Minicat 2.0版本
         * 需求：封装Request和Response对象，返回html静态资源文件
         */
        /*while(true) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();

            // 封装Request对象和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());

            response.outputHtml(request.getUrl());
            socket.close();

        }*/

        /**
         * 完成Minicat 3.0版本
         * 需求：可以请求动态资源（Servlet）
         */
        /*while(true) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();

            // 封装Request对象和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());

            // 静态资源处理
            if(servletMap.get(request.getUrl()) == null) {
                response.outputHtml(request.getUrl());
            }else{
                // 动态资源servlet请求
                HttpServlet httpServlet = servletMap.get(request.getUrl());
                httpServlet.service(request,response);
            }
            socket.close();

        }
*/

        /*
            多线程改造（不使用线程池）
         */
        /*while(true) {
            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket,servletMap);
            requestProcessor.start();
        }*/

        System.out.println("=========>>>>>>使用线程池进行多线程改造");
        /*
            多线程改造（使用线程池）
         */
        while(true) {

            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket,service.getEngine().getMapper());
            //requestProcessor.start();
            threadPoolExecutor.execute(requestProcessor);

        }
    }

    //private Map<String,HttpServlet> servletMap = new HashMap<String,HttpServlet>();

    /**
     * 加载解析server.xml,初始化Servlet
     */
    private Service loadServer() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("server.xml");
        //接下来我们的目标是 获取每个demo下的web.xml内容信息
        SAXReader saxReader = new SAXReader();

        //定义service对象
        Service service = new Service();

        try {
            //解析server.xml
            Document document = saxReader.read(resourceAsStream);
            //获取根标签 <Server></Server>
            Element rootElement = document.getRootElement();
            //获取子标签<Service></Service>（一个server可以有多个service）
            List<Element> serviceElements = rootElement.selectNodes("//Service");
            //对service标签中的子标签进行处理
            for(Element serviceElement:serviceElements){
                //获取连接器Connector
                List<Element> connectorElements = serviceElement.selectNodes("//Connector");
                //初始化并加载连接器
                if(connectorElements != null && connectorElements.size()>0){
                    service.init(connectorElements.size());
                    for(Element connectorElement:connectorElements){
                        String port = connectorElement.attributeValue("port");
                        Connector connector = new Connector(Integer.valueOf(port));
                        service.add(connector);
                    }
                }

                Mapper mapper = new Mapper();
                //获取Servlet引擎
                Element engineElement = (Element)serviceElement.selectSingleNode("//Engine");
                if(engineElement != null){
                    //解析Host
                    List<Element> hostElements = engineElement.selectNodes("//Host");
                    if(hostElements != null && hostElements.size()>0){
                        mapper.init(hostElements.size());
                        for(Element hostElement:hostElements){
                            Host host = new Host();
                            host.setHostName(hostElement.attributeValue("name"));
                            mapper.addHost(host);
                            Engine engine = new Engine();
                            engine.setMapper(mapper);
                            service.setEngine(engine);

                            String appBase = hostElement.attributeValue("appBase");
                            File file = new File(appBase);

                            //解析webapps中的应用即 context
                            if(file.exists()){
                                File[] webapps = file.listFiles();
                                for(File webapp:webapps){
                                    if(webapp.isFile()){
                                        continue;
                                    }
                                    Context context = new Context(webapp.getName());
                                    context.init(webapps.length);
                                    host.addContext(context);
                                    // D:/IdeaProjects/Minicat/webapps/demo01/web.xml
                                    //http://localhost:8080/demo1/lagou
                                    String webXmlPath = webapp.getPath().replace("\\","/")+"/web.xml";
                                    loadServlet(webXmlPath,context);
                                    //输出打印部署的项目情况（contextName，servlet）
                                    System.out.println(context);
                                }
                            }

                        }
                    }
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return service;
    }

    /**
     * 加载解析web.xml，初始化Servlet
     */
    private void loadServlet(String webXmlPath,Context context) {
        //InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        File file = new File(webXmlPath);
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(file);
            Element rootElement = document.getRootElement();

            List<Element> selectNodes = rootElement.selectNodes("//servlet");
            for (int i = 0; i < selectNodes.size(); i++) {
                Element element = selectNodes.get(i);
                // <servlet-name>lagou</servlet-name>
                Element servletnameElement = (Element) element.selectSingleNode("servlet-name");
                String servletName = servletnameElement.getStringValue();
                // <servlet-class>server.LagouServlet</servlet-class>
                Element servletclassElement = (Element) element.selectSingleNode("servlet-class");
                //server.LagouServlet
                String servletClass = servletclassElement.getStringValue();

                // 根据servlet-name的值找到url-pattern
                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                // /lagou
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
                //获取web.xml中配置的servlet
                //D:/IdeaProjects/Minicat/webapps/demo01
                System.out.println(webXmlPath);
                String servletPath = webXmlPath.replace("/web.xml", "");
                System.out.println("servlet加载路径=====》" + servletPath);
                File servletfile = new File(servletPath);
                URL url = servletfile.toURL();
                URL[] urls = {url};
                ClassLoader classLoader = new URLClassLoader(urls);
                //从web.xml中读取类的全路径，例如server.LagouServlet1
                Class clazz = classLoader.loadClass(servletClass);
                Object o = clazz.newInstance();
                Wrapper wrapper = new Wrapper(urlPattern, (HttpServlet)o);
                //servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());
                context.addWrapper(wrapper);
            }

        } catch (MalformedURLException e) {
                e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Minicat 的程序启动入口
     * @param args
     */
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        try {
            // 启动Minicat
            bootstrap.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
