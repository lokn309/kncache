package cn.lokn.kncache;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @description:
 * @author: lokn
 * @date: 2024/07/23 22:47
 */
public class KNCacheDecoder extends ByteToMessageDecoder {

    AtomicLong counter = new AtomicLong();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("KNCacheDecoder decodeCount: " + counter.incrementAndGet());

        if (in.readableBytes() <= 0) return;
        int count = in.readableBytes();
        int index = in.readerIndex();
        System.out.println("count: " + count + ",index: " + index);

        byte[] bytes = new byte[count];
        in.readBytes(bytes);
        String ret = new String(bytes);
        System.out.println("ret: " + ret);

        out.add(ret);
    }
}
