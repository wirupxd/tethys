package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Animations
        extends Module {
    private final Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.Old));
    private final Setting<Swing> swing = this.register(new Setting<Swing>("Swing", Swing.Mainhand));

    public Animations() {
        super("Animations", "Change animations.", Category.RENDER, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (Animations.nullCheck()) {
            System.out.println("fotr");
        }
        if (this.swing.getValue() == Swing.Offhand) {
            Animations.mc.player.swingingHand = EnumHand.OFF_HAND;
        }
        if (this.mode.getValue() == Mode.Old && (double)Animations.mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9) {
            Animations.mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
            Animations.mc.entityRenderer.itemRenderer.itemStackMainHand = Animations.mc.player.getHeldItemMainhand();
        }
    }


    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send send) {
        Object t = send.getPacket();
        if (t instanceof CPacketAnimation && this.swing.getValue() == Swing.Disable) {
            send.setCanceled(true);
        }
    }

    private static enum Swing {
        Mainhand,
        Offhand,
        Disable;

    }

    private static enum Mode {
        Normal,
        Old;

    }
}
