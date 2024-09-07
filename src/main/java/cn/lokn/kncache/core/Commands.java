package cn.lokn.kncache.core;

import cn.lokn.kncache.command.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @description: command
 * @author: lokn
 * @date: 2024/08/25 22:19
 */
public class Commands {

    private static Map<String, Command> ALL = new LinkedHashMap<>();

    static {
        initCommand();
    }

    private static void initCommand() {
        // command
        register(new PingCommand());
        register(new InfoCommand());
        register(new CommandCommand());

        // String
        register(new SetCommand());
        register(new GetCommand());
        register(new StrlenCommand());
        register(new DelCommand());
        register(new ExistsCommand());
        register(new MgetCommand());
        register(new MsetCommand());
        register(new IncrCommand());
        register(new DecrCommand());

        // list
        // Lpush Rpush Lpop Rpop Llen Lindex Lrange
        register(new LpushCommand());
        register(new LpopCommand());
        register(new RpopCommand());
        register(new RpushCommand());
        register(new LlenCommand());
        register(new LindexCommand());
        register(new LrangeCommand());

        // set
        register(new SaddCommand());
        register(new SmembersCommand());
        register(new ScardCommand());
        register(new SpopCommand());
        register(new SremCommand());
        register(new SismemberCommand());

        // hash: hset hget hlen hgetall hdel hexists hmget
        register(new HsetCommand());
        register(new HgetallCommand());
        register(new HgetCommand());
        register(new HlenCommand());
        register(new HdelCommand());
        register(new HmgetCommand());
        register(new HexistsCommand());

        // zset


    }

    public static void register(Command command) {
        ALL.put(command.name(), command);
    }

    public static Command get(String name) {
        return ALL.get(name);
    }

    public static String[] getCommandNames() {
        return ALL.keySet().toArray(new String[0]);
    }

}
