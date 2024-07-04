package me.zimzaza4.geyserutils.common.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EntityPropertyRegisterPacket extends CustomPayloadPacket {
    private int entityId;

    private String identifier;
    private Class<?> type;

}
