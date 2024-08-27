package cn.lokn.kncache.command;

import cn.lokn.kncache.core.Command;
import cn.lokn.kncache.core.KNCache;
import cn.lokn.kncache.core.Reply;

/**
 * @description: Lindex command
 * @author: lokn
 * @date: 2024/08/25 22:18
 */
public class LindexCommand implements Command {
    @Override
    public String name() {
        return "LINDEX";
    }

    @Override
    public Reply<?> exec(KNCache cache, String[] args) {
        String key = getKey(args);
        int index = Integer.parseInt(getValue(args));
        return Reply.bulkString(cache.lindex(key, index));
    }
}
