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

        NpcDialogueForm form = NpcDialogueForms.getOpenNpcDialogueForms(geyserSession);

        if (form == null) {
            return;
        }

        if (form.dialogueButtons().isEmpty()) {
            if (packet.getRequestType().equals(NpcRequestType.EXECUTE_CLOSING_COMMANDS)) {
                NpcDialogueForms.removeNpcDialogueForm(geyserSession, form);
            }

            return;
        }

        System.out.println(packet);

        Button button = form.dialogueButtons().get(packet.getActionType());

        if (button == null) {
            return;
        }


        if ((button.mode().equals(NpcDialogueButton.ButtonMode.ON_ENTER) && packet.getRequestType().equals(NpcRequestType.EXECUTE_OPENING_COMMANDS)) ||
                (button.mode().equals(NpcDialogueButton.ButtonMode.BUTTON_MODE) && packet.getRequestType().equals(NpcRequestType.EXECUTE_COMMAND_ACTION)) ||
                (button.mode().equals(NpcDialogueButton.ButtonMode.ON_EXIT) && packet.getRequestType().equals(NpcRequestType.EXECUTE_CLOSING_COMMANDS))) {
            button.click().run();
            if (!form.hasNextForm()) {
                form.close(geyserSession);
            }
        }
    }
}
