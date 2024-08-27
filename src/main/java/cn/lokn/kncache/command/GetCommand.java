package cn.lokn.kncache.command;

import cn.lokn.kncache.core.Command;
import cn.lokn.kncache.core.KNCache;
import cn.lokn.kncache.core.Reply;

/**
 * @description: get command
 * @author: lokn
 * @date: 2024/08/25 22:18
 */
public class GetCommand implements Command {
    @Override
    public String name() {
        return "GET";
    }

    @Override
    public Reply<?> exec(KNCache cache, String[] args) {
        String s = cache.get(getKey(args));
        return Reply.bulkString(s);
    }
}
