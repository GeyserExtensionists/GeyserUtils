package me.zimzaza4.geyserutils.geyser;

import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundCustomPayloadPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundCustomPayloadPacket;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.Getter;
import me.zimzaza4.geyserutils.common.channel.GeyserUtilsChannels;
import me.zimzaza4.geyserutils.common.form.element.NpcDialogueButton;
import me.zimzaza4.geyserutils.common.manager.PacketManager;
import me.zimzaza4.geyserutils.common.packet.*;
import me.zimzaza4.geyserutils.common.util.CustomPayloadPacketUtils;
import me.zimzaza4.geyserutils.geyser.form.NpcDialogueForm;
import me.zimzaza4.geyserutils.geyser.form.NpcDialogueForms;
import me.zimzaza4.geyserutils.geyser.form.element.Button;
import me.zimzaza4.geyserutils.geyser.translator.NPCFormResponseTranslator;
import org.cloudburstmc.protocol.bedrock.packet.AnimateEntityPacket;
import org.cloudburstmc.protocol.bedrock.packet.NpcRequestPacket;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.bedrock.camera.CameraShake;
import org.geysermc.geyser.api.event.bedrock.SessionJoinEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserPostInitializeEvent;
import org.geysermc.geyser.api.extension.Extension;
import org.geysermc.geyser.entity.type.Entity;
import org.geysermc.geyser.registry.Registries;
import org.geysermc.geyser.session.GeyserSession;

import java.util.ArrayList;
import java.util.List;

public class GeyserUtils implements Extension {

    @Getter
    public static PacketManager packetManager;

    @Subscribe
    public void onLoad(GeyserPostInitializeEvent event) {
        packetManager = new PacketManager();
        Registries.BEDROCK_PACKET_TRANSLATORS.register(NpcRequestPacket.class, new NPCFormResponseTranslator());
    }
    @Subscribe
    public void onSessionJoin(SessionJoinEvent event) {
        if (event.connection() instanceof GeyserSession session) {
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
                                        }
                                        ));
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
                            }
                        }
                    }
                }
            });
        }
    }
}
