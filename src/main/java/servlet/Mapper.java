package servlet;

/**
 * @author tudedong
 * @description 映射关系类
 * @date 2020-05-25 17:47:25
 */
public class Mapper {

    /**
     * 虚拟主机数组
     */
    private Host[] hosts;

    private int index = 0;

    /**
     * 初始化
     * @param size
     */
    public void init(int size){
        hosts = new Host[size];
    }

    /**
     * 存储host
     */
    public void addHost(Host host){
        hosts[index++] = host;
    }

    public Host[] getHosts() {
        return hosts;
    }

    public void setHosts(Host[] hosts) {
        this.hosts = hosts;
    }
}
