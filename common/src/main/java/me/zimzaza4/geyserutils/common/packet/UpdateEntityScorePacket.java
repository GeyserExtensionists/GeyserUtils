package me.zimzaza4.geyserutils.common.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateEntityScorePacket extends CustomPayloadPacket  {
    private int entityId;
    private String objective;
    private int score;
}
