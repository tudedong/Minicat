package servlet;

import server.Servlet;

/**
 * @author tudedong
 * @description Wrapper组件，封装一个url和对应的servlet
 * @date 2020-05-25 17:21:27
 */
public class Wrapper {

    /**
     * 请求url
     */
    private String urlPattern;

    /**
     * 与请求对应的servlet
     */
    private Servlet servlet;

    /**
     * 无参构造器
     */
    public Wrapper() {
    }

    /**
     * 全参构造器
     * @param urlPattern
     * @param servlet
     */
    public Wrapper(String urlPattern, Servlet servlet) {
        this.urlPattern = urlPattern;
        this.servlet = servlet;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public Servlet getServlet() {
        return servlet;
    }

    public void setServlet(Servlet servlet) {
        this.servlet = servlet;
    }
}
