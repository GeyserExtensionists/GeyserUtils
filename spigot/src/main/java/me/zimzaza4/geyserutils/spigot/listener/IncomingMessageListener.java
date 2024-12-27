package me.zimzaza4.geyserutils.spigot.listener;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import me.zimzaza4.geyserutils.common.channel.GeyserUtilsChannels;
import me.zimzaza4.geyserutils.common.packet.CustomPayloadPacket;
import me.zimzaza4.geyserutils.common.packet.form.NpcFormResponseCustomPayloadPacket;
import me.zimzaza4.geyserutils.spigot.GeyserUtils;
import me.zimzaza4.geyserutils.spigot.api.form.NpcDialogueForm;

public class IncomingMessageListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String chanel, Player player, byte[] bytes) {
        if (!chanel.equals(GeyserUtilsChannels.MAIN))
            return;

        // Decode packet
        CustomPayloadPacket packet = GeyserUtils.getPacketManager().decodePacket(bytes);
        if (!(packet instanceof NpcFormResponseCustomPayloadPacket))
            return;

        // Check if form is registered
        NpcFormResponseCustomPayloadPacket response = (NpcFormResponseCustomPayloadPacket) packet;
        if (!NpcDialogueForm.FORMS.containsKey(response.getFormId()))
            return;

        NpcDialogueForm form = NpcDialogueForm.FORMS.get(response.getFormId());

        // Handle button click
        if (form.handler() != null && response.getButtonId() != -1) {
            form.handler().accept(response.getFormId(), response.getButtonId());
        }

        // Close form
        if (response.getButtonId() == -1) {
            if (form.closeHandler() != null) {
                form.closeHandler().accept(response.getFormId());
            }
            NpcDialogueForm.FORMS.remove(response.getFormId());
        }

    }
}
