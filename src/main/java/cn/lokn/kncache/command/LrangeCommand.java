package cn.lokn.kncache.command;

import cn.lokn.kncache.core.Command;
import cn.lokn.kncache.core.KNCache;
import cn.lokn.kncache.core.Reply;

/**
 * @description: Lrange command
 * @author: lokn
 * @date: 2024/08/25 22:18
 */
public class LrangeCommand implements Command {
    @Override
    public String name() {
        return "LRANGE";
    }

    @Override
    public Reply<?> exec(KNCache cache, String[] args) {
        String key = getKey(args);
        String[] params = getParamsNoKey(args);
        int start = Integer.parseInt(params[0]);
        int end = Integer.parseInt(params[1]);
        return Reply.array(cache.lrange(key, start, end));
    }
}
