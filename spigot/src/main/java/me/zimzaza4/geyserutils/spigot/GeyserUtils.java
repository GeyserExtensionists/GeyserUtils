package me.zimzaza4.geyserutils.spigot;

import lombok.Getter;
import me.zimzaza4.geyserutils.common.channel.GeyserUtilsChannels;
import me.zimzaza4.geyserutils.common.packet.NpcDialogueFormDataPacket;
import me.zimzaza4.geyserutils.common.packet.NpcFormResponsePacket;
import me.zimzaza4.geyserutils.common.packet.Packet;
import me.zimzaza4.geyserutils.common.util.CustomPayloadPacketUtils;
import me.zimzaza4.geyserutils.spigot.api.form.NpcDialogueForm;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;

public final class GeyserUtils extends JavaPlugin {

    @Getter
    public static GeyserUtils instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        Messenger messenger = this.getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(this, GeyserUtilsChannels.MAIN);
        messenger.registerIncomingPluginChannel(this, GeyserUtilsChannels.MAIN, (channel, player, message) -> {
            if (channel.equals(GeyserUtilsChannels.MAIN)) {
                Packet packet = CustomPayloadPacketUtils.decodePacket(message);
                if (packet instanceof NpcFormResponsePacket) {
                    NpcFormResponsePacket response = (NpcFormResponsePacket) packet;
                    if (NpcDialogueForm.FORMS.containsKey(response.getFormId())) {

                        BiConsumer<String, Integer> handler = NpcDialogueForm.FORMS.get(response.getFormId()).handler();
                        if (handler != null) {
                            handler.accept(response.getFormId(), response.getButtonId());
                        }
                        NpcDialogueForm.FORMS.remove(response.getFormId());
                    }

                }
            }
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
