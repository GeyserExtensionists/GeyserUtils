package me.zimzaza4.geyserutils.common.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomEntityDataPacket extends CustomPayloadPacket {
    private int entityId;
    private Float height;
    private Float width;
    private Float scale;
    private Integer color;
    private Integer variant;

}
