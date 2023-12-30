package me.zimzaza4.geyserutils.common.particle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.zimzaza4.geyserutils.common.util.Pos;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Getter
@Setter
public class CustomParticle {
    private Pos position;
    private String identifier;
    @Nullable
    private String molangVariablesJson;
}
