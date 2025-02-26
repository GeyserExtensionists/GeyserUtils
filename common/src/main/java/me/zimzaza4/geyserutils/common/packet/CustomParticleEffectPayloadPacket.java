package me.zimzaza4.geyserutils.common.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.zimzaza4.geyserutils.common.particle.CustomParticle;
import me.zimzaza4.geyserutils.common.util.Pos;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomParticleEffectPayloadPacket extends CustomPayloadPacket {
    private CustomParticle particle;
    private Pos pos;

}
