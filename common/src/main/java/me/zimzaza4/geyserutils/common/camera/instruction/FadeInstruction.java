package me.zimzaza4.geyserutils.common.camera.instruction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.zimzaza4.geyserutils.common.camera.data.Color;
import me.zimzaza4.geyserutils.common.camera.data.Time;
import org.jetbrains.annotations.Nullable;

@Builder
@AllArgsConstructor
@Getter
public class FadeInstruction implements Instruction {
    @Nullable
    private Color color;
    @Nullable
    private Time time;

    protected FadeInstruction() {
    }
}
