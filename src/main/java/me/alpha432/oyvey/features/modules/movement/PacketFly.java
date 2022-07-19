package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PacketFly extends Module {

    public Setting<Integer> cooldown = register(new Setting("Cooldown", 0, 0, 10));
    public Setting<Float> fallSpeed = register(new Setting("FallSpeed", 0.05f, 0.0f, 10.0f));
    public Setting<Float> upSpeed = register(new Setting("Up Speed", 0.05f, 0.0f, 10.0f));
    private float counter;
    int j;

    public PacketFly() {
        super("PacketFly", "packet fly", Module.Category.MOVEMENT, true, false, false);
        this.counter = 0.0f;
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
        this.j = 0;
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (mc.player == null) {
            return;
        }
        if (event.phase == TickEvent.Phase.END) {
            if (!mc.player.isElytraFlying()) {
                if (this.counter < 1.0f) {
                    this.counter += this.cooldown.getValue();
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(PacketFly.mc.player.posX, PacketFly.mc.player.posY, PacketFly.mc.player.posZ, PacketFly.mc.player.onGround));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(PacketFly.mc.player.posX, PacketFly.mc.player.posY - 0.03, PacketFly.mc.player.posZ, PacketFly.mc.player.onGround));
                    mc.player.connection.sendPacket(new CPacketConfirmTeleport(++this.j));
                }
                else {
                    --this.counter;
                }
            }
        }
        else if (mc.gameSettings.keyBindJump.isPressed()) {
            mc.player.motionY = this.upSpeed.getValue();
        }
        else {
            mc.player.motionY = -this.fallSpeed.getValue();
        }
    }
}
