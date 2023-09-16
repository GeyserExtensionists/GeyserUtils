package me.zimzaza4.geyserutils.common.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CameraShakeCustomPayloadPacket extends CustomPayloadPacket {

    float intensity;
    float duration;
    int type;

}
