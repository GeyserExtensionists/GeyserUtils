package me.zimzaza4.geyserutils.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public final class GeyserUtils extends Plugin {

    @Override
    public void onEnable() {
        ProxyServer.getInstance()
                .registerChannel("geyserutils:main");
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
