package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class NoArmor extends Module {

    public static NoArmor INSTANCE;

    public NoArmor() {
        super("NoArmor", "noarmorrender", Category.RENDER, false, false, false);
        INSTANCE = this;
    }

    public final Setting<Boolean> helmet = register(new Setting<>("Helmet", false));
    public final Setting<Boolean> chestplate = register(new Setting<>("Chestplate", false));
    public final Setting<Boolean> thighHighs = register(new Setting<>("Leggings", false));
    public final Setting<Boolean> boots = register(new Setting<>("Boots", false));



}
