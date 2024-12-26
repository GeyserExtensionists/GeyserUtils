package me.zimzaza4.geyserutils.common.manager;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.zimzaza4.geyserutils.common.packet.CustomPayloadPacket;

import java.io.IOException;

public class PacketManager {

    private final ObjectMapper objectMapper;

    public PacketManager() {
        objectMapper = new ObjectMapper();
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.EVERYTHING, JsonTypeInfo.As.PROPERTY);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }


    public byte[] encodePacket(CustomPayloadPacket packet) {

        try {
            return objectMapper.writeValueAsBytes(packet);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public CustomPayloadPacket decodePacket(byte[] bytes) {
        try {
            return objectMapper.readValue(bytes, CustomPayloadPacket.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
