package cn.lokn.kncache.command;

import cn.lokn.kncache.core.Command;
import cn.lokn.kncache.core.Commands;
import cn.lokn.kncache.core.KNCache;
import cn.lokn.kncache.core.Reply;

/**
 * @description:
 * @author: lokn
 * @date: 2024/08/25 22:18
 */
public class CommandCommand implements Command {
    @Override
    public String name() {
        return "COMMAND";
    }

    @Override
    public Reply<?> exec(KNCache cache, String[] args) {
        return Reply.array(Commands.getCommandNames());
    }
}
