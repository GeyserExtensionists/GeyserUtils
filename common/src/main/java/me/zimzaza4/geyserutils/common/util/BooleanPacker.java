package me.zimzaza4.geyserutils.common.util;

import java.util.ArrayList;
import java.util.List;

public class BooleanPacker {
    private static final int MAX_BOOLEANS = 24;

    public static float packBooleans(List<Boolean> booleans) {
        int packedInt = 0;
        for (int i = 0; i < Math.min(booleans.size(), MAX_BOOLEANS); i++) {
            if (!booleans.get(i)) {
                packedInt |= (1 << i);
            }
        }
        return (float) packedInt;
    }

    public static List<Boolean> unpackBooleans(float packed) {
        int packedInt = (int) packed;
        List<Boolean> result = new ArrayList<>(MAX_BOOLEANS);
        for (int i = 0; i < MAX_BOOLEANS; i++) {
            result.add((packedInt & (1 << i)) == 0);
        }
        return result;
    }
}
