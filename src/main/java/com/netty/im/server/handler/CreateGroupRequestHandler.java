package com.netty.im.server.handler;

import com.netty.im.protocol.request.CreateGroupRequestPacket;
import com.netty.im.protocol.response.CreateGroupResponsePacket;
import com.netty.im.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {

    public static final CreateGroupRequestHandler INSTANCE = new CreateGroupRequestHandler();

    private CreateGroupRequestHandler() {}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket msg) throws Exception {
        List<String> userIds = msg.getUserIds();
        List<String> userNames = new ArrayList<>();

        // 创建一个channel分组
        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());
        // 筛选出待加入群聊的用户的channel和username
        for (String userId : userIds) {
            Channel channel = SessionUtil.getChannel(userId);
            if (channel != null) {
                channelGroup.add(channel);
                userNames.add(SessionUtil.getSession(channel).getUserName());
            }
        }
        String groupId = UUID.randomUUID().toString().split("-")[0];
        CreateGroupResponsePacket createGroupResponsePacket = new CreateGroupResponsePacket();
        createGroupResponsePacket.setSuccess(true);
        createGroupResponsePacket.setUserNameList(userNames);
        createGroupResponsePacket.setGroupId(groupId);

        channelGroup.writeAndFlush(createGroupResponsePacket);

        System.out.print("群创建成功，id 为[" + createGroupResponsePacket.getGroupId() + "], ");
        System.out.println("群里面有：" + createGroupResponsePacket.getUserNameList());
        // 保存群组相关信息
        SessionUtil.bindChanelGroup(groupId, channelGroup);
    }
}
