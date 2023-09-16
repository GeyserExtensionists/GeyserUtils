package me.zimzaza4.geyserutils.common.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NpcFormResponseCustomPayloadPacket extends CustomPayloadPacket {
    String formId;
    int buttonId;
}
