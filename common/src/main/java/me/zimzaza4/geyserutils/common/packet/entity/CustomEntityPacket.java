package me.zimzaza4.geyserutils.common.packet.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.zimzaza4.geyserutils.common.packet.CustomPayloadPacket;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomEntityPacket extends CustomPayloadPacket {

    private int entityId;
    private String identifier;

}
