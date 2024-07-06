package me.zimzaza4.geyserutils.common.packet;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BundlePacket extends CustomPayloadPacket {

    private List<CustomPayloadPacket> packets = new ArrayList<>();

    public void addPacket(CustomPayloadPacket packet) {
        this.packets.add(packet);
    }

}
