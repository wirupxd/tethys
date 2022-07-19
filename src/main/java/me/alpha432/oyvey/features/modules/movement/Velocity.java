package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.event.events.PushEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity
        extends Module {
    public Setting<Boolean> noPush = this.register(new Setting<Boolean>("NoPush", true));
    public Setting<Float> horizontal = this.register(new Setting<Float>("Horizontal", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(100.0f)));
    public Setting<Float> vertical = this.register(new Setting<Float>("Vertical", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(100.0f)));
    public Setting<Boolean> explosions = this.register(new Setting<Boolean>("Explosions", true));
    public Setting<Boolean> bobbers = this.register(new Setting<Boolean>("Bobbers", true));
    public Setting<Boolean> water = this.register(new Setting<Boolean>("Water", false));
    public Setting<Boolean> blocks = this.register(new Setting<Boolean>("Blocks", false));
    public Setting<Boolean> ice = this.register(new Setting<Boolean>("Ice", false));
    private static Velocity INSTANCE = new Velocity();

    public Velocity() {
        super("Velocity", "Allows you to control your velocity", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static Velocity getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Velocity();
        }
        return INSTANCE;
    }


    @SubscribeEvent
    public void onPacketReceived(PacketEvent.Receive receive) {
        if (receive.getStage() == 0 && Velocity.mc.player != null) {
            if (receive.getPacket() instanceof SPacketEntityVelocity) {
                final SPacketEntityVelocity sPacketEntityVelocity = receive.getPacket();
                if (sPacketEntityVelocity.getEntityID() == Velocity.mc.player.entityId) {
                    if (this.horizontal.getValue() == 0.0f && this.vertical.getValue() == 0.0f) {
                        receive.setCanceled(true);
                        return;
                    }
                    final SPacketEntityVelocity sPacketEntityVelocity2 = sPacketEntityVelocity;
                    sPacketEntityVelocity2.motionX *= (int)(Object)this.horizontal.getValue();
                    final SPacketEntityVelocity sPacketEntityVelocity3 = sPacketEntityVelocity;
                    sPacketEntityVelocity3.motionY *= (int)(Object)this.vertical.getValue();
                    final SPacketEntityVelocity sPacketEntityVelocity4 = sPacketEntityVelocity;
                    sPacketEntityVelocity4.motionZ *= (int)(Object)this.horizontal.getValue();
                }
            }
            if (receive.getPacket() instanceof SPacketEntityStatus && this.bobbers.getValue()) {
                final SPacketEntityStatus sPacketEntityStatus = receive.getPacket();
                if (sPacketEntityStatus.getOpCode() == 31) {
                    final Entity entity = sPacketEntityStatus.getEntity((World)Velocity.mc.world);
                    if (entity instanceof EntityFishHook && ((EntityFishHook)entity).caughtEntity == Velocity.mc.player) {
                        receive.setCanceled(true);
                    }
                }
            }
            if (this.explosions.getValue() && receive.getPacket() instanceof SPacketExplosion) {
                if (this.horizontal.getValue() == 0.0f && this.vertical.getValue() == 0.0f) {
                    receive.setCanceled(true);
                    return;
                }
                final SPacketExplosion sPacketExplosion2;
                final SPacketExplosion sPacketExplosion = sPacketExplosion2 = receive.getPacket();
                sPacketExplosion2.motionX *= this.horizontal.getValue();
                final SPacketExplosion sPacketExplosion3 = sPacketExplosion;
                sPacketExplosion3.motionY *= this.vertical.getValue();
                final SPacketExplosion sPacketExplosion4 = sPacketExplosion;
                sPacketExplosion4.motionZ *= this.horizontal.getValue();
            }
        }
    }

    @SubscribeEvent
    public void onPush(PushEvent pushEvent) {
        if (pushEvent.getStage() == 0 && this.noPush.getValue().booleanValue() && pushEvent.entity.equals(Velocity.mc.player)) {
            if (this.horizontal.getValue().floatValue() == 0.0f && this.vertical.getValue().floatValue() == 0.0f) {
                pushEvent.setCanceled(true);
                return;
            }
            pushEvent.x = -pushEvent.x * (double)this.horizontal.getValue().floatValue();
            pushEvent.y = -pushEvent.y * (double)this.vertical.getValue().floatValue();
            pushEvent.z = -pushEvent.z * (double)this.horizontal.getValue().floatValue();
        } else if (pushEvent.getStage() == 1 && this.blocks.getValue().booleanValue()) {
            pushEvent.setCanceled(true);
        } else if (pushEvent.getStage() == 2 && this.water.getValue().booleanValue() && Velocity.mc.player != null && Velocity.mc.player.equals(pushEvent.entity)) {
            pushEvent.setCanceled(true);
        }
    }
}
