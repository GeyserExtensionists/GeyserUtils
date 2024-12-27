package me.zimzaza4.geyserutils.spigot;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

import lombok.Getter;
import me.zimzaza4.geyserutils.common.camera.data.CameraPreset;
import me.zimzaza4.geyserutils.common.channel.GeyserUtilsChannels;
import me.zimzaza4.geyserutils.common.manager.PacketManager;
import me.zimzaza4.geyserutils.common.packet.CustomPayloadPacket;
import me.zimzaza4.geyserutils.common.packet.form.NpcDialogueFormDataCustomPayloadPacket;
import me.zimzaza4.geyserutils.spigot.listener.IncomingMessageListener;

public final class GeyserUtils extends JavaPlugin {

    @Getter
    private static GeyserUtils instance;

    @Getter
    private static PacketManager packetManager;

    public static void sendPacket(Player player, CustomPayloadPacket packet) {
        player.sendPluginMessage(GeyserUtils.getInstance(), GeyserUtilsChannels.MAIN, GeyserUtils.getPacketManager().encodePacket(packet));
    }

    public static void closeForm(Player player) {
        GeyserUtils.sendPacket(player, new NpcDialogueFormDataCustomPayloadPacket(
                null,
                null,
                null,
                null,
                -1,
                null,
                "CLOSE",
                false
        ));
    }

    @Override
    public void onEnable() {
        instance = this;
        packetManager = new PacketManager();
        Messenger messenger = this.getServer().getMessenger();

        CameraPreset.load();
        messenger.registerOutgoingPluginChannel(this, GeyserUtilsChannels.MAIN);
        messenger.registerIncomingPluginChannel(this, GeyserUtilsChannels.MAIN, new IncomingMessageListener());
    }

    @Override
    public void onDisable() {
        Bukkit.getMessenger().unregisterIncomingPluginChannel(this, GeyserUtilsChannels.MAIN);
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, GeyserUtilsChannels.MAIN);
    }
}
