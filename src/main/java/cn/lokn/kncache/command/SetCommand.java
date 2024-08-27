package cn.lokn.kncache.command;

import cn.lokn.kncache.core.Command;
import cn.lokn.kncache.core.KNCache;
import cn.lokn.kncache.core.Reply;

/**
 * @description: set command
 * @author: lokn
 * @date: 2024/08/25 22:18
 */
public class SetCommand implements Command {
    @Override
    public String name() {
        return "SET";
    }

    @Override
    public Reply<?> exec(KNCache cache, String[] args) {
        cache.set(getKey(args), getValue(args));
        return Reply.string(OK);
    }
}
