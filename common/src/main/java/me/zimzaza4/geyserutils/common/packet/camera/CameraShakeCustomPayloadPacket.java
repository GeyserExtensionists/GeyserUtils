package me.zimzaza4.geyserutils.common.packet.camera;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.zimzaza4.geyserutils.common.packet.CustomPayloadPacket;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CameraShakeCustomPayloadPacket extends CustomPayloadPacket {

    float intensity;
    float duration;
    int type;

}
