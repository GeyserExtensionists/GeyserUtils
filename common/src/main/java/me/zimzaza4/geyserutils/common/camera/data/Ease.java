package me.zimzaza4.geyserutils.common.camera.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Accessors(fluent = true)
@Data
public class Ease {

    float time;
    int easeType;

    public Ease(float time, EaseType easeType) {
        this.time = time;
        this.easeType = easeType.getIndex();
    }

    public Ease easeType(EaseType easeType) {
        this.easeType = easeType.getIndex();
        return this;
    }
}
