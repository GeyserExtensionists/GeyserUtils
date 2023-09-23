package me.zimzaza4.geyserutils.common.camera.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Data
public class Time {
    float fadeIn;
    float hold;
    float fadeOut;
}
