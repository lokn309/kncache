package cn.lokn.kncache.command;

import cn.lokn.kncache.core.Command;
import cn.lokn.kncache.core.KNCache;
import cn.lokn.kncache.core.Reply;

/**
 * @description: decr command
 * @author: lokn
 * @date: 2024/08/25 22:18
 */
public class DecrCommand implements Command {
    @Override
    public String name() {
        return "DECR";
    }

    @Override
    public Reply<?> exec(KNCache cache, String[] args) {
        String key = getKey(args);
        return Reply.integer(cache.decr(key));
    }
}
