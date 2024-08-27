package cn.lokn.kncache.command;

import cn.lokn.kncache.core.Command;
import cn.lokn.kncache.core.KNCache;
import cn.lokn.kncache.core.Reply;

/**
 * @description: mset command
 * @author: lokn
 * @date: 2024/08/25 22:18
 */
public class MsetCommand implements Command {
    @Override
    public String name() {
        return "MSET";
    }

    @Override
    public Reply<?> exec(KNCache cache, String[] args) {
        String[] keys = getKeys(args);
        String[] values = getValues(args);
        cache.mset(keys, values);
        return Reply.string(OK);
    }
}
