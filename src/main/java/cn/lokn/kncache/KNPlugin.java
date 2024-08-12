package cn.lokn.kncache;

/**
 * kn cache plugin
 */
public interface KNPlugin {

    void init();
    void startup();
    void shutdown();

}
