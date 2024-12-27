package me.zimzaza4.geyserutils.common.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.zimzaza4.geyserutils.common.packet.entity.EntityPropertyPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BundlePacket extends CustomPayloadPacket {

    private List<CustomPayloadPacket> packets = new ArrayList<>();

    public void addPacket(CustomPayloadPacket packet) {
        this.packets.add(packet);
    }

    public static <T> BundlePacket create(Map<String, T> bundle) {
        BundlePacket packet = new BundlePacket();
        bundle.forEach((identifier, value) -> {
            EntityPropertyPacket<T> propertyPacket = new EntityPropertyPacket<>();
            propertyPacket.setIdentifier(identifier);
            propertyPacket.setValue(value);
            packet.addPacket(propertyPacket);
        });
        return packet;
    }
}
