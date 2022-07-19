package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.event.events.UpdateEvent;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoGG
        extends Module {
    private static EntityPlayer currentTarget;

    public AutoGG() {
        super("AutoGG", "excuse for log", Category.MISC, true, false, false);
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent updateEvent) {
        if (currentTarget != null && this.mc.player.getDistanceSq(currentTarget) < 100.0 && (AutoGG.currentTarget.isDead || currentTarget.getHealth() <= 0.0f)) {
            this.mc.player.sendChatMessage("https://doxbin.com/upload/" + currentTarget.getName());
            currentTarget = null;
        }
    }

    public static void setCurrentTarget(EntityPlayer entityPlayer) {
        currentTarget = entityPlayer;
    }
}

