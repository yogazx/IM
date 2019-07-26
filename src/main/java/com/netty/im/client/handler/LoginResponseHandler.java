package com.netty.im.client.handler;

import com.netty.im.protocol.response.LoginResponsePacket;
import com.netty.im.session.Session;
import com.netty.im.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket msg) throws Exception {
        String userId = msg.getUserId();
        String userName = msg.getUserName();
        if (msg.isSuccess()) {
            System.out.println("[" + userName + "]登录成功，userId 为: " + msg.getUserId());
            // 客户端和服务端都要绑定，因为实际场景下客户端与服务端会部署在不同机器上
            SessionUtil.bindSession(new Session(userId, userName), ctx.channel());
        } else {
            System.out.println("[" + userName + "]登录失败，原因：" + msg.getReason());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接被关闭!");
    }
}
