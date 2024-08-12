package cn.lokn.kncache;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.redis.ArrayHeaderRedisMessage;
import io.netty.handler.codec.redis.BulkStringHeaderRedisMessage;
import io.netty.handler.codec.redis.DefaultBulkStringRedisContent;
import io.netty.handler.codec.redis.RedisMessage;

import java.security.cert.CRL;

/**
 * @description:
 * @author: lokn
 * @date: 2024/07/17 23:39
 */
public class KNCacheHandler extends SimpleChannelInboundHandler<String> {

    private static final String CRLF= "\r\n";
    private static final String STR_PREFIX = "+";
    private static final String OK = STR_PREFIX + "OK" + CRLF;
    private static final String INFO = "KNCache server[v1.0.0], created by kn." + CRLF
                                    + "Mock Redis Server, at 2024-08-12 in Guangzhou." + CRLF;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                String message) throws Exception {

        String[] args = message.split(CRLF);
        System.out.println("KNCacheHandler => " + String.join(",", args));
        String cmd = args[2].toUpperCase();
        if ("COMMAND".equals(cmd)) {
            // 此处需要通过byteBuff 包装一下后，才能识别
            writeByteBuf(ctx, "*2"
                    + CRLF + "$7"
                    + CRLF + "COMMAND"
                    + CRLF + "$4"
                    + CRLF + "PING"
                    + CRLF);
        } else if ("PING".equals(cmd)) {
            String ret = "PONG";
            if (args.length > 4) {
                ret = args[4];
            }
            simpleString(ctx, ret);
        } else if ("INFO".equals(cmd)) {
            bulString(ctx, INFO);
        } else {
            simpleString(ctx, OK);
        }
    }

    private void bulString(ChannelHandlerContext ctx, String content) {
        writeByteBuf(ctx, "$" + content.getBytes().length + CRLF + content + CRLF);
    }
    private void simpleString(ChannelHandlerContext ctx, String content) {
        writeByteBuf(ctx, STR_PREFIX + content + CRLF);
    }

    private void writeByteBuf(ChannelHandlerContext ctx, String context) {
        System.out.println("wrap byte buffer and reply: " + context);
        ByteBuf buffer = Unpooled.buffer(128);
        buffer.writeBytes(context.getBytes());
        ctx.writeAndFlush(buffer);
    }
}
