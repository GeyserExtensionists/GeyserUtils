package me.zimzaza4.geyserutils.spigot.api;

import me.zimzaza4.geyserutils.common.animation.Animation;
import me.zimzaza4.geyserutils.common.camera.instruction.Instruction;
import me.zimzaza4.geyserutils.common.channel.GeyserUtilsChannels;
import me.zimzaza4.geyserutils.common.packet.*;
import me.zimzaza4.geyserutils.common.particle.CustomParticle;
import me.zimzaza4.geyserutils.common.util.Pos;
import me.zimzaza4.geyserutils.spigot.GeyserUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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

    public static void sendCameraInstruction(Player player, Instruction instruction) {
        CameraInstructionCustomPayloadPacket packet = new CameraInstructionCustomPayloadPacket();
        packet.setInstruction(instruction);
        player.sendPluginMessage(GeyserUtils.getInstance(), GeyserUtilsChannels.MAIN, GeyserUtils.getPacketManager().encodePacket(packet));

    }

    public static void sendCustomParticle(Player player, Location location, CustomParticle particle) {
        CustomParticleEffectPayloadPacket packet = new CustomParticleEffectPayloadPacket();
        packet.setParticle(particle);
        packet.setPos(new Pos((float) location.getX(), (float) location.getY(), (float) location.getZ()));
        player.sendPluginMessage(GeyserUtils.getInstance(), GeyserUtilsChannels.MAIN, GeyserUtils.getPacketManager().encodePacket(packet));
    }

    public static void sendCustomSkin(Player player, Entity entity, String skin) {
        CustomSkinPayloadPacket skinPayloadPacket = new CustomSkinPayloadPacket();
        skinPayloadPacket.setSkinId(skin);
        skinPayloadPacket.setEntityId(entity.getEntityId());
        player.sendPluginMessage(GeyserUtils.getInstance(), GeyserUtilsChannels.MAIN, GeyserUtils.getPacketManager().encodePacket(skinPayloadPacket));

    }

    public static void sendCustomHitBox(Player player, Entity entity, float height, float width) {
        CustomEntityDataPacket packet = new CustomEntityDataPacket();
        packet.setEntityId(entity.getEntityId());
        packet.setWidth(width);
        packet.setHeight(height);
        player.sendPluginMessage(GeyserUtils.getInstance(), GeyserUtilsChannels.MAIN, GeyserUtils.getPacketManager().encodePacket(packet));

    }

    public static void sendCustomScale(Player player, Entity entity, float scale) {
        CustomEntityDataPacket packet = new CustomEntityDataPacket();
        packet.setEntityId(entity.getEntityId());
        packet.setScale(scale);
        player.sendPluginMessage(GeyserUtils.getInstance(), GeyserUtilsChannels.MAIN, GeyserUtils.getPacketManager().encodePacket(packet));

    }

    public static void setCustomEntity(Player player, int entityId, String def) {
        CustomEntityPacket packet = new CustomEntityPacket(entityId, def);
        player.sendPluginMessage(GeyserUtils.getInstance(), GeyserUtilsChannels.MAIN, GeyserUtils.getPacketManager().encodePacket(packet));

    }

    public static void sendEntityScoreUpdate(Player player, Entity entity, String objective, int score) {
        UpdateEntityScorePacket packet = new UpdateEntityScorePacket(entity.getEntityId(), objective, score);
        player.sendPluginMessage(GeyserUtils.getInstance(), GeyserUtilsChannels.MAIN, GeyserUtils.getPacketManager().encodePacket(packet));

    }

    // (yes I'm aware it's "horrible" code, I'm just matching the energy of this plugin), also this aint player packets at all lmao

    public static void registerProperty(Player player, Entity entity, String identifier, Class<?> type) {
        EntityPropertyRegisterPacket packet = new EntityPropertyRegisterPacket();
        packet.setEntityId(entity.getEntityId());
        packet.setIdentifier(identifier);
        packet.setType(type);
        player.sendPluginMessage(GeyserUtils.getInstance(), GeyserUtilsChannels.MAIN, GeyserUtils.getPacketManager().encodePacket(packet));
    }

    public static void sendBoolProperty(Player player, Entity entity, String identifier, Boolean value) {
        EntityPropertyPacket<Boolean> packet = new EntityPropertyPacket<>();
        packet.setEntityId(entity.getEntityId());
        packet.setIdentifier(identifier);
        packet.setValue(value);
        player.sendPluginMessage(GeyserUtils.getInstance(), GeyserUtilsChannels.MAIN, GeyserUtils.getPacketManager().encodePacket(packet));
    }

    public static void sendFloatProperty(Player player, Entity entity, String identifier, Float value) {
        EntityPropertyPacket<Float> packet = new EntityPropertyPacket<>();
        packet.setEntityId(entity.getEntityId());
        packet.setIdentifier(identifier);
        packet.setValue(value);
        player.sendPluginMessage(GeyserUtils.getInstance(), GeyserUtilsChannels.MAIN, GeyserUtils.getPacketManager().encodePacket(packet));
    }

    public static void sendIntProperty(Player player, Entity entity, String identifier, Integer value) {
        EntityPropertyPacket<Integer> packet = new EntityPropertyPacket<>();
        packet.setEntityId(entity.getEntityId());
        packet.setIdentifier(identifier);
        packet.setValue(value);
        player.sendPluginMessage(GeyserUtils.getInstance(), GeyserUtilsChannels.MAIN, GeyserUtils.getPacketManager().encodePacket(packet));
    }
}
