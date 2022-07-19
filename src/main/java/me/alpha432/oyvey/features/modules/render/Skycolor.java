package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Skycolor
extends Module {
        private final Setting<Float> red = this.register(new Setting<Float>("Red", Float.valueOf(255.0f), Float.valueOf(0.0f), Float.valueOf(255.0f)));
        private final Setting<Float> green = this.register(new Setting<Float>("Green", Float.valueOf(255.0f), Float.valueOf(0.0f), Float.valueOf(255.0f)));
        private final Setting<Float> blue = this.register(new Setting<Float>("Blue", Float.valueOf(255.0f), Float.valueOf(0.0f), Float.valueOf(255.0f)));

    public Skycolor() {
        super("SkyColor", "rgb nebe", Category.RENDER, true, false, false);
    }

    @SubscribeEvent
        public void setFogColors(EntityViewRenderEvent.FogColors fogColors) {
            fogColors.setRed(this.red.getValue().floatValue() / 255.0f);
            fogColors.setGreen(this.green.getValue().floatValue() / 255.0f);
            fogColors.setBlue(this.blue.getValue().floatValue() / 255.0f);
        }

        @Override
        public void onEnable() {
            MinecraftForge.EVENT_BUS.register(this);
        }

        @Override
        public void onDisable() {
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }
