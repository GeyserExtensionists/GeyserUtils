package me.zimzaza4.geyserutils.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "geyserutils",
        name = "GeyserUtils",
        version = "1.0.0",
        description = "nooo",
        authors = {"zimzaza4"}
)
public class GeyserUtils {

    private final ProxyServer server;

    @Inject
    public GeyserUtils(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

        server.getChannelRegistrar().register(MinecraftChannelIdentifier.from("geyserutils:main"));
    }
}
