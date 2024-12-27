package me.zimzaza4.geyserutils.common.packet.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.zimzaza4.geyserutils.common.form.element.NpcDialogueButton;
import me.zimzaza4.geyserutils.common.packet.CustomPayloadPacket;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@Setter
public class NpcDialogueFormDataCustomPayloadPacket extends CustomPayloadPacket {

    String formId;
    String title;
    String dialogue;
    String skinData;
    int bindEntity;
    List<NpcDialogueButton> buttons;
    String action;
    boolean hasNextForm;

}
