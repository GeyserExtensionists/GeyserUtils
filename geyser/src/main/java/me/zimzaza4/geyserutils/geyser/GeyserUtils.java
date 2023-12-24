package me.zimzaza4.geyserutils.geyser;

import com.github.steveice10.mc.protocol.packet.common.clientbound.ClientboundCustomPayloadPacket;
import com.github.steveice10.mc.protocol.packet.common.serverbound.ServerboundCustomPayloadPacket;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.Getter;
import me.zimzaza4.geyserutils.common.camera.data.CameraPreset;
import me.zimzaza4.geyserutils.common.camera.instruction.ClearInstruction;
import me.zimzaza4.geyserutils.common.camera.instruction.FadeInstruction;
import me.zimzaza4.geyserutils.common.camera.instruction.SetInstruction;
import me.zimzaza4.geyserutils.common.channel.GeyserUtilsChannels;
import me.zimzaza4.geyserutils.common.form.element.NpcDialogueButton;
import me.zimzaza4.geyserutils.common.manager.PacketManager;
import me.zimzaza4.geyserutils.common.packet.*;
import me.zimzaza4.geyserutils.geyser.camera.CameraPresetDefinition;
import me.zimzaza4.geyserutils.geyser.camera.Converter;
import me.zimzaza4.geyserutils.geyser.form.NpcDialogueForm;
import me.zimzaza4.geyserutils.geyser.form.NpcDialogueForms;
import me.zimzaza4.geyserutils.geyser.form.element.Button;
import me.zimzaza4.geyserutils.geyser.translator.NPCFormResponseTranslator;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.packet.AnimateEntityPacket;
import org.cloudburstmc.protocol.bedrock.packet.CameraInstructionPacket;
import org.cloudburstmc.protocol.bedrock.packet.CameraPresetsPacket;
import org.cloudburstmc.protocol.bedrock.packet.NpcRequestPacket;
import org.cloudburstmc.protocol.common.DefinitionRegistry;
import org.cloudburstmc.protocol.common.NamedDefinition;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.api.bedrock.camera.CameraShake;
import org.geysermc.geyser.api.event.bedrock.SessionJoinEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserPostInitializeEvent;
import org.geysermc.geyser.api.extension.Extension;
import org.geysermc.geyser.entity.type.Entity;
import org.geysermc.geyser.registry.Registries;
import org.geysermc.geyser.session.GeyserSession;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GeyserUtils implements Extension {


    NbtMap CLEAR_INSTRUCTION_TAG = NbtMap.builder().putByte("clear", Integer.valueOf(1).byteValue()).build();
    @Getter
    public static PacketManager packetManager;

    @Subscribe
    public void onLoad(GeyserPostInitializeEvent event) {
        packetManager = new PacketManager();
        CameraPreset.load();
        Registries.BEDROCK_PACKET_TRANSLATORS.register(NpcRequestPacket.class, new NPCFormResponseTranslator());

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {

            for (GeyserSession session : GeyserImpl.getInstance().onlineConnections()) {
                sendCameraPresets(session);
            }

        }, 10, 10, TimeUnit.SECONDS);


    }
    @Subscribe
    public void onSessionJoin(SessionJoinEvent event) {
        if (event.connection() instanceof GeyserSession session) {
            sendCameraPresets(session);
            session.getDownstream().getSession().addListener(new SessionAdapter() {
                @Override
                public void packetReceived(Session tcpSession, Packet packet) {
                    if (packet instanceof ClientboundCustomPayloadPacket payloadPacket) {
                        if (payloadPacket.getChannel().equals(GeyserUtilsChannels.MAIN)) {;
                            CustomPayloadPacket customPacket = packetManager.decodePacket(payloadPacket.getData());
                            if (customPacket instanceof CameraShakeCustomPayloadPacket cameraShakePacket) {
                                event.connection().shakeCamera(cameraShakePacket.getIntensity(), cameraShakePacket.getDuration(), CameraShake.values()[cameraShakePacket.getType()]);
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
                                AnimateEntityPacket animateEntityPacket = new AnimateEntityPacket();
                                animateEntityPacket.setAnimation(animateEntityCustomPayloadPacket.getAnimation());
                                animateEntityPacket.setController(animateEntityCustomPayloadPacket.getController());
                                animateEntityPacket.setBlendOutTime(animateEntityCustomPayloadPacket.getBlendOutTime());
                                animateEntityPacket.setNextState(animateEntityCustomPayloadPacket.getNextState());
                                animateEntityPacket.setStopExpressionVersion(animateEntityCustomPayloadPacket.getStopExpressionVersion());
                                animateEntityPacket.setStopExpression(animateEntityCustomPayloadPacket.getStopExpression());
                                for (int id : animateEntityCustomPayloadPacket.getEntityJavaIds()) {
                                    Entity entity = session.getEntityCache().getEntityByJavaId(id);
                                    if (entity != null) {
                                        animateEntityPacket.getRuntimeEntityIds().add(entity.getGeyserId());
                                    }
                                }
                                session.sendUpstreamPacket(animateEntityPacket);
                            } else if (customPacket instanceof CameraInstructionCustomPayloadPacket cameraInstructionPacket) {
                                CameraInstructionPacket bedrockPacket = new CameraInstructionPacket();
                                if (cameraInstructionPacket.getInstruction() instanceof SetInstruction instruction) {
                                    bedrockPacket.setSetInstruction(Converter.serializeSetInstruction(instruction));
                                } else if (cameraInstructionPacket.getInstruction() instanceof FadeInstruction instruction) {
                                    bedrockPacket.setFadeInstruction(Converter.serializeFadeInstruction(instruction));
                                } else if (cameraInstructionPacket.getInstruction() instanceof ClearInstruction){
                                    bedrockPacket.setClear(true);
                                }
                                session.sendUpstreamPacket(bedrockPacket);
                            }
                        }
                    }
                }
            });

        }


    }

    public static void sendCameraPresets(GeyserSession session) {
        if (session.getUpstream().getCodecHelper().getCameraPresetDefinitions() == null) {

            session.getUpstream().getCodecHelper().setCameraPresetDefinitions(new DefinitionRegistry<>() {
                @Override
                public NamedDefinition getDefinition(int i) {
                    for (CameraPreset preset : CameraPreset.getPresets().values()) {
                        if (preset.getId() == i) {
                            return new CameraPresetDefinition(preset.getIdentifier(), i);
                        }
                    }

                    return null;
                }

                @Override
                public boolean isRegistered(NamedDefinition namedDefinition) {
                    return CameraPreset.getPreset(namedDefinition.getIdentifier()) != null;
                }
            });
        }
        CameraPresetsPacket pk = new CameraPresetsPacket();
        for (CameraPreset preset : CameraPreset.getPresets().values()) {
            pk.getPresets().add(Converter.serializeCameraPreset(preset));
        }

        session.sendUpstreamPacket(pk);
    }
}
