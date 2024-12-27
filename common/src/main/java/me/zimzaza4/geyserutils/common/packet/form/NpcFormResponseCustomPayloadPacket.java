package me.zimzaza4.geyserutils.common.packet.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.zimzaza4.geyserutils.common.packet.CustomPayloadPacket;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NpcFormResponseCustomPayloadPacket extends CustomPayloadPacket {

    private String formId;
    private int buttonId;

}
