package server;

import servlet.Context;
import servlet.Host;
import servlet.Mapper;
import servlet.Wrapper;

import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

public class RequestProcessor extends Thread {

    private Socket socket;
    //private Map<String,HttpServlet> servletMap;
    private Mapper mapper;

    public RequestProcessor(Socket socket, Mapper mapper) {
        this.socket = socket;
        this.mapper = mapper;
    }

    @Override
    public void run() {
        try{
            InputStream inputStream = socket.getInputStream();

            // 封装Request对象和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());

            HttpServlet httpServlet = getServlet(mapper,request);

            // 静态资源处理
            if(httpServlet == null) {
                response.outputHtml(request.getUrl());
            }else{
                // 动态资源servlet请求
                //HttpServlet httpServlet = servletMap.get(request.getUrl());
                httpServlet.service(request,response);
            }
            socket.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据路径获取servlet
     * @param mapper
     * @param request
     * @return
     */
    private HttpServlet getServlet(Mapper mapper, Request request) {
        //http://localhost:8080/demo01/lagou
        String[] split = request.getUrl().split("\\/");
        if(split.length>2){
            String context = split[1];
            String servletName = split[2];

            for (Host host : mapper.getHosts()) {
                // host匹配
                if( host.getHostName().equals(request.getHostAndPort().split(":")[0])){
                    for (Context thisContext : host.getContextList()) {
                        // context 匹配
                        if(thisContext.getContextName().equals(context)){
                            for (Wrapper wrapper : thisContext.getWrappers()) {
                                // wrapper匹配
                                //区分处理静态资源还是动态资源
                                if(wrapper != null){
                                    if(wrapper.getUrlPattern().equals("/"+servletName)){
                                        return (HttpServlet)wrapper.getServlet();
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        return null;
    }
}
