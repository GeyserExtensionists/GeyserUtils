package me.zimzaza4.geyserutils.common.manager;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import me.zimzaza4.geyserutils.common.packet.CameraShakeCustomPayloadPacket;
import me.zimzaza4.geyserutils.common.packet.CustomPayloadPacket;
import me.zimzaza4.geyserutils.common.packet.NpcDialogueFormDataCustomPayloadPacket;

import java.io.*;

public class PacketManager {

    private final Kryo kryo = new Kryo();

    public PacketManager() {
        init();
    }

    public void init() {
        kryo.setRegistrationRequired(false);
    }
    public void registerPacket(Class<? extends CustomPayloadPacket> clazz) {
        kryo.register(clazz);
    }

    public byte[] encodePacket(CustomPayloadPacket packet) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (Output output = new Output()) {
            kryo.writeObject(output, packet);
            return byteArrayOutputStream.toByteArray();
        }

    }

    public CustomPayloadPacket decodePacket(byte[] bytes) {
        try (Input input = new Input(bytes)) {
            return kryo.readObject(input, CustomPayloadPacket.class);
        }
    }
}
