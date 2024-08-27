package cn.lokn.kncache.command;

import cn.lokn.kncache.core.Command;
import cn.lokn.kncache.core.KNCache;
import cn.lokn.kncache.core.Reply;

/**
 * @description: exists command
 * @author: lokn
 * @date: 2024/08/25 22:18
 */
public class ExistsCommand implements Command {
    @Override
    public String name() {
        return "EXISTS";
    }

    @Override
    public Reply<?> exec(KNCache cache, String[] args) {
        String[] keys = getParams(args);
        return Reply.integer(cache.exists(keys));
    }
}
