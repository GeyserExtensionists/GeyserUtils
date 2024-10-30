package me.zimzaza4.geyserutils.spigot;

import lombok.Getter;
import me.zimzaza4.geyserutils.common.camera.data.CameraPreset;
import me.zimzaza4.geyserutils.common.channel.GeyserUtilsChannels;
import me.zimzaza4.geyserutils.common.manager.PacketManager;
import me.zimzaza4.geyserutils.common.packet.CustomPayloadPacket;
import me.zimzaza4.geyserutils.common.packet.NpcFormResponseCustomPayloadPacket;
import me.zimzaza4.geyserutils.spigot.api.form.NpcDialogueForm;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

public final class GeyserUtils extends JavaPlugin {

    @Getter
    private static GeyserUtils instance;

    @Getter
    private static PacketManager packetManager;
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        packetManager = new PacketManager();
        Messenger messenger = this.getServer().getMessenger();

        CameraPreset.load();
        messenger.registerOutgoingPluginChannel(this, GeyserUtilsChannels.MAIN);
        messenger.registerIncomingPluginChannel(this, GeyserUtilsChannels.MAIN, (channel, player, message) -> {
            if (channel.equals(GeyserUtilsChannels.MAIN)) {
                CustomPayloadPacket packet = packetManager.decodePacket(message);
                if (packet instanceof NpcFormResponseCustomPayloadPacket) {
                    NpcFormResponseCustomPayloadPacket response = (NpcFormResponseCustomPayloadPacket) packet;
                    if (NpcDialogueForm.FORMS.containsKey(response.getFormId())) {

                        NpcDialogueForm form = NpcDialogueForm.FORMS.get(response.getFormId());

                        if (form.handler() != null) {
                            if (response.getButtonId() != -1) {
                                form.handler().accept(response.getFormId(), response.getButtonId());
                            }
                        }
                        if (response.getButtonId() == -1) {
                            if (form.closeHandler() != null) {
                                form.closeHandler().accept(response.getFormId());
                            }
                            NpcDialogueForm.FORMS.remove(response.getFormId());
                        }


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
