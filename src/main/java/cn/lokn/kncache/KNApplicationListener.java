package cn.lokn.kncache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: plugins entrypoint
 * @author: lokn
 * @date: 2024/07/15 23:55
 */
@Component
public class KNApplicationListener implements ApplicationListener<ApplicationEvent> {

    @Autowired
    List<KNPlugin> plugins;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        // 统一管理 plugin 的生命周期
        if (event instanceof ApplicationReadyEvent are) {
            for (KNPlugin plugin : plugins) {
                plugin.init();
                plugin.startup();
            }
        } else if (event instanceof ContextClosedEvent cce) {
            for (KNPlugin plugin : plugins) {
                plugin.shutdown();
            }
        }
    }
}
