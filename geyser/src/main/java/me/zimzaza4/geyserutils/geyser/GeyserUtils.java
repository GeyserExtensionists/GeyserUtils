package me.zimzaza4.geyserutils.geyser;

import com.github.steveice10.mc.protocol.packet.common.clientbound.ClientboundCustomPayloadPacket;
import com.github.steveice10.mc.protocol.packet.common.serverbound.ServerboundCustomPayloadPacket;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.PacketSendingEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.bytes.ByteArrays;
import lombok.Getter;
import me.zimzaza4.geyserutils.common.camera.data.CameraPreset;
import me.zimzaza4.geyserutils.common.camera.instruction.ClearInstruction;
import me.zimzaza4.geyserutils.common.camera.instruction.FadeInstruction;
import me.zimzaza4.geyserutils.common.camera.instruction.SetInstruction;
import me.zimzaza4.geyserutils.common.channel.GeyserUtilsChannels;
import me.zimzaza4.geyserutils.common.form.element.NpcDialogueButton;
import me.zimzaza4.geyserutils.common.manager.PacketManager;
import me.zimzaza4.geyserutils.common.packet.*;
import me.zimzaza4.geyserutils.geyser.form.NpcDialogueForm;
import me.zimzaza4.geyserutils.geyser.form.NpcDialogueForms;
import me.zimzaza4.geyserutils.geyser.form.element.Button;
import me.zimzaza4.geyserutils.geyser.translator.NPCFormResponseTranslator;
import me.zimzaza4.geyserutils.geyser.util.Converter;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.data.skin.ImageData;
import org.cloudburstmc.protocol.bedrock.data.skin.SerializedSkin;
import org.cloudburstmc.protocol.bedrock.packet.*;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.api.bedrock.camera.CameraShake;
import org.geysermc.geyser.api.event.bedrock.SessionLoginEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserPostInitializeEvent;
import org.geysermc.geyser.api.extension.Extension;
import org.geysermc.geyser.entity.type.Entity;
import org.geysermc.geyser.entity.type.player.PlayerEntity;
import org.geysermc.geyser.registry.Registries;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.skin.SkinProvider;
import org.geysermc.geyser.util.DimensionUtils;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GeyserUtils implements Extension {


    NbtMap CLEAR_INSTRUCTION_TAG = NbtMap.builder().putByte("clear", Integer.valueOf(1).byteValue()).build();
    @Getter
    public static PacketManager packetManager;

    @Getter
    public static Map<String, SkinProvider.SkinData> LOADED_SKIN_DATA = new HashMap<>();

    static final SkinProvider.Cape EMPTY_CAPE = new SkinProvider.Cape("", "no-cape", ByteArrays.EMPTY_ARRAY, -1, true);


    @Subscribe
    public void onLoad(GeyserPostInitializeEvent event) {
        packetManager = new PacketManager();
        CameraPreset.load();
        Registries.BEDROCK_PACKET_TRANSLATORS.register(NpcRequestPacket.class, new NPCFormResponseTranslator());
        loadSkins();
    }


    public void loadSkins() {
        LOADED_SKIN_DATA.clear();
        File folder = this.dataFolder().resolve("skins").toFile();
        if (!folder.exists()) {
            folder.mkdirs();
        }
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                File textureFile = null;
                File geometryFile = null;

                for (File folderFile : file.listFiles()) {
                    if (folderFile.getName().endsWith(".png")) {
                        textureFile = folderFile;
                    }
                    if (folderFile.getName().endsWith(".json")) {
                        geometryFile = folderFile;
                    }
                }

                try {
                    SkinProvider.Skin skin = new SkinProvider.Skin(null, file.getName(), Files.readAllBytes(textureFile.toPath()), -1, false, false);

                    String geoId = "";
                    JsonElement json = new JsonParser().parse(new FileReader(geometryFile));
                    for (JsonElement element : json.getAsJsonObject().get("minecraft:geometry").getAsJsonArray()) {
                        if (element.isJsonObject() && element.getAsJsonObject().has("description")) {
                            geoId = element.getAsJsonObject().get("description").getAsJsonObject().get("identifier").getAsString();
                            break;
                        }
                    }
                    String geoName = "{\"geometry\" :{\"default\" :\"" + geoId + "\"}}";
                    SkinProvider.SkinGeometry geometry = new SkinProvider.SkinGeometry(geoName, Files.readString(geometryFile.toPath()), false);
                    LOADED_SKIN_DATA.put(file.getName(), new SkinProvider.SkinData(skin, EMPTY_CAPE, geometry));
                    this.logger().info("Loaded skin: " + file.getName() + "| geo:" + geoName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Subscribe
    public void onSessionJoin(SessionLoginEvent event) {
        if (event.connection() instanceof GeyserSession session) {
            GeyserImpl.getInstance().getScheduledThread()
                            .schedule(() -> session.getDownstream().getSession().addListener(new SessionAdapter() {


                                @Override
                                public void packetSending(PacketSendingEvent event) {
                                    Packet packet = event.getPacket();
                                    if (packet instanceof ServerboundCustomPayloadPacket payloadPacket) {
                                        if (payloadPacket.getChannel().equals("minecraft:register")) {
                                            String channels = new String(payloadPacket.getData(), StandardCharsets.UTF_8);
                                            channels = channels + "\0" + GeyserUtilsChannels.MAIN;
                                            event.setPacket(new ServerboundCustomPayloadPacket("minecraft:register", channels.getBytes(StandardCharsets.UTF_8)));
                                        }
                                    }
                                }

                                @Override
                                public void packetReceived(Session tcpSession, Packet packet) {

                                    if (packet instanceof ClientboundCustomPayloadPacket payloadPacket) {
                                        if (payloadPacket.getChannel().equals(GeyserUtilsChannels.MAIN)) {
                                            CustomPayloadPacket customPacket = packetManager.decodePacket(payloadPacket.getData());
                                            if (customPacket instanceof CameraShakeCustomPayloadPacket cameraShakePacket) {
                                                event.connection().camera().shakeCamera(cameraShakePacket.getIntensity(), cameraShakePacket.getDuration(), CameraShake.values()[cameraShakePacket.getType()]);
                                            } else if (customPacket instanceof NpcDialogueFormDataCustomPayloadPacket formData) {

                                                if (formData.action().equals("CLOSE")) {
                                                    NpcDialogueForm openForm = NpcDialogueForms.getOpenNpcDialogueForms(session);
                                                    if (openForm != null) {
                                                        openForm.close(session);
                                                    }
                                                    return;
                                                }

                                                NpcDialogueForm form = new NpcDialogueForm();
                                                form.title(formData.title())
                                                        .dialogue(formData.dialogue())
                                                        .bindEntity(session.getEntityCache().getEntityByJavaId(formData.bindEntity()))
                                                        .hasNextForm(formData.hasNextForm());

                                                if (formData.skinData() != null) {
                                                    form.skinData(formData.skinData());
                                                }


                                                List<Button> buttons = new ArrayList<>();

                                                if (formData.buttons() != null) {

                                                    int i = 0;
                                                    for (NpcDialogueButton button : formData.buttons()) {


                                                        int finalI = i;
                                                        buttons.add(new Button(button.text(), button.commands(),
                                                                button.mode(), () -> {
                                                            if (button.mode() == NpcDialogueButton.ButtonMode.BUTTON_MODE) {
                                                                session.sendDownstreamPacket(new ServerboundCustomPayloadPacket(GeyserUtilsChannels.MAIN, packetManager.encodePacket(new NpcFormResponseCustomPayloadPacket(formData.formId(), finalI))));
                                                            }
                                                        }, button.hasNextForm()));
                                                        i++;
                                                    }
                                                }

                                                form.closeHandler(() -> session.sendDownstreamPacket(new ServerboundCustomPayloadPacket(GeyserUtilsChannels.MAIN, packetManager.encodePacket(new NpcFormResponseCustomPayloadPacket(formData.formId(), -1)))));
                                                form.buttons(buttons);

                                                form.createAndSend(session);

                                            } else if (customPacket instanceof AnimateEntityCustomPayloadPacket animateEntityCustomPayloadPacket) {
                                                AnimateEntityPacket animateEntityPacket = getAnimateEntityPacket(animateEntityCustomPayloadPacket);
                                                for (int id : animateEntityCustomPayloadPacket.getEntityJavaIds()) {
                                                    Entity entity = session.getEntityCache().getEntityByJavaId(id);
                                                    if (entity != null) {
                                                        animateEntityPacket.getRuntimeEntityIds().add(entity.getGeyserId());
                                                    }
                                                }
                                                session.sendUpstreamPacket(animateEntityPacket);
                                            } else if (customPacket instanceof CameraInstructionCustomPayloadPacket cameraInstructionPacket) {
                                                if (cameraInstructionPacket.getInstruction() instanceof SetInstruction instruction) {
                                                    session.camera().sendCameraPosition(Converter.serializeSetInstruction(instruction));
                                                    session.getCameraData().forceCameraPerspective(Converter.serializeCameraPerspective(instruction.getPreset()));

                                                } else if (cameraInstructionPacket.getInstruction() instanceof FadeInstruction instruction) {
                                                    session.camera().sendCameraFade(Converter.serializeFadeInstruction(instruction));
                                                } else if (cameraInstructionPacket.getInstruction() instanceof ClearInstruction) {
                                                    session.camera().clearCameraInstructions();
                                                }

                                            } else if (customPacket instanceof CustomParticleEffectPayloadPacket customParticleEffectPacket) {
                                                SpawnParticleEffectPacket spawnParticleEffectPacket = new SpawnParticleEffectPacket();
                                                spawnParticleEffectPacket.setDimensionId(DimensionUtils.javaToBedrock(session.getDimension()));
                                                spawnParticleEffectPacket.setPosition(Converter.serializePos(customParticleEffectPacket.getPos()));
                                                spawnParticleEffectPacket.setIdentifier(customParticleEffectPacket.getParticle().identifier());
                                                spawnParticleEffectPacket.setMolangVariablesJson(Optional.ofNullable(customParticleEffectPacket.getParticle().molangVariablesJson()));
                                                session.sendUpstreamPacket(spawnParticleEffectPacket);
                                            } else if (customPacket instanceof CustomSkinPayloadPacket customSkinPayloadPacket) {
                                                if (session.getEntityCache().getEntityByJavaId(customSkinPayloadPacket.getEntityId()) instanceof PlayerEntity player) {
                                                    SkinProvider.SkinData data = LOADED_SKIN_DATA.get(customSkinPayloadPacket.getSkinId());
                                                    if (data != null) {
                                                        sendSkinPacket(session, player, data);
                                                    }
                                                }
                                            } else if (customPacket instanceof CustomHitBoxPacket customHitBoxPacket) {
                                                Entity entity = (session.getEntityCache().getEntityByJavaId(customHitBoxPacket.getEntityId()));
                                                if (entity != null) {
                                                    entity.setBoundingBoxHeight(customHitBoxPacket.getHeight());
                                                    entity.setBoundingBoxWidth(customHitBoxPacket.getWidth());
                                                    entity.updateBedrockMetadata();
                                                }
                                            }
                                        }
                                    }
                                }

                                @NotNull
                                private static AnimateEntityPacket getAnimateEntityPacket(AnimateEntityCustomPayloadPacket animateEntityCustomPayloadPacket) {
                                    AnimateEntityPacket animateEntityPacket = new AnimateEntityPacket();
                                    animateEntityPacket.setAnimation(animateEntityCustomPayloadPacket.getAnimation());
                                    animateEntityPacket.setController(animateEntityCustomPayloadPacket.getController());
                                    animateEntityPacket.setBlendOutTime(animateEntityCustomPayloadPacket.getBlendOutTime());
                                    animateEntityPacket.setNextState(animateEntityCustomPayloadPacket.getNextState());
                                    animateEntityPacket.setStopExpressionVersion(animateEntityCustomPayloadPacket.getStopExpressionVersion());
                                    animateEntityPacket.setStopExpression(animateEntityCustomPayloadPacket.getStopExpression());
                                    return animateEntityPacket;
                                }
                            }), 100, TimeUnit.MILLISECONDS);
        }


    }

    public static void sendSkinPacket(GeyserSession session, PlayerEntity entity, SkinProvider.SkinData skinData) {
        SkinProvider.Skin skin = skinData.skin();
        SkinProvider.Cape cape = skinData.cape();
        SkinProvider.SkinGeometry geometry = skinData.geometry();
        if (entity.getUuid().equals(session.getPlayerEntity().getUuid())) {
            PlayerListPacket.Entry updatedEntry = buildEntryManually(session, entity.getUuid(), entity.getUsername(), entity.getGeyserId(), skin, cape, geometry);
            PlayerListPacket playerAddPacket = new PlayerListPacket();
            playerAddPacket.setAction(PlayerListPacket.Action.ADD);
            playerAddPacket.getEntries().add(updatedEntry);
            session.sendUpstreamPacket(playerAddPacket);
        } else {
            PlayerSkinPacket packet = new PlayerSkinPacket();
            packet.setUuid(entity.getUuid());
            packet.setOldSkinName("");
            String skinId = skin.getTextureUrl() + UUID.randomUUID().toString().replace("-", "");
            packet.setNewSkinName(skinId);
            packet.setSkin(getSkin(skinId, skin, cape, geometry));
            packet.setTrustedSkin(true);
            session.sendUpstreamPacket(packet);
        }


    }

    public static PlayerListPacket.Entry buildEntryManually(GeyserSession session, UUID uuid, String username, long geyserId, SkinProvider.Skin skin, SkinProvider.Cape cape, SkinProvider.SkinGeometry geometry) {
        SerializedSkin serializedSkin = getSkin(skin.getTextureUrl(), skin, cape, geometry);
        String xuid = "";
        GeyserSession playerSession = GeyserImpl.getInstance().connectionByUuid(uuid);
        if (playerSession != null) {
            xuid = playerSession.getAuthData().xuid();
        }

        PlayerListPacket.Entry entry;
        if (session.getPlayerEntity().getUuid().equals(uuid)) {
            entry = new PlayerListPacket.Entry(session.getAuthData().uuid());
        } else {
            entry = new PlayerListPacket.Entry(uuid);
        }

        entry.setName(username);
        entry.setEntityId(geyserId);
        entry.setSkin(serializedSkin);
        entry.setXuid(xuid);
        entry.setPlatformChatId("");
        entry.setTeacher(false);
        entry.setTrustedSkin(true);

        return entry;
    }

    private static SerializedSkin getSkin(String skinId, SkinProvider.Skin skin, SkinProvider.Cape cape, SkinProvider.SkinGeometry geometry) {
        try {
            return SerializedSkin.of(skinId, "", geometry.geometryName(), ImageData.from(ImageIO.read(new ByteArrayInputStream(skin.getSkinData()))), Collections.emptyList(), ImageData.of(cape.capeData()), geometry.geometryData(), "", true, false, false, cape.capeId(), skinId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

