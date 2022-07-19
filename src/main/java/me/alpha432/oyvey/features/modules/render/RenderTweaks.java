package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.events.PerspectiveEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderTweaks extends Module {
    public static Setting<Double> aspect;
    public static Setting<Float> scale;
    public static Setting<Boolean> sneak;
    public static RenderTweaks INSTANCE;

    public RenderTweaks() {
        super("RenderTweaks", "various tweaks", Category.RENDER,true, false, false);
        this.aspect = this.register(new Setting<Double>("AspectRatio", (double)AspectRatio.mc.displayWidth / (double)AspectRatio.mc.displayHeight, 0.0, 3.0));
        this.scale = this.register(new Setting<Float>("CrystalSize", Float.valueOf(6.25f), Float.valueOf(0.01f), Float.valueOf(10.0f)));
        this.sneak = this.register(new Setting<Boolean>("Sneak", Boolean.valueOf(false)));
        INSTANCE = this;
    }
    public float getScale() {
        return this.scale.getValue().floatValue() / 10.0f;
    }
    @SubscribeEvent
    public void onPerspectiveEvent(PerspectiveEvent perspectiveEvent) {
        perspectiveEvent.setAspect(this.aspect.getValue().floatValue());
    }
}
