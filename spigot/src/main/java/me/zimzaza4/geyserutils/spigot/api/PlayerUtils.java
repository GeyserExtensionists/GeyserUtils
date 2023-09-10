package me.zimzaza4.geyserutils.spigot.api;

import me.zimzaza4.geyserutils.common.channel.GeyserUtilsChannels;
import me.zimzaza4.geyserutils.common.packet.CameraShakePacket;
import me.zimzaza4.geyserutils.common.util.CustomPayloadPacketUtils;
import me.zimzaza4.geyserutils.spigot.GeyserUtils;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class PlayerUtils {


    public static void shakeCamera(Player player, float intensity, float duration, int type) {
        player.sendPluginMessage(GeyserUtils.getInstance(), GeyserUtilsChannels.MAIN, CustomPayloadPacketUtils.encodePacket(new CameraShakePacket(intensity, duration, type)));

    }
}
