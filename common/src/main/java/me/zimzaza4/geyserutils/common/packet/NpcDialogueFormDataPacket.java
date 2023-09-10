package me.zimzaza4.geyserutils.common.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.zimzaza4.geyserutils.common.form.element.NpcDialogueButton;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
@Setter
public class NpcDialogueFormDataPacket extends Packet {

    String formId;
    String title;
    String dialogue;
    String skinData;
    int bindEntity;
    List<NpcDialogueButton> buttons;
    String action;
    boolean hasNextForm;

}
