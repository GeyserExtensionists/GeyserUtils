package me.zimzaza4.geyserutils.common.camera.data;

import lombok.Builder;
import lombok.Getter;
import me.zimzaza4.geyserutils.common.util.Pos;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.TreeMap;

@Getter
public class CameraPreset {
    private static final Map<String, CameraPreset> PRESETS = new TreeMap<>();
    public static CameraPreset FIRST_PERSON;
    public static CameraPreset FREE;
    public static CameraPreset THIRD_PERSON;
    public static CameraPreset THIRD_PERSON_FRONT;
    private String identifier;
    @Getter
    private String inheritFrom;
    @Getter
    @Nullable
    private Pos pos;
    @Getter
    @Nullable
    private Rot rot;
    @Getter
    private int id;
    @Builder
    public CameraPreset(String identifier, String inheritFrom, @Nullable Pos pos, @Nullable Rot rot) {
        this.identifier = identifier;
        this.inheritFrom = inheritFrom != null ? inheritFrom : "";
        this.pos = pos;
        this.rot = rot;
    }
    protected CameraPreset() {

    }

    public static Map<String, CameraPreset> getPresets() {
        return PRESETS;
    }

    public static CameraPreset getPreset(String identifier) {
        return getPresets().get(identifier);
    }

    public static void registerCameraPresets(CameraPreset... presets) {
        for (var preset : presets) {
            if (PRESETS.containsKey(preset.getIdentifier())) {
                continue;
            }
            PRESETS.put(preset.getIdentifier(), preset);
        }
        int id = 0;
        //重新分配id
        for (var preset : presets) {
            preset.id = id++;
        }
    }

    public static void load() {
        FIRST_PERSON = CameraPreset.builder()
                .identifier("minecraft:first_person")
                .build();
        FREE = CameraPreset.builder()
                .identifier("minecraft:free")
                .pos(new Pos(0, 0, 0))
                .rot(new Rot(0, 0))
                .build();
        THIRD_PERSON = CameraPreset.builder()
                .identifier("minecraft:third_person")
                .build();
        THIRD_PERSON_FRONT = CameraPreset.builder()
                .identifier("minecraft:third_person_front")
                .build();

        registerCameraPresets(FIRST_PERSON, FREE, THIRD_PERSON, THIRD_PERSON_FRONT);
    }
}
