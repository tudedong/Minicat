package servlet;

import java.util.Arrays;
import java.util.List;

/**
 * @author tudedong
 * @description web应用组件
 * @date 2020-05-25 17:28:57
 */
public class Context {

    /**
     * 对应一个web应用名称
     */
    private String contextName;

    /**
     * 对应多个wrapper
     */
    private Wrapper[] wrappers;

    private int index = 0;

    public void init(int size){
        wrappers = new Wrapper[size];
    }

    public void addWrapper(Wrapper wrapper){
        wrappers[index++] = wrapper;
    }

    /**
     * 构造器
     * @param contextName
     */
    public Context(String contextName) {
        this.contextName = contextName;
    }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public Wrapper[] getWrappers() {
        return wrappers;
    }

    public void setWrappers(Wrapper[] wrappers) {
        this.wrappers = wrappers;
    }

    @Override
    public String toString() {
        return "Context{" +
                "contextName='" + contextName + '\'' +
                ", wrappers=" + Arrays.toString(wrappers) +
                ", index=" + index +
                '}';
    }
}
