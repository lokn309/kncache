package cn.lokn.kncache.command;

import cn.lokn.kncache.core.Command;
import cn.lokn.kncache.core.KNCache;
import cn.lokn.kncache.core.Reply;

/**
 * @description: Zscore command
 * @author: lokn
 * @date: 2024/08/25 22:18
 */
public class ZscoreCommand implements Command {
    @Override
    public String name() {
        return "ZSCORE";
    }

    @Override
    public Reply<?> exec(KNCache cache, String[] args) {
        String key = getKey(args);
        String val = getValue(args);
        Double zscore = cache.zscore(key, val);
        return Reply.string(zscore == null ? null : zscore.toString());
    }
}
