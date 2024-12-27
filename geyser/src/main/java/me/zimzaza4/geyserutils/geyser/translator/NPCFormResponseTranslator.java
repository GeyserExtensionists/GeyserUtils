package me.zimzaza4.geyserutils.geyser.translator;

import org.cloudburstmc.protocol.bedrock.data.NpcRequestType;
import org.cloudburstmc.protocol.bedrock.packet.NpcRequestPacket;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.PacketTranslator;

import me.zimzaza4.geyserutils.common.form.NpcDialogueButton;
import me.zimzaza4.geyserutils.geyser.form.NpcDialogueForm;
import me.zimzaza4.geyserutils.geyser.form.NpcDialogueForms;
import me.zimzaza4.geyserutils.geyser.form.element.Button;

public class NPCFormResponseTranslator extends PacketTranslator<NpcRequestPacket> {

    @Override
    public void translate(GeyserSession geyserSession, NpcRequestPacket packet) {
        // Retrieve form
        NpcDialogueForm form = NpcDialogueForms.getOpenNpcDialogueForms(geyserSession);
        if (form == null)
            return;

        // Close form
        if (packet.getRequestType().equals(NpcRequestType.EXECUTE_CLOSING_COMMANDS)
            && packet.getSceneName().equals(form.sceneName())) {
            form.close(geyserSession);
            return;
        }

        // Retrieve button
        Button button = form.dialogueButtons().get(packet.getActionType());
        if (button == null
            || !button.mode().equals(NpcDialogueButton.ButtonMode.BUTTON_MODE)
            || !packet.getRequestType().equals(NpcRequestType.EXECUTE_COMMAND_ACTION))
            return;

        button.click().run();
        if (!button.hasNextForm())
            form.close(geyserSession);
    }
}
