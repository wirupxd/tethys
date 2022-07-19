package me.alpha432.oyvey.util;

import me.alpha432.oyvey.event.events.MoveEvent;
import net.minecraft.client.Minecraft;

public class MovementUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static float getSpeed()
    {
        return (float) Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
    }


    public static void strafe()
    {
        strafe(getSpeed());
    }

    public static boolean isMoving()
    {
        return mc.player != null && (mc.player.movementInput.moveForward != 0F || mc.player.movementInput.moveStrafe != 0F);
    }



    public static void strafe(final float speed)
    {
        if(!isMoving())
            return;

        final double yaw = getDirection();

        mc.player.motionX = -Math.sin(yaw) * speed;
        mc.player.motionZ = Math.cos(yaw) * speed;
    }

    public static void strafe(MoveEvent e, final double speed)
    {
        if(!isMoving())
            return;

        final double yaw = getDirection();

        mc.player.motionX = -Math.sin(yaw) * speed;
        mc.player.motionZ = Math.cos(yaw) * speed;
    }

    public static void forward(final double length)
    {
        final double yaw = Math.toRadians(mc.player.rotationYaw);
        mc.player.setPosition(mc.player.posX + (-Math.sin(yaw) * length), mc.player.posY, mc.player.posZ + (Math.cos(yaw) * length));
    }

    public static double getDirection()
    {
        float rotationYaw = mc.player.rotationYaw;

        if(mc.player.moveForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;

        if(mc.player.moveForward < 0F)
            forward = -0.5F;
        else if(mc.player.moveForward > 0F)
            forward = 0.5F;

        if(mc.player.moveStrafing > 0F)
            rotationYaw -= 90F * forward;

        if(mc.player.moveStrafing < 0F)
            rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }

    public static double getDirectionWithYaw(float yaw)
    {
        float rotationYaw = yaw;

        if(mc.player.moveForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        if(mc.player.moveForward < 0F)
            forward = -0.5F;
        else if(mc.player.moveForward > 0F)
            forward = 0.5F;

        if(mc.player.moveStrafing > 0F)
            rotationYaw -= 90F * forward;

        if(mc.player.moveStrafing < 0F)
            rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }

    public static void move(final float yaw, final float multiplier, final float up)
    {
        final double moveX = -Math.sin(Math.toRadians(yaw)) * multiplier;
        final double moveZ = Math.cos(Math.toRadians(yaw)) * multiplier;
        mc.player.motionX = moveX;
        mc.player.motionY = up;
        mc.player.motionZ = moveZ;
    }

    public static void move(final float yaw, final float multiplier)
    {
        final double moveX = -Math.sin(Math.toRadians(yaw)) * multiplier;
        final double moveZ = Math.cos(Math.toRadians(yaw)) * multiplier;
        mc.player.motionX = moveX;
        mc.player.motionZ = moveZ;
    }
    public static void setMotion(double x, double y, double z) {
        if (MovementUtil.mc.player != null) {
            if (MovementUtil.mc.player.isRiding()) {
                MovementUtil.mc.player.ridingEntity.motionX = x;
                MovementUtil.mc.player.ridingEntity.motionY = y;
                MovementUtil.mc.player.ridingEntity.motionZ = x;
            } else {
                MovementUtil.mc.player.motionX = x;
                MovementUtil.mc.player.motionY = y;
                MovementUtil.mc.player.motionZ = z;
            }
        }
    }
    public static void step(float height) {
        if (!MovementUtil.mc.player.collidedVertically || (double)MovementUtil.mc.player.fallDistance > 0.1 || MovementUtil.mc.player.isOnLadder() || !MovementUtil.mc.player.onGround) {
            return;
        }
        MovementUtil.mc.player.stepHeight = height;
    }
}
