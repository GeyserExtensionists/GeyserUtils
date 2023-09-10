package me.zimzaza4.geyserutils.geyser.form;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.geysermc.geyser.session.GeyserSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NpcDialogueForms {
    private static final Map<GeyserSession, NpcDialogueForm> npcDialogueForms = new HashMap<>();

    public static void removeNpcDialogueForm(GeyserSession session, NpcDialogueForm form) {
        form.closeHandler().run();
        npcDialogueForms.remove(session);
    }

    public static NpcDialogueForm getOpenNpcDialogueForms(GeyserSession session) {
        return npcDialogueForms.get(session);

    }

    public static void addNpcDialogueForm(GeyserSession session, NpcDialogueForm form) {

        npcDialogueForms.put(session, form);

    }
}
