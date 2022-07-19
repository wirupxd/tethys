// Decompiled with: CFR 0.152
// Class Version: 8
package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class CrystalScale
        extends Module {
    public Setting<Float> scale = this.register(new Setting<Float>("Scale", Float.valueOf(6.25f), Float.valueOf(0.01f), Float.valueOf(10.0f)));
    public static CrystalScale INSTANCE;

    public CrystalScale() {
        super("CrystalScale", "", Module.Category.RENDER, true, false, false);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
    }

    public static CrystalScale getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CrystalScale();
        }
        return INSTANCE;
    }

    public float getScale() {
        return this.scale.getValue().floatValue() / 10.0f;
    }
}
