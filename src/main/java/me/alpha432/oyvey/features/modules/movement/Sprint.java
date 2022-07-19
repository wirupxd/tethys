package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class Sprint
        extends Module {
    public Setting<Boolean> vanille = this.register(new Setting<Boolean>("Vanilla", false));

    public Sprint() {
        super("Sprint", "Auto sprint nn newfag", Module.Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (this.vanille.getValue().booleanValue()) {
            if (Sprint.mc.player.moveForward > 0.0f && !Sprint.mc.player.isSprinting()) {
                Sprint.mc.player.setSprinting(true);
            }
        } else if (Sprint.mc.player.motionZ > 0.0 || Sprint.mc.player.motionX > 0.0) {
            Sprint.mc.player.setSprinting(true);
        }
        return;
    }
}
