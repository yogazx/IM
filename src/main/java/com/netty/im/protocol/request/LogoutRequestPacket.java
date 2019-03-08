package com.netty.im.protocol.request;

import com.netty.im.protocol.Packet;
import com.netty.im.protocol.command.Command;
import lombok.Data;

@Data
public class LogoutRequestPacket extends Packet {
    @Override
    public Byte getCommand() {

        return Command.LOGOUT_REQUEST;
    }
}
