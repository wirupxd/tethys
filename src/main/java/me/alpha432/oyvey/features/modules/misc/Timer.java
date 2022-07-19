package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.EntityUtil;

public class Timer extends Module {
    public Setting<Float> timerSpeed = register(new Setting<Float>("Speed", 1f, 0.1f, 25f));

    public Timer() {
        super("Timer", "fast n furious", Category.MISC, true, false, false);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        EntityUtil.resetTimer();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        EntityUtil.setTimer(timerSpeed.getValue());
    }
}