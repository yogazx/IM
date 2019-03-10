package com.netty.im.server;

import com.netty.im.codec.PacketCodeHandler;
import com.netty.im.codec.PacketDecode;
import com.netty.im.codec.PacketEncode;
import com.netty.im.codec.Spliter;
import com.netty.im.handler.IMIdleStateHandler;
import com.netty.im.server.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;

public class NettyServer {

    private static final int PORT = 8000;

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 添加服务端空闲检测
                        ch.pipeline().addLast(new IMIdleStateHandler());
                        ch.pipeline().addLast(new Spliter());
//                        使用PacketCodeHandler替代PacketDecode和PacketEncode
                        ch.pipeline().addLast(PacketCodeHandler.INSTANCE);
                        ch.pipeline().addLast(LoginRequestHandler.INSTANCE);
                        ch.pipeline().addLast(AuthHandler.INSTANCE);
                        ch.pipeline().addLast(IMHandler.INSTANCE);
//                        ch.pipeline().addLast(new PacketDecode());
//                        ch.pipeline().addLast(new LoginRequestHandler());
//                        ch.pipeline().addLast(new AuthHandler());
//                        ch.pipeline().addLast(new MessageRequestHandler());
//                        ch.pipeline().addLast(new CreateGroupRequestHandler());
//                        ch.pipeline().addLast(new JoinGroupRequestHandler());
//                        ch.pipeline().addLast(new QuitGroupRequestHandler());
//                        ch.pipeline().addLast(new ListGroupMembersRequestHandler());
//                        ch.pipeline().addLast(new LogoutRequestHandler());
//                        ch.pipeline().addLast(new PacketEncode());
                    }
                });
        bind(serverBootstrap, PORT);
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
            }
        });
    }
}
