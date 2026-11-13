package me.zimzaza4.geyserutils.spigot.api.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.zimzaza4.geyserutils.common.channel.GeyserUtilsChannels;
import me.zimzaza4.geyserutils.common.form.element.NpcDialogueButton;
import me.zimzaza4.geyserutils.common.packet.NpcDialogueFormDataCustomPayloadPacket;
import me.zimzaza4.geyserutils.spigot.GeyserUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Accessors(fluent = true)
public class NpcDialogueForm {

    public static Map<String, NpcDialogueForm> FORMS = new HashMap<>();

    private static final ScheduledExecutorService CLEANUP = Executors.newSingleThreadScheduledExecutor(runnable -> {
        Thread thread = new Thread(runnable, "GeyserUtils-NpcDialogueForm");
        thread.setDaemon(true);
        return thread;
    });

    String title;
    String dialogue;
    String skinData;
    Entity bindEntity;
    boolean hasNextForm = false;
    List<NpcDialogueButton> buttons;
    BiConsumer<String, Integer> handler;
    Consumer<String> closeHandler;

    public static void closeForm(FloodgatePlayer floodgatePlayer) {
        NpcDialogueFormDataCustomPayloadPacket data = new NpcDialogueFormDataCustomPayloadPacket(null, null, null, null, -1, null, "CLOSE", false);
        Player p = Bukkit.getPlayer(floodgatePlayer.getCorrectUniqueId());
        if (p != null) {
            p.sendPluginMessage(GeyserUtils.getInstance(), GeyserUtilsChannels.MAIN, GeyserUtils.getPacketManager().encodePacket(data));
        }
    }

    public void send(FloodgatePlayer floodgatePlayer) {
        UUID formId = UUID.randomUUID();
        NpcDialogueFormDataCustomPayloadPacket data = new NpcDialogueFormDataCustomPayloadPacket(formId.toString(), title, dialogue, skinData, bindEntity.getEntityId(), buttons, "OPEN", hasNextForm);
        Player p = Bukkit.getPlayer(floodgatePlayer.getCorrectUniqueId());
        if (p != null) {

            FORMS.put(formId.toString(), this);

            p.sendPluginMessage(GeyserUtils.getInstance(), GeyserUtilsChannels.MAIN, GeyserUtils.getPacketManager().encodePacket(data));
            AtomicReference<ScheduledFuture<?>> task = new AtomicReference<>();
            task.set(CLEANUP.scheduleAtFixedRate(() -> {
                if (!p.isOnline()) {
                    FORMS.remove(formId.toString());
                }
                ScheduledFuture<?> future = task.get();
                if (!FORMS.containsKey(formId.toString()) && future != null) {
                    future.cancel(false);
                }
            }, 500, 500, TimeUnit.MILLISECONDS));
        }
    }
}
