package me.zimzaza4.geyserutils.common.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
