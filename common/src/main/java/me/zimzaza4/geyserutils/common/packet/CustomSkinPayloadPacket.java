package me.zimzaza4.geyserutils.common.packet;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class CustomSkinPayloadPacket extends CustomPayloadPacket {
    private int entityId;
    private String skinId;
}
