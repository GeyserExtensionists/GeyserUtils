package me.zimzaza4.geyserutils.spigot.api;

import me.zimzaza4.geyserutils.common.packet.*;
import me.zimzaza4.geyserutils.common.packet.entity.EntityPropertyRegisterPacket;
import me.zimzaza4.geyserutils.spigot.GeyserUtils;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.Map;

public class EntityUtils {

    public static void sendCustomHitBox(Player player, int id, float height, float width) {
        me.zimzaza4.geyserutils.common.packet.entity.CustomEntityDataPacket packet = new me.zimzaza4.geyserutils.common.packet.entity.CustomEntityDataPacket();
        packet.setEntityId(id);
        packet.setWidth(width);
        packet.setHeight(height);
        GeyserUtils.sendPacket(player, packet);
    }

    public static void sendCustomScale(Player player, int id, float scale) {
        me.zimzaza4.geyserutils.common.packet.entity.CustomEntityDataPacket packet = new me.zimzaza4.geyserutils.common.packet.entity.CustomEntityDataPacket();
        packet.setEntityId(id);
        packet.setScale(scale);
        GeyserUtils.sendPacket(player, packet);
    }

    public static void sendCustomColor(Player player, int id, Color color) {
        me.zimzaza4.geyserutils.common.packet.entity.CustomEntityDataPacket packet = new me.zimzaza4.geyserutils.common.packet.entity.CustomEntityDataPacket();
        packet.setEntityId(id);
        packet.setColor(color.getRGB());
        GeyserUtils.sendPacket(player, packet);
    }

    public static void sendVariant(Player player, int id, int variant) {
        me.zimzaza4.geyserutils.common.packet.entity.CustomEntityDataPacket packet = new me.zimzaza4.geyserutils.common.packet.entity.CustomEntityDataPacket();
        packet.setEntityId(id);
        packet.setVariant(variant);
        GeyserUtils.sendPacket(player, packet);
    }


    public static void setCustomEntity(Player player, int entityId, String def) {
        me.zimzaza4.geyserutils.common.packet.entity.CustomEntityPacket packet = new me.zimzaza4.geyserutils.common.packet.entity.CustomEntityPacket(entityId, def);
        GeyserUtils.sendPacket(player, packet);
    }

    // (yes I'm aware it's "horrible" code), also this aint player packets at all lmao
    // right, so this part needs to be refactored xD
    // the plugin didn't have this much functionality in its earliest days (it even just have camera shakes),
    // so I didn't think too much about it

    public static void registerProperty(Player player, int id, String identifier, Class<?> type) {
        GeyserUtils.sendPacket(player, new EntityPropertyRegisterPacket(
                id,
                identifier,
                type
        ));
    }

    public static <T> void sendProperty(Player player, int id, String identifier, T value) {
        GeyserUtils.sendPacket(player, new me.zimzaza4.geyserutils.common.packet.entity.EntityPropertyPacket<>(
                id,
                identifier,
                value
        ));
    }

    public static <T> void sendProperties(Player player, int id, Map<String, T> bundle) {
        GeyserUtils.sendPacket(player, BundlePacket.create(bundle));
    }
}
