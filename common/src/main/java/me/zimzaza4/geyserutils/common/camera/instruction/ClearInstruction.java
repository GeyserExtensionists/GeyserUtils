package me.zimzaza4.geyserutils.common.camera.instruction;

import lombok.Getter;

@Getter
public class ClearInstruction implements Instruction {
    private static final ClearInstruction INSTANCE = new ClearInstruction();
    private final int clear = 1;

    private ClearInstruction() {
    }

    public static ClearInstruction instance() {
        return INSTANCE;
    }


}
