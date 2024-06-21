package me.zimzaza4.geyserutils.geyser.mappings;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unimi.dsi.fastutil.Pair;
import lombok.Getter;
import me.zimzaza4.geyserutils.geyser.GeyserUtils;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.item.GeyserCustomItemOptions;
import org.geysermc.geyser.registry.mappings.MappingsConfigReader;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.ParticleType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ItemParticlesMappings {

    Map<String, Map<Integer, String>> mappings;
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public void read(Path file) {
        if (!file.toFile().exists()) {
            mappings = new HashMap<>();
            mappings.put("minecraft:stone", Map.of(10001, "custom:test"));

            try (FileWriter writer = new FileWriter(file.toFile())){
                file.toFile().createNewFile();
                String json = GSON.toJson(mappings);
                writer.write(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            mappings = GSON.fromJson(new FileReader(file.toFile()), new TypeToken<Map<String, Map<Integer, String>>>(){}.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
