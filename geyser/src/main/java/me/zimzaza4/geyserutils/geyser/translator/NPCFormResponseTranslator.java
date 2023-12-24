package me.zimzaza4.geyserutils.geyser.translator;

import me.zimzaza4.geyserutils.common.form.element.NpcDialogueButton;
import me.zimzaza4.geyserutils.geyser.form.NpcDialogueForm;
import me.zimzaza4.geyserutils.geyser.form.NpcDialogueForms;
import me.zimzaza4.geyserutils.geyser.form.element.Button;
import org.cloudburstmc.protocol.bedrock.data.NpcRequestType;
import org.cloudburstmc.protocol.bedrock.packet.NpcRequestPacket;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.PacketTranslator;
import org.geysermc.geyser.translator.protocol.Translator;

public class NPCFormResponseTranslator extends PacketTranslator<NpcRequestPacket> {

    @Override
    public void translate(GeyserSession geyserSession, NpcRequestPacket packet) {

        // System.out.println(packet);
        NpcDialogueForm form = NpcDialogueForms.getOpenNpcDialogueForms(geyserSession);

        if (form == null) {
            return;
        }

        if (packet.getRequestType().equals(NpcRequestType.EXECUTE_CLOSING_COMMANDS)) {
            if (packet.getSceneName().equals(form.sceneName())) {
                // System.out.println("CLOSE FORM");
                form.close(geyserSession);
                return;
            }
        }




        Button button = form.dialogueButtons().get(packet.getActionType());

        if (button == null) {
            return;
        }

        if (button.mode().equals(NpcDialogueButton.ButtonMode.BUTTON_MODE) && packet.getRequestType().equals(NpcRequestType.EXECUTE_COMMAND_ACTION)) {
            button.click().run();
            if (!button.hasNextForm()) {
                form.close(geyserSession);
            }
        }

    }
}
