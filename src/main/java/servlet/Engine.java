package servlet;

/**
 * @author tudedong
 * @description Servlet引擎
 * @date 2020-05-26 09:30:48
 */
public class Engine {

    /**
     * 映射关系，包括各个子容器
     */
    private Mapper mapper;

    /**
     * 无参构造器
     */
    public Engine() {
    }

    /**
     * 有参构造器
     * @param mapper
     */
    public Engine(Mapper mapper) {
        this.mapper = mapper;
    }

    public Mapper getMapper() {
        return mapper;
    }

    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }
}
