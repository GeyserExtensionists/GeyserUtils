package me.zimzaza4.geyserutils.common.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CameraShakePacket extends Packet {

    float intensity;
    float duration;
    int type;

}
