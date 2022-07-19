package me.alpha432.oyvey.features.modules.client;


import me.alpha432.oyvey.Discord;
import me.alpha432.oyvey.features.modules.Module;

public class RPC extends Module {

    public static RPC INSTANCE;

    public RPC() {
        super("Discord", "Discord rich presence", Module.Category.CLIENT, false, false, false);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        Discord.start();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Discord.stop();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        Discord.start();
    }
}
