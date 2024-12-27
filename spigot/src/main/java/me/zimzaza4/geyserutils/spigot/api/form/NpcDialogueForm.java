package me.zimzaza4.geyserutils.spigot.api.form;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.zimzaza4.geyserutils.common.form.NpcDialogueButton;
import me.zimzaza4.geyserutils.common.packet.form.NpcDialogueFormDataCustomPayloadPacket;
import me.zimzaza4.geyserutils.spigot.GeyserUtils;

@AllArgsConstructor
@Setter
@Getter
@Builder
@Accessors(fluent = true)
public class NpcDialogueForm {

    public static Map<String, NpcDialogueForm> FORMS = new HashMap<>();

    private final String title;
    private final String dialogue;
    private final String skinData;
    private final Entity bindEntity;
    private final boolean hasNextForm;
    private final List<NpcDialogueButton> buttons;
    private final BiConsumer<String, Integer> handler;
    private final Consumer<String> closeHandler;

    public static void closeForm(FloodgatePlayer floodgatePlayer) {
        Player player = Bukkit.getPlayer(floodgatePlayer.getCorrectUniqueId());
        if (player == null)
            return;

        // Create and send packet
        GeyserUtils.closeForm(player);
    }

    public void send(FloodgatePlayer floodgatePlayer) {
        UUID formId = UUID.randomUUID();

        // Get player
        Player player = Bukkit.getPlayer(floodgatePlayer.getCorrectUniqueId());
        if (player == null)
            return;

        // Create packet
        NpcDialogueFormDataCustomPayloadPacket data = new NpcDialogueFormDataCustomPayloadPacket(
                formId.toString(),
                title,
                dialogue,
                skinData,
                bindEntity.getEntityId(),
                buttons,
                "OPEN",
                hasNextForm
        );

        // Register form
        FORMS.put(formId.toString(), this);

        // Send packet
        GeyserUtils.sendPacket(player, data);

        // Register task
        new BukkitRunnable() {
            @Override
            public void run() {
                if (FORMS.containsKey(formId.toString()) && player.isOnline())
                    return;

                FORMS.remove(formId.toString());
                this.cancel();
            }
        }.runTaskTimerAsynchronously(GeyserUtils.getInstance(), 10, 10);
    }
}
