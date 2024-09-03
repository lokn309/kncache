package cn.lokn.kncache.command;

import cn.lokn.kncache.core.Command;
import cn.lokn.kncache.core.KNCache;
import cn.lokn.kncache.core.Reply;

/**
 * @description: Sadd command
 * @author: lokn
 * @date: 2024/08/25 22:18
 */
public class SaddCommand implements Command {
    @Override
    public String name() {
        return "SADD";
    }

    @Override
    public Reply<?> exec(KNCache cache, String[] args) {
        String key = getKey(args);
        String[] vals = getParamsNoKey(args);
        return Reply.integer(cache.sadd(key, vals));
    }
}
