package com.netty.im.client.handler;

import com.netty.im.protocol.response.LogoutResponsePacket;
import com.netty.im.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LogoutResponseHandler extends SimpleChannelInboundHandler<LogoutResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutResponsePacket logoutResponsePacket) {
        // client端和server端都要解绑
        SessionUtil.unBindSession(ctx.channel());
    }
}
