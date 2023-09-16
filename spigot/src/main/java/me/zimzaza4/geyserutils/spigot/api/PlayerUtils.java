package me.zimzaza4.geyserutils.spigot.api;

import me.zimzaza4.geyserutils.common.animation.Animation;
import me.zimzaza4.geyserutils.common.channel.GeyserUtilsChannels;
import me.zimzaza4.geyserutils.common.packet.AnimateEntityCustomPayloadPacket;
import me.zimzaza4.geyserutils.common.packet.CameraShakeCustomPayloadPacket;
import me.zimzaza4.geyserutils.common.util.CustomPayloadPacketUtils;
import me.zimzaza4.geyserutils.spigot.GeyserUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtils {


    public static void shakeCamera(Player player, float intensity, float duration, int type) {
        player.sendPluginMessage(GeyserUtils.getInstance(), GeyserUtilsChannels.MAIN, GeyserUtils.getPacketManager().encodePacket(new CameraShakeCustomPayloadPacket(intensity, duration, type)));
    }

    public static void playEntityAnimation(Player player, Animation animation, Entity... entityList) {
        List<Integer> idList = new ArrayList<>();
        for (Entity entity : entityList) {
            idList.add(entity.getEntityId());
        }

        playEntityAnimation(player, animation, idList);
    }

    public static void playEntityAnimation(Player player, Animation animation, List<Integer> entityList) {
        AnimateEntityCustomPayloadPacket packet = new AnimateEntityCustomPayloadPacket();
        packet.parseFromAnimation(animation);
        packet.setEntityJavaIds(entityList);
        player.sendPluginMessage(GeyserUtils.getInstance(), GeyserUtilsChannels.MAIN, GeyserUtils.getPacketManager().encodePacket(packet));

    }
}
