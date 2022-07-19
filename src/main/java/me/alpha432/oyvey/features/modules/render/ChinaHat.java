package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.ColourUtils;
import me.alpha432.oyvey.util.Render2DUtils;

import java.awt.*;

public class ChinaHat
        extends Module {
    public Setting<Integer> red = this.register(new Setting<Integer>("Red", 0, 0, 255));
    public Setting<Integer> green = this.register(new Setting<Integer>("Green", 255, 0, 255));
    public Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 0, 0, 255));
    public Setting<Integer> red2 = this.register(new Setting<Integer>("Red2", 0, 0, 255));
    public Setting<Integer> green2 = this.register(new Setting<Integer>("Green2", 255, 0, 255));
    public Setting<Integer> blue2 = this.register(new Setting<Integer>("Blue2", 0, 0, 255));
    public Setting<Integer> points = this.register(new Setting<Integer>("Points", 12, 4, 64));
    public Setting<Boolean> firstP = this.register(new Setting<Boolean>("FirstPerson", false));

    public ChinaHat() {
        super("ChinaHat", "", Category.RENDER, true, false, false);
    }

    @Override
    public void onRender3D(Render3DEvent render3DEvent) {
        float f = 0.0f;
        if (ChinaHat.mc.gameSettings.thirdPersonView != 0 || this.firstP.getValue().booleanValue()) {
            for (int i = 0; i < 400; ++i) {
                f = ColourUtils.getGradientOffset(new Color(this.red2.getValue(), this.green2.getValue(), this.blue2.getValue(), 255), new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), 255), (double)Math.abs(System.currentTimeMillis() / 7L - (long)(i / 2)) / 120.0).getRGB();
                if (ChinaHat.mc.player.isElytraFlying()) {
                    Render2DUtils.drawHat(ChinaHat.mc.player, 0.009 + (double)i * 0.0014, render3DEvent.getPartialTicks(), this.points.getValue(), 2.0f, 1.1f - (float)i * 7.85E-4f - (ChinaHat.mc.player.isSneaking() ? 0.07f : 0.03f), (int)f);
                    continue;
                }
                if (ChinaHat.mc.player.isSneaking()) {
                    Render2DUtils.drawHat(ChinaHat.mc.player, 0.009 + (double)i * 0.0014, render3DEvent.getPartialTicks(), this.points.getValue(), 2.0f, 1.1f - (float)i * 7.85E-4f - (ChinaHat.mc.player.isSneaking() ? 0.07f : 0.03f), (int)f);
                    continue;
                }
                Render2DUtils.drawHat(ChinaHat.mc.player, 0.009 + (double)i * 0.0014, render3DEvent.getPartialTicks(), this.points.getValue(), 2.0f, 2.2f - (float)i * 7.85E-4f - (ChinaHat.mc.player.isSneaking() ? 0.07f : 0.03f), (int)f);
            }
        }
    }
}
