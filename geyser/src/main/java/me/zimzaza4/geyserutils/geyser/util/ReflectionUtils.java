package me.zimzaza4.geyserutils.geyser.util;

import lombok.SneakyThrows;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.api.util.PlatformType;
import org.geysermc.mcprotocollib.protocol.packet.common.clientbound.ClientboundCustomPayloadPacket;
import org.geysermc.mcprotocollib.protocol.packet.common.serverbound.ServerboundCustomPayloadPacket;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ReflectionUtils {

    public static String prefix;
    public static Class<?> KEY_CLASS;

    public static Class<?> CLIENTBOUND_PAYLOAD_PACKET_CLASS;

    public static Class<?> SERVERBOUND_PAYLOAD_PACKET_CLASS;

    public static Constructor<?> SERVERBOUND_PAYLOAD_PACKET_CONSTRUCTOR;
    public static Method KEY_BUILD_METHOD;

    public static Method SERVERBOUND_GET_CHANNEL_METHOD;

    public static Method CLIENTBOUND_GET_CHANNEL_METHOD;

    public static boolean OLD_VERSION = false;

    @SneakyThrows
    public static void init() {
        PlatformType type = GeyserImpl.getInstance().platformType();
        prefix = type == PlatformType.STANDALONE || type == PlatformType.VELOCITY ? "" : "org.geysermc.geyser.platform." + type.platformName().toLowerCase() + ".shaded.";
        CLIENTBOUND_PAYLOAD_PACKET_CLASS = ClientboundCustomPayloadPacket.class;
        SERVERBOUND_PAYLOAD_PACKET_CLASS = ServerboundCustomPayloadPacket.class;
        KEY_CLASS = Class.forName(prefix + "net.kyori.adventure.key.Key");
        CLIENTBOUND_GET_CHANNEL_METHOD = CLIENTBOUND_PAYLOAD_PACKET_CLASS.getMethod("getChannel");
        SERVERBOUND_GET_CHANNEL_METHOD = SERVERBOUND_PAYLOAD_PACKET_CLASS.getMethod("getChannel");

        try {
            SERVERBOUND_PAYLOAD_PACKET_CONSTRUCTOR = SERVERBOUND_PAYLOAD_PACKET_CLASS.getConstructor(KEY_CLASS, byte[].class);
        } catch (NoSuchMethodException e) {
            OLD_VERSION = true;
            SERVERBOUND_PAYLOAD_PACKET_CONSTRUCTOR = SERVERBOUND_PAYLOAD_PACKET_CLASS.getConstructor(String.class, byte[].class);
        }
        KEY_BUILD_METHOD = KEY_CLASS.getMethod("key", String.class);
    }

    @SneakyThrows
    public static Object getChannel(ClientboundCustomPayloadPacket packet) {
        return CLIENTBOUND_GET_CHANNEL_METHOD.invoke(packet);
    }

    @SneakyThrows
    public static Object getChannel(ServerboundCustomPayloadPacket packet) {
        return SERVERBOUND_GET_CHANNEL_METHOD.invoke(packet);
    }

    @SneakyThrows
    public static ServerboundCustomPayloadPacket buildServerboundPayloadPacket(String key, byte[] data) {
        return (ServerboundCustomPayloadPacket) SERVERBOUND_PAYLOAD_PACKET_CONSTRUCTOR.newInstance(buildKey(key), data);
    }

    @SneakyThrows
    public static Object buildKey(String key) {
        if (OLD_VERSION) return key;
        return KEY_BUILD_METHOD.invoke(null, key);
    }


}
