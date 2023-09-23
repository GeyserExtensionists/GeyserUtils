package me.zimzaza4.geyserutils.geyser.camera;

import me.zimzaza4.geyserutils.common.camera.data.*;
import me.zimzaza4.geyserutils.common.camera.instruction.FadeInstruction;
import me.zimzaza4.geyserutils.common.camera.instruction.SetInstruction;
import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraAudioListener;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraEase;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraFadeInstruction;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraSetInstruction;
import org.cloudburstmc.protocol.common.util.OptionalBoolean;

public class Converter {

    public static org.cloudburstmc.protocol.bedrock.data.camera.CameraPreset serializeCameraPreset(CameraPreset preset) {
        org.cloudburstmc.protocol.bedrock.data.camera.CameraPreset cbPreset = new org.cloudburstmc.protocol.bedrock.data.camera.CameraPreset();

        cbPreset.setIdentifier(preset.getIdentifier());

        cbPreset.setParentPreset(preset.getInheritFrom());

        cbPreset.setListener(CameraAudioListener.PLAYER);

        cbPreset.setPlayEffect(OptionalBoolean.of(true));

        if (preset.getPos() != null) {
            cbPreset.setPos(serializePos(preset.getPos()));
        }
        if (preset.getRot() != null) {
            cbPreset.setPitch(preset.getRot().x());
            cbPreset.setYaw(preset.getRot().y());
        }


        return cbPreset;
    }

    public static java.awt.Color serializeColor(Color color) {
        return new java.awt.Color(color.r(), color.g(), color.b());
    }

    public static CameraSetInstruction.EaseData serializeEase(Ease ease) {
        return new CameraSetInstruction.EaseData(CameraEase.values()[ease.easeType()], ease.time());
    }

    public static CameraFadeInstruction.TimeData serializeTime(Time time) {
        return new CameraFadeInstruction.TimeData(time.fadeIn(), time.hold(), time.fadeOut());
    }


    public static Vector3f serializePos(Pos pos) {
        return Vector3f.from(pos.x(), pos.y(), pos.z());
    }

    public static Vector2f serializeRot(Rot rot) {
        return Vector2f.from(rot.x(), rot.y());
    }


    public static CameraFadeInstruction serializeFadeInstruction(FadeInstruction instruction) {
        CameraFadeInstruction cbInstruction = new CameraFadeInstruction();

        if (instruction.getColor() != null) {
            cbInstruction.setColor(serializeColor(instruction.getColor()));
        }
        if (instruction.getTime() != null) {
            cbInstruction.setTimeData(serializeTime(instruction.getTime()));
        }

        return cbInstruction;

    }

    public static CameraSetInstruction serializeSetInstruction(SetInstruction instruction) {

        CameraSetInstruction cbInstruction = new CameraSetInstruction();

        if (instruction.getEase() != null) {
            cbInstruction.setEase(serializeEase(instruction.getEase()));
        }
        if (instruction.getPos() != null) {
            cbInstruction.setPos(serializePos(instruction.getPos()));
        }
        if (instruction.getRot() != null) {
            cbInstruction.setRot(serializeRot(instruction.getRot()));
        }
        if (instruction.getFacing() != null) {
            cbInstruction.setFacing(serializePos(instruction.getFacing()));
        }


        cbInstruction.setDefaultPreset(OptionalBoolean.of(false));
        cbInstruction.setPreset(new CameraPresetDefinition(instruction.getPreset().getIdentifier(), instruction.getPreset().getId()));
        return cbInstruction;

    }

}
