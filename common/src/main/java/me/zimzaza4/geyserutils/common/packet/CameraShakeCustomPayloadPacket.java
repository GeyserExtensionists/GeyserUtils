package me.zimzaza4.geyserutils.common.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CameraShakeCustomPayloadPacket extends CustomPayloadPacket {

    float intensity;
    float duration;
    int type;

}
