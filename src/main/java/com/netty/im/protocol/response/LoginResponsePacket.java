package com.netty.im.protocol.response;

import com.netty.im.protocol.Packet;
import com.netty.im.protocol.command.Command;
import lombok.Data;

@Data
public class LoginResponsePacket extends Packet {

    private String userId;
    private String userName;
    private boolean success;
    private String reason;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }
}
