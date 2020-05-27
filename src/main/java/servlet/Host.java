package servlet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tudedong
 * @description 虚拟主机组件
 * @date 2020-05-25 17:38:36
 */
public class Host {

    /**
     * 虚拟主机名称
     */
    private String hostName;

    /**
     * 对应的多个Context
     */
    private List<Context> contextList;

    public Host() {
    }

    /**
     * 构造器
     * @param hostName
     */
    public Host(String hostName) {
        this.hostName = hostName;
    }

    /**
     * 添加Context
     * @param context
     */
    public void addContext(Context context){
        if(contextList == null){
            contextList = new ArrayList<Context>();
        }
        contextList.add(context);
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public List<Context> getContextList() {
        return contextList;
    }

    public void setContextList(List<Context> contextList) {
        this.contextList = contextList;
    }
}
