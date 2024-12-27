package me.zimzaza4.geyserutils.common.packet.camera;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.zimzaza4.geyserutils.common.camera.instruction.Instruction;
import me.zimzaza4.geyserutils.common.packet.CustomPayloadPacket;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CameraInstructionCustomPayloadPacket extends CustomPayloadPacket {

    private Instruction instruction;

}
