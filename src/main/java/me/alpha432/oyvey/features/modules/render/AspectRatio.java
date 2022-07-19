package me.alpha432.oyvey.features.modules.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.alpha432.oyvey.event.events.PerspectiveEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class AspectRatio
        extends Module {
    private Setting<Double> aspect;

    public AspectRatio() {
        super("AspectRatio", "Changes the aspect ratio (p100 stretch res fortnite)", Category.RENDER, true, false, false);
        this.aspect = this.register(new Setting<Double>("Aspect", (double)AspectRatio.mc.displayWidth / (double)AspectRatio.mc.displayHeight, 0.0, 3.0));
    }

    @SubscribeEvent
    public void onPerspectiveEvent(PerspectiveEvent perspectiveEvent) {
        perspectiveEvent.setAspect(this.aspect.getValue().floatValue());
    }
}

