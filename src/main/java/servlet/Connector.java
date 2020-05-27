package servlet;

/**
 * @author tudedong
 * @description 连接器
 * @date 2020-05-26 09:34:29
 */
public class Connector {

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 有参构造器
     * @param port
     */
    public Connector(Integer port) {
        this.port = port;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
