package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.util.MovementInput;

public class EntityControl
        extends Module {
    public Setting<Double> speed= this.register(new Setting<Double>("Speed", 1.0d, 0.1d, 5.0d));
    public static EntityControl INSTANCE;

    public EntityControl() {
        super("EntityControl", "sped", Module.Category.MOVEMENT, false, false, false);
        INSTANCE = this;
    }

    private static void speedEntity(Entity entity, Double speed) {
        if (entity instanceof EntityLlama) {
            entity.rotationYaw = EntityControl.mc.player.rotationYaw;
            ((EntityLlama)entity).rotationYawHead = EntityControl.mc.player.rotationYawHead;
        }
        MovementInput movementInput = EntityControl.mc.player.movementInput;
        double forward = movementInput.moveForward;
        double strafe = movementInput.moveStrafe;
        float yaw = EntityControl.mc.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            entity.motionX = 0.0;
            entity.motionZ = 0.0;
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            entity.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            entity.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
            if (entity instanceof EntityMinecart) {
                EntityMinecart em = (EntityMinecart)entity;
                em.setVelocity(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)), em.motionY, forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
            }
        }
    }

    @Override
    public void onUpdate() {
        try {
            if (EntityControl.mc.player.getRidingEntity() != null) {
                Entity theEntity = EntityControl.mc.player.getRidingEntity();
                EntityControl.speedEntity(theEntity, this.speed.getValue());
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}
