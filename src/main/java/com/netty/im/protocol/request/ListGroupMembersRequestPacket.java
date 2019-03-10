package com.netty.im.protocol.request;

import com.netty.im.protocol.Packet;
import com.netty.im.protocol.command.Command;
import lombok.Data;

@Data
public class ListGroupMembersRequestPacket extends Packet {

    private String groupId;

    @Override
    public Byte getCommand() {

        return Command.LIST_GROUP_MEMBERS_REQUEST;
    }
}
