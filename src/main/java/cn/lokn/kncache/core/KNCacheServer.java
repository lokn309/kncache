package cn.lokn.kncache.core;

import cn.lokn.kncache.KNPlugin;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.stereotype.Component;


/**
 * @description: kn cache server
 * @author: lokn
 * @date: 2024/07/17 23:10
 */
@Component
public class KNCacheServer implements KNPlugin {

    int port = 6379;
    NioEventLoopGroup bossGroup;
    NioEventLoopGroup workGroup;
    Channel channel;

    @Override
    public void init() {
         bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("redis-boss"));
         workGroup = new NioEventLoopGroup(16, new DefaultThreadFactory("redis-work"));
    }

    @Override
    public void startup() {
        try {
            final ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.option(ChannelOption.SO_BACKLOG, 126)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.SO_RCVBUF, 32 * 1024)
                    .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
                    .childOption(EpollChannelOption.SO_REUSEPORT, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            bootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new KNCacheDecoder());
                            ch.pipeline().addLast(new KNCacheHandler());
                        }
                    });

            channel = bootstrap.bind(port).sync().channel();
            System.out.println("开启netty redis服务器，端口为 " + port);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    @Override
    public void shutdown() {
        if (this.channel != null) {
            this.channel.close();
            this.channel = null;
        }
        if (this.bossGroup != null) {
            this.bossGroup.shutdownGracefully();
            this.bossGroup = null;
        }
        if (this.workGroup != null) {
            this.workGroup.shutdownGracefully();
            this.workGroup = null;
        }

    }
}
