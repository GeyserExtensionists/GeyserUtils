package me.zimzaza4.geyserutils.geyser;

import org.cloudburstmc.protocol.bedrock.data.entity.EntityLinkData;
import org.cloudburstmc.protocol.bedrock.packet.SetEntityLinkPacket;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.entity.EntityDefinition;
import org.geysermc.geyser.entity.EntityDefinitions;
import org.geysermc.geyser.entity.type.Entity;
import org.geysermc.geyser.session.GeyserSession;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MountFix {

    public static void start() {
        // just keep send SetEntityLinkPacket to fix the mount bug
        // https://github.com/GeyserMC/Geyser/issues/3302
        // if the vehicle is too fast, the problem appear
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() -> {
                    for (GeyserSession session : GeyserImpl.getInstance().onlineConnections()) {
                        Entity v = session.getPlayerEntity().getVehicle();
                        if (v != null && v.getDefinition() == EntityDefinitions.ARMOR_STAND) {
                            long vehicleBedrockId = v.getGeyserId();
                            if (session.getPlayerEntity().getVehicle().getGeyserId() == vehicleBedrockId) {
                                // The Bedrock client, as of 1.19.51, dismounts on its end. The server may not agree with this.
                                // If the server doesn't agree with our dismount (sends a packet saying we dismounted),
                                // then remount the player.
                                SetEntityLinkPacket linkPacket = new SetEntityLinkPacket();
                                linkPacket.setEntityLink(new EntityLinkData(vehicleBedrockId, session.getPlayerEntity().getGeyserId(), EntityLinkData.Type.RIDER, true, false));
                                session.sendUpstreamPacket(linkPacket);
                            }
                        }
                    }
                }, 2000, 80, TimeUnit.MILLISECONDS);


    }

}
