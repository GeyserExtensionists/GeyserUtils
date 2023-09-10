package me.zimzaza4.geyserutils.common.util;

import me.zimzaza4.geyserutils.common.packet.Packet;

import java.io.*;

public class CustomPayloadPacketUtils {

    public static byte[] encodePacket(Packet packet) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            outputStream.writeObject(packet);
            outputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static Packet decodePacket(byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        try (ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream)) {
            return (Packet) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
