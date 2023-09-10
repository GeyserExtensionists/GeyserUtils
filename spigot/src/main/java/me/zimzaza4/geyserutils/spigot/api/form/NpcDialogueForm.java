package me.zimzaza4.geyserutils.spigot.api.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.zimzaza4.geyserutils.common.channel.GeyserUtilsChannels;
import me.zimzaza4.geyserutils.common.form.element.NpcDialogueButton;
import me.zimzaza4.geyserutils.common.packet.NpcDialogueFormDataPacket;
import me.zimzaza4.geyserutils.common.util.CustomPayloadPacketUtils;
import me.zimzaza4.geyserutils.spigot.GeyserUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Accessors( fluent = true )
public class NpcDialogueForm {

    public static Map<String, NpcDialogueForm> FORMS = new HashMap<>();

    String title;
    String dialogue;
    String skinData;
    Entity bindEntity;
    boolean hasNextForm = false;
    List<NpcDialogueButton> buttons;
    BiConsumer<String, Integer> handler;

    public void send(FloodgatePlayer floodgatePlayer) {
        UUID formId = UUID.randomUUID();
        NpcDialogueFormDataPacket data = new NpcDialogueFormDataPacket(formId.toString(), title, dialogue, skinData, bindEntity.getEntityId(), buttons, "OPEN",  hasNextForm);
        Player p = Bukkit.getPlayer(floodgatePlayer.getCorrectUniqueId());
        if (p!= null) {

            FORMS.put(formId.toString(), this);

            p.sendPluginMessage(GeyserUtils.getInstance(), GeyserUtilsChannels.MAIN, CustomPayloadPacketUtils.encodePacket(data));
            new BukkitRunnable() {

                 @Override
                 public void run() {
                     if (!FORMS.containsKey(formId.toString())) {
                         this.cancel();
                     }
                     if (!p.isOnline()) {
                         FORMS.remove(formId.toString());
                     }

                 }
            }.runTaskTimerAsynchronously(GeyserUtils.getInstance(), 10, 10);
        }
    }

    public static void closeForm(FloodgatePlayer floodgatePlayer) {
        NpcDialogueFormDataPacket data = new NpcDialogueFormDataPacket(null, null, null, null, -1, null, "CLOSE", false);
        Player p = Bukkit.getPlayer(floodgatePlayer.getCorrectUniqueId());
        if (p != null) {
            p.sendPluginMessage(GeyserUtils.getInstance(), GeyserUtilsChannels.MAIN, CustomPayloadPacketUtils.encodePacket(data));
        }
    }
}
