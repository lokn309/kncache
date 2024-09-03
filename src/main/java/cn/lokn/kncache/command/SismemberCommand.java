package cn.lokn.kncache.command;

import cn.lokn.kncache.core.Command;
import cn.lokn.kncache.core.KNCache;
import cn.lokn.kncache.core.Reply;

/**
 * @description: Sadd command
 * @author: lokn
 * @date: 2024/08/25 22:18
 */
public class SismemberCommand implements Command {
    @Override
    public String name() {
        return "SISMEMBER";
    }

    @Override
    public Reply<?> exec(KNCache cache, String[] args) {
        String key = getKey(args);
        String val = getValue(args);
        return Reply.integer(cache.sismember(key, val));
    }
}
