package com.netty.im.protocol;

import com.netty.im.protocol.command.Command;
import com.netty.im.protocol.request.*;
import com.netty.im.protocol.response.*;
import com.netty.im.serialize.Serializer;
import com.netty.im.serialize.impl.JSONSerialize;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

public class PacketCode {

    public static final int MAGIC_NUMBER  = 0x12345678;
    public static final PacketCode INSTANCE = new PacketCode();
    private static final Map<Byte, Class<? extends Packet>> PACKET_TYPE_MAP;
    private static final Map<Byte, Serializer> SERIALIZER_MAP;

    static {
        PACKET_TYPE_MAP = new HashMap<>();
        PACKET_TYPE_MAP.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);
        PACKET_TYPE_MAP.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);
        PACKET_TYPE_MAP.put(Command.MESSAGE_REQUEST, MessageRequestPacket.class);
        PACKET_TYPE_MAP.put(Command.MESSAGE_RESPONSE, MessageResponsePacket.class);
        PACKET_TYPE_MAP.put(Command.LOGOUT_REQUEST, LogoutRequestPacket.class);
        PACKET_TYPE_MAP.put(Command.LOGOUT_RESPONSE, LogoutResponsePacket.class);
        PACKET_TYPE_MAP.put(Command.CREATE_GROUP_REQUEST, CreateGroupRequestPacket.class);
        PACKET_TYPE_MAP.put(Command.CREATE_GROUP_RESPONSE, CreateGroupResponsePacket.class);
        PACKET_TYPE_MAP.put(Command.JOIN_GROUP_REQUEST, JoinGroupRequestPacket.class);
        PACKET_TYPE_MAP.put(Command.JOIN_GROUP_RESPONSE, JoinGroupResponsePacket.class);
        PACKET_TYPE_MAP.put(Command.QUIT_GROUP_REQUEST, QuitGroupRequestPacket.class);
        PACKET_TYPE_MAP.put(Command.QUIT_GROUP_RESPONSE, QuitGroupResponsePacket.class);
        PACKET_TYPE_MAP.put(Command.LIST_GROUP_MEMBERS_REQUEST, ListGroupMembersRequestPacket.class);
        PACKET_TYPE_MAP.put(Command.LIST_GROUP_MEMBERS_RESPONSE, ListGroupMembersResponsePacket.class);
        PACKET_TYPE_MAP.put(Command.GROUP_MESSAGE_REQUEST, GroupMessageRequestPacket.class);
        PACKET_TYPE_MAP.put(Command.GROUP_MESSAGE_RESPONSE, GroupMessageResponsePacket.class);
        PACKET_TYPE_MAP.put(Command.HEARTBEAT_REQUEST, HeartBeatRequestPacket.class);
        PACKET_TYPE_MAP.put(Command.HEARTBEAT_RESPONSE, HeartBeatResponsePacket.class);
        SERIALIZER_MAP = new HashMap<>();
        Serializer serializer = new JSONSerialize();
        SERIALIZER_MAP.put(serializer.getSerializerAlgorithm(), serializer);
    }

    /**
     * customize communication protocol
     *  +--------------+-----------+------------------------+-----------+--------------+------------------+
     *  | magic number |  version  |  serializer algorithm  |  command  |  data length |        data      |
     *  |    4 bit     |   1 bit   |          1 bit         |   1 bit   |    4 bit     |        N bit     |
     *  +--------------+-----------+------------------------+-----------+--------------+------------------+
     * @param packet
     * @param byteBuf
     */
    public void encode(ByteBuf byteBuf, Packet packet) {
        JSONSerialize serializer = new JSONSerialize();
        byte[] bytes = serializer.serialize(packet);
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(serializer.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }

    public Packet decode(ByteBuf byteBuf) {
        byteBuf.skipBytes(4);
        byteBuf.skipBytes(1);
        byte serializeAlgorithm = byteBuf.readByte();
        byte command = byteBuf.readByte();
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        // 复制内容到字节数组bytes
        byteBuf.readBytes(bytes);
        Class<? extends Packet> commandType = getCommandType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);
        if (commandType != null && serializer != null) {
            return serializer.deSerialize(commandType, bytes);
        }
        return null;
    }

    private Serializer getSerializer(byte serializeAlgorithm) {
        return SERIALIZER_MAP.get(serializeAlgorithm);
    }

    private Class<? extends Packet> getCommandType(byte command) {
        return PACKET_TYPE_MAP.get(command);
    }
}
