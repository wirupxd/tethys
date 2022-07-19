package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class MainMenu extends Module {
    public static Setting<Integer> logoy;
    public static Setting<Boolean> logo;
    public static Setting<String> text;
    public static MainMenu INSTANCE;

    public MainMenu() {
        super("Menu", "main menu", Category.CLIENT, true, false, false);
        this.logo = this.register(new Setting<Boolean>("Watermark", true));
        this.logoy = this.register(new Setting<Integer>("Watermark Y", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(12), n -> this.logo.getValue()));
        INSTANCE = this;
        setEnabled(true);
    }
    @Override
    public void onDisable() {
        super.onDisable();
        setEnabled(true);
    }
}
