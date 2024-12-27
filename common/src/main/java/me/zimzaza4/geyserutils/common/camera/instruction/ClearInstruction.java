package me.zimzaza4.geyserutils.common.camera.instruction;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClearInstruction implements Instruction {

    private static final ClearInstruction INSTANCE = new ClearInstruction();
    private final int clear = 1;

    public static ClearInstruction instance() {
        return INSTANCE;
    }

}
