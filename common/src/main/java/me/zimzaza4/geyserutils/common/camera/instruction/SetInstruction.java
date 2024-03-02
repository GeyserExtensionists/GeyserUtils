package me.zimzaza4.geyserutils.common.camera.instruction;

import lombok.*;
import me.zimzaza4.geyserutils.common.camera.data.CameraPreset;
import me.zimzaza4.geyserutils.common.camera.data.Ease;
import me.zimzaza4.geyserutils.common.util.Pos;
import me.zimzaza4.geyserutils.common.camera.data.Rot;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@Builder
@Getter
public class SetInstruction implements Instruction {

    @Nullable
    private Ease ease;
    @Nullable
    private Pos pos;
    @Nullable
    private Rot rot;
    @Nullable
    private Pos facing;
    private CameraPreset preset;
    @Nullable
    private FadeInstruction fade;
    protected SetInstruction() {}
}
