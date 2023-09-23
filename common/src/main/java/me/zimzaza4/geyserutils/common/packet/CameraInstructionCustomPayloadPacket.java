package me.zimzaza4.geyserutils.common.packet;

import lombok.Getter;
import lombok.Setter;
import me.zimzaza4.geyserutils.common.camera.instruction.Instruction;

@Setter
@Getter
public class CameraInstructionCustomPayloadPacket extends CustomPayloadPacket {
    private Instruction instruction;
}
