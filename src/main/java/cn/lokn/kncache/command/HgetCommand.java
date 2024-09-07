package cn.lokn.kncache.command;

import cn.lokn.kncache.core.Command;
import cn.lokn.kncache.core.KNCache;
import cn.lokn.kncache.core.Reply;

/**
 * @description: Hget command
 * @author: lokn
 * @date: 2024/08/25 22:18
 */
public class HgetCommand implements Command {
    @Override
    public String name() {
        return "HGET";
    }

    @Override
    public Reply<?> exec(KNCache cache, String[] args) {
        String key = getKey(args);
        String hkey = getValue(args);
        return Reply.string(cache.hget(key, hkey));
    }
}
