package com.netty.im.protocol.request;

import com.netty.im.protocol.Packet;
import com.netty.im.protocol.command.Command;
import lombok.Data;

@Data
public class LoginRequestPacket extends Packet {

    private String userId;
    private String userName;
    private String password;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
