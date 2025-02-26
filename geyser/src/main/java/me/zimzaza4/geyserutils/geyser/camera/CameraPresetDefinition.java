package me.zimzaza4.geyserutils.geyser.camera;

import lombok.AllArgsConstructor;
import org.cloudburstmc.protocol.common.NamedDefinition;


@AllArgsConstructor
public class CameraPresetDefinition implements NamedDefinition {
    private String identifier;
    private int id;


    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public int getRuntimeId() {
        return id;
    }

    @Override
    public String toString() {
        return "CameraPresetDefinition{" +
                "identifier='" + identifier + '\'' +
                ", id=" + id +
                '}';
    }
}
