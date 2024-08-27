package cn.lokn.kncache.command;

import cn.lokn.kncache.core.Command;
import cn.lokn.kncache.core.KNCache;
import cn.lokn.kncache.core.Reply;

/**
 * @description:
 * @author: lokn
 * @date: 2024/08/25 22:18
 */
public class PingCommand implements Command {
    @Override
    public String name() {
        return "PING";
    }

    @Override
    public Reply<?> exec(KNCache cache, String[] args) {
        String ret = "PONG";
        if (args.length >= 5) {
            ret = getKey(args);
        }
        return Reply.string(ret);
    }
}
