package com.netty.im.client.console;

import com.netty.im.protocol.request.MessageRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

public class SendToUserConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.print("发送消息给某个某个用户：");

        String toUserId = scanner.nextLine();
        String message = scanner.nextLine();
        channel.writeAndFlush(new MessageRequestPacket(toUserId, message));
    }
}
