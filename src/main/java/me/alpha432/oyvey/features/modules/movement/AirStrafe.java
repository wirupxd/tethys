package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.MoveEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.mixin.mixins.ITimer;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.MovementUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AirStrafe
        extends Module {
    Setting<Boolean> useTimer = this.register(new Setting<Boolean>("UseTimer", true));
    Setting<Boolean> noLag = this.register(new Setting<Boolean>("NoLag", true));
    Setting<Boolean> noLiquid = this.register(new Setting<Boolean>("NoLiquid", true));
    Setting<Boolean> step = this.register(new Setting<Boolean>("Step", true));
    Setting<Float> height = this.register(new Setting<Object>("Height", Float.valueOf(2.0f), Float.valueOf(0.1f), Float.valueOf(2.0f), v -> this.step.getValue()));

    public AirStrafe() {
        super("AirStrafe", "lets u strafe in air", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onToggle() {
        if (!AirStrafe.fullNullCheck() && this.step.getValue().booleanValue()) {
            AirStrafe.mc.player.stepHeight = 0.6f;
        }
        if (this.useTimer.getValue().booleanValue() && ((ITimer)((Object)AirStrafe.mc.timer)).getTickLength() != 50.0f) {
            EntityUtil.resetTimer();
        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent e) {
        if (this.isDisabled() || AirStrafe.mc.player.isElytraFlying()) {
            return;
        }
        if (this.noLiquid.getValue().booleanValue() && EntityUtil.isInLiquid()) {
            return;
        }
        if (OyVey.moduleManager.isModuleEnabled("HoleSnap") || OyVey.moduleManager.isModuleEnabled("Freecam") || OyVey.moduleManager.isModuleEnabled("YPort") || OyVey.moduleManager.isModuleEnabled("Strafe")) {
            return;
        }
        if (this.noLag.getValue().booleanValue() && OyVey.packetManager.caughtPlayerPosLook()) {
            return;
        }
        if (this.step.getValue().booleanValue()) {
            MovementUtil.step(this.height.getValue().floatValue());
        }
        if (this.useTimer.getValue().booleanValue()) {
            EntityUtil.setTimer(1.0888f);
        }
        MovementUtil.strafe(e, MovementUtil.getSpeed());
    }
}
