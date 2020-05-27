package servlet;

/**
 * @author tudedong
 * @description Service组件
 * @date 2020-05-26 09:27:45
 */
public class Service {

    /**
     * 多个连接器
     */
    private Connector[] connectors;

    /**
     * 定义一个service中Connector的计数
     */
    private int index = 0;

    /**
     * 初始化连接器
     * @param size
     */
    public void init(int size){
        connectors = new Connector[size];
    }

    /**
     * 添加连接器
     * @param connector
     */
    public void add(Connector connector){
        connectors[index++] = connector;
    }

    /**
     * servlet引擎
     */
    private Engine engine;

    /**
     * 无参构造器
     */
    public Service() {
    }

    /**
     * 有参构造器
     * @param connectors
     * @param engine
     */
    public Service(Connector[] connectors, Engine engine) {
        this.connectors = connectors;
        this.engine = engine;
    }

    public Connector[] getConnectors() {
        return connectors;
    }

    public void setConnectors(Connector[] connectors) {
        this.connectors = connectors;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }
}
