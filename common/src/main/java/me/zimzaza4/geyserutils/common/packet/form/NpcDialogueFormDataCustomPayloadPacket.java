package me.zimzaza4.geyserutils.common.packet.form;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.zimzaza4.geyserutils.common.form.NpcDialogueButton;
import me.zimzaza4.geyserutils.common.packet.CustomPayloadPacket;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@Setter
public class NpcDialogueFormDataCustomPayloadPacket extends CustomPayloadPacket {

    private String formId;
    private String title;
    private String dialogue;
    private String skinData;
    private int bindEntity;
    private List<NpcDialogueButton> buttons;
    private String action;
    private boolean hasNextForm;

}
