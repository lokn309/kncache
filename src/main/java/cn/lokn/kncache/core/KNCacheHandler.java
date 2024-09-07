package cn.lokn.kncache.core;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description:
 * @author: lokn
 * @date: 2024/07/17 23:39
 */
public class KNCacheHandler extends SimpleChannelInboundHandler<String> {

    private static final String CRLF = "\r\n";
    private static final String STR_PREFIX = "+";
    private static final String BULK_PREFIX = "$";
    private static final String OK = "OK";
    private static final String INFO = "KNCache server[v1.0.0], created by kn." + CRLF
            + "Mock Redis Server, at 2024-08-12 in Guangzhou." + CRLF;

    public static final KNCache CACHE = new KNCache();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                String message) throws Exception {

        String[] args = message.split(CRLF);
        System.out.println("KNCacheHandler => " + String.join(",", args));
        String cmd = args[2].toUpperCase();

        Command command = Commands.get(cmd);
        if (command != null) {
            try {
                Reply<?> reply = command.exec(CACHE, args);
                System.out.println("CMD [" + cmd + "] => " + reply.type + " => " + reply.value);
                replyContext(ctx, reply);
            } catch (Exception e) {
                Reply<String> reply = Reply.error("EXP exception with msg: " + e.getMessage());
                replyContext(ctx, reply);
            }
        } else {
            Reply<String> reply = Reply.error("ERR unsupported command [ " + cmd + "]");
            replyContext(ctx, reply);
        }
    }

    private void replyContext(ChannelHandlerContext ctx, Reply<?> reply) {
        switch(reply.getType()) {
            case INT:
                integer(ctx, (int) reply.getValue()); break;
            case ERROR:
                error(ctx, (String) reply.getValue()); break;
            case SIMPLE_STRING:
                simpleString(ctx, (String) reply.getValue()); break;
            case BULK_STRING:
                bulString(ctx, (String) reply.getValue()); break;
            case ARRAY:
                array(ctx, (String[]) reply.getValue()); break;
        }
    }

    private void error(ChannelHandlerContext ctx, String msg) {
        writeByteBuf(ctx, errorEncode(msg));
    }

    private String errorEncode(String msg) {
        return "-" + msg + CRLF;
    }

    private void integer(ChannelHandlerContext ctx, int i) {
        writeByteBuf(ctx, integerEncode(i));
    }

    private String integerEncode(int i) {
        return ":" + i + CRLF;
    }

    private void array(ChannelHandlerContext ctx, String[] array) {
        writeByteBuf(ctx, arrayEncode(array));
    }

    private String arrayEncode(Object[] array) {
        final StringBuilder sb = new StringBuilder();
        if (array == null) {
            sb.append("*-1" + CRLF);
        } else if (array.length == 0) {
            sb.append("*0" + CRLF);
        } else {
            sb.append("*" + array.length + CRLF);
            for (int i = 0; i < array.length; i++) {
                Object obj = array[i];
                if (obj == null) {
                    sb.append("*-1" + CRLF);
                } else {
                    if (obj instanceof Integer) {
                        sb.append(integerEncode((Integer) obj));
                    } else if (obj instanceof String) {
                        sb.append(bulkStringEncode((String) obj));
                    } else if (obj instanceof Object[] objs) {
                        sb.append(arrayEncode(objs));
                    }
                }
            }
        }
        return sb.toString();
    }


    private void bulString(ChannelHandlerContext ctx, String content) {
        writeByteBuf(ctx, bulkStringEncode(content));
    }

    private static String bulkStringEncode(String content) {
        String reg;
        if (content == null) {
            reg = "$-1" + CRLF;
        } else if (content.isEmpty()) {
            reg = "$0" + CRLF;
        } else {
            reg = BULK_PREFIX + content.getBytes().length + CRLF + content + CRLF;
        }
        return reg;
    }

    private void simpleString(ChannelHandlerContext ctx, String content) {
        writeByteBuf(ctx, simpleEncode(content));
    }

    private static String simpleEncode(String content) {
        String ret;
        if (content == null) {
            ret = "$-1" + CRLF;
        } else if (content.isEmpty()) {
            ret = "$0" + CRLF;
        } else {
            ret = STR_PREFIX + content + CRLF;
        }
        return ret;
    }


    private void writeByteBuf(ChannelHandlerContext ctx, String context) {
        System.out.println("wrap byte buffer and reply: " + context);
        ByteBuf buffer = Unpooled.buffer(128);
        buffer.writeBytes(context.getBytes());
        ctx.writeAndFlush(buffer);
    }
}
