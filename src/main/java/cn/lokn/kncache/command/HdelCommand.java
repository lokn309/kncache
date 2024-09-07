package cn.lokn.kncache.command;

import cn.lokn.kncache.core.Command;
import cn.lokn.kncache.core.KNCache;
import cn.lokn.kncache.core.Reply;

/**
 * @description: Hdel command
 * @author: lokn
 * @date: 2024/08/25 22:18
 */
public class HdelCommand implements Command {
    @Override
    public String name() {
        return this.getClass()
                .getSimpleName()
                .replace("Command", "")
                .toUpperCase();
    }

    @Override
    public Reply<?> exec(KNCache cache, String[] args) {
        String key = getKey(args);
        String[] hkeys = getParamsNoKey(args);
        return Reply.integer(cache.hdel(key, hkeys));
    }
}
