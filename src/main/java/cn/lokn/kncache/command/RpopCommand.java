package cn.lokn.kncache.command;

import cn.lokn.kncache.core.Command;
import cn.lokn.kncache.core.KNCache;
import cn.lokn.kncache.core.Reply;

/**
 * @description: Lpop command
 * @author: lokn
 * @date: 2024/08/25 22:18
 */
public class RpopCommand implements Command {
    @Override
    public String name() {
        return "RPOP";
    }

    @Override
    public Reply<?> exec(KNCache cache, String[] args) {
        String key = getKey(args);
        int count = 1;
        if (args.length > 6) {
            String value = getValue(args);
            count = Integer.parseInt(value);
            // 返回数组
            return Reply.array(cache.rpop(key, count));
        }

        String[] lpop = cache.rpop(key, count);
        return Reply.bulkString(lpop == null ? null : lpop[0]);
    }
}
