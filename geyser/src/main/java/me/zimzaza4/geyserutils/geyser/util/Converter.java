package me.zimzaza4.geyserutils.geyser.util;

import java.util.Arrays;

import me.zimzaza4.geyserutils.common.camera.data.*;
import me.zimzaza4.geyserutils.common.camera.instruction.FadeInstruction;
import me.zimzaza4.geyserutils.common.camera.instruction.SetInstruction;
import me.zimzaza4.geyserutils.common.util.Pos;
import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraAudioListener;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraEase;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraFadeInstruction;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraSetInstruction;
import org.cloudburstmc.protocol.common.util.OptionalBoolean;
import org.geysermc.geyser.api.bedrock.camera.CameraEaseType;
import org.geysermc.geyser.api.bedrock.camera.CameraFade;
import org.geysermc.geyser.api.bedrock.camera.CameraPerspective;
import org.geysermc.geyser.api.bedrock.camera.CameraPosition;

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


    public static CameraFade serializeFadeInstruction(FadeInstruction instruction) {
        CameraFade.Builder builder = CameraFade.builder();
        if (instruction.getColor() != null) {
            builder.color(serializeColor(instruction.getColor()));
        }
        if (instruction.getTime() != null) {
            builder.fadeOutSeconds(instruction.getTime().fadeOut());
            builder.fadeInSeconds(instruction.getTime().fadeIn());
            builder.fadeHoldSeconds(instruction.getTime().hold());
        }
        return builder.build();

    }

    public static CameraPerspective serializeCameraPerspective(CameraPreset preset) {
        return Arrays.stream(CameraPerspective.values())
                .filter(value -> value.id().equals(preset.getIdentifier()))
                .findFirst()
                .orElse(CameraPerspective.FREE);
    }

    public static CameraPosition serializeSetInstruction(SetInstruction instruction) {
        CameraPosition.Builder builder = CameraPosition.builder();

        if (instruction.getEase() != null) {
            builder.easeType(CameraEaseType.values()[instruction.getEase().easeType()]);
            builder.easeSeconds(instruction.getEase().time());
        }
        if (instruction.getPos() != null) {
            builder.position(serializePos(instruction.getPos()));
        }
        if (instruction.getRot() != null) {
            builder.rotationX((int) instruction.getRot().x());
            builder.rotationY((int) instruction.getRot().y());

        }
        if (instruction.getFacing() != null) {
            builder.facingPosition(serializePos(instruction.getFacing()));
        }
        if (instruction.getFade() != null) {
            builder.cameraFade(serializeFadeInstruction(instruction.getFade()));
        }
        return builder.build();
    }

}
