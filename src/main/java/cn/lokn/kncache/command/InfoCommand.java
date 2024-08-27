package cn.lokn.kncache.command;

import cn.lokn.kncache.core.Command;
import cn.lokn.kncache.core.KNCache;
import cn.lokn.kncache.core.Reply;

/**
 * @description:
 * @author: lokn
 * @date: 2024/08/25 22:18
 */
public class InfoCommand implements Command {

    private static final String INFO = "KNCache server[v1.0.1], created by kn." + CRLF
            + "Mock Redis Server, at 2024-08-12 in Guangzhou." + CRLF;

    @Override
    public String name() {
        return "INFO";
    }

    @Override
    public Reply<?> exec(KNCache cache, String[] args) {
        return Reply.string(INFO);
    }
}
