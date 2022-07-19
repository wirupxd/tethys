package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

import java.awt.*;


public class EnchantColor
        extends Module {
    public final Setting<Boolean> sync = this.register(new Setting<Boolean>("Sync", true));
    public final Setting<Integer> red = this.register(new Setting<Object>("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), object -> this.sync.getValue() == false));
    public final Setting<Integer> green = this.register(new Setting<Object>("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), object -> this.sync.getValue() == false));
    public final Setting<Integer> blue = this.register(new Setting<Object>("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), object -> this.sync.getValue() == false));
    private static EnchantColor INSTANCE = new EnchantColor();

    public EnchantColor() {
        super("EnchantColor", "", Category.RENDER, true, false, false);
        INSTANCE = this;
    }

    public static EnchantColor getINSTANCE() {
        return INSTANCE;
    }

    public int getColor() {
         return new Color(this.sync.getValue() != false ? OyVey.colorManager.getColorAsInt() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()).getRGB()).getRGB();
    }
}

