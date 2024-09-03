package cn.lokn.kncache.command;

import cn.lokn.kncache.core.Command;
import cn.lokn.kncache.core.KNCache;
import cn.lokn.kncache.core.Reply;

/**
 * @description: Spop command
 * @author: lokn
 * @date: 2024/08/25 22:18
 */
public class SpopCommand implements Command {
    @Override
    public String name() {
        return "SPOP";
    }

    @Override
    public Reply<?> exec(KNCache cache, String[] args) {
        String key = getKey(args);
        int count = 1;
        if (args.length > 6) {
            String value = getValue(args);
            count = Integer.parseInt(value);
            // 返回数组
            return Reply.array(cache.spop(key, count));
        }

        String[] spop = cache.spop(key, count);
        return Reply.bulkString(spop == null ? null : spop[0]);
    }
}
