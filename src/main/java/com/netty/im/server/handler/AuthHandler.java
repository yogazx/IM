package com.netty.im.server.handler;

import com.netty.im.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class AuthHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!SessionUtil.hasLogin(ctx.channel())) {
            ctx.channel().close();
        } else {
            // 防止每次收到信息都验证一下是否已登录
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        }
    }
}
