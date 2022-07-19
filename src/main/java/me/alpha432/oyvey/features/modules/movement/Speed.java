package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.MotionUtil;
import me.alpha432.oyvey.util.MovementUtil;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class Speed extends Module {

    public Setting<Modes> mode = register(new Setting("Mode", Modes.Strafe));
    public Setting<Boolean> usePotion = this.register(new Setting<Boolean>("PotionDetect", false));
    public Setting<Integer> potionSpeed = this.register(new Setting<Integer>("PotionDetect Speed", 1, 1, 10, n -> this.usePotion.getValue()));
    public Setting<Double> yPortSpeed= this.register(new Setting<Double>("YPort Speed", 6.0d, 1.0d, 15.0d, p -> this.mode.getValue() == Modes.YPort));


    public Speed() {
        super("Speed", "gotta go fast", Module.Category.MOVEMENT, true, false, false);
    }

    public void handleStrafeSpeed() {
        MovementUtil.strafe();

        if(MovementUtil.isMoving())
        {
            mc.player.setSprinting(true);
            if(mc.player.onGround)
                mc.player.jump();
        }
        else
        {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }
        return;
    }

    public void handleYPortSpeed() {
        if (!MotionUtil.isMoving(mc.player) || mc.player.isInWater() && mc.player.isInLava() || mc.player.collidedHorizontally) {
            return;
        }

        if (mc.player.onGround) {
            EntityUtil.setTimer(1.15f);
            mc.player.jump();
            MotionUtil.setSpeed(mc.player, MotionUtil.getBaseMoveSpeed() + (yPortSpeed.getValue() / 100));
        } else {
            mc.player.motionY = -1;
            EntityUtil.resetTimer();
        }
    }
    public void handleTPHop() {
        if(MovementUtil.isMoving()) {
            mc.player.setSprinting(true);
            if (mc.player.onGround) {
                EntityUtil.setTimer(10f);
                mc.player.motionY = +0.5f;
                MotionUtil.setSpeed(mc.player, MotionUtil.getBaseMoveSpeed() + (15d / 100));
            } else {
                EntityUtil.resetTimer();
            }
        }
    }

    public void handleStrafeFastSpeed() {
        MovementUtil.strafe();

        if(MovementUtil.isMoving())
        {
            EntityUtil.setTimer(1.1f);
            mc.player.setSprinting(true);
            if(mc.player.onGround)
                mc.player.jump();
        }
        else
        {
            EntityUtil.resetTimer();
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }
        return;
    }
    public void handleStiHop() {
        if (!MotionUtil.isMoving(mc.player) || mc.player.isInWater() && mc.player.isInLava()) {
            return;
        }

        if (mc.player.collidedHorizontally) {
            return;
        }


        if(MovementUtil.isMoving())
        {
            if (mc.player.onGround) {
                EntityUtil.setTimer(1.5f);
                mc.player.setSprinting(true);
                if (mc.player.onGround) {
                    mc.player.jump();
                    MotionUtil.setSpeed(mc.player, MotionUtil.getBaseMoveSpeed() + (10d / 100));
                }
            }
        }
        else
        {
            EntityUtil.resetTimer();
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }
        return;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        EntityUtil.resetTimer();
        if (this.usePotion.getValue().booleanValue()) {
            Speed.mc.player.removePotionEffect(MobEffects.SPEED);
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (usePotion.getValue()) {
            Speed.mc.player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 5, potionSpeed.getValue()-1));
        }
        if (this.usePotion.getValue().booleanValue() ==  Boolean.FALSE) {
            Speed.mc.player.removePotionEffect(MobEffects.SPEED);
        }

        switch (mode.getValue()){
            case Strafe:
                this.handleStrafeSpeed();
                break;
            case StrafeFast:
                //Speed.mc.player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 5210, potionSpeed.getValue()-1));
                this.handleStrafeFastSpeed();
                break;
            case YPort:
                this.handleYPortSpeed();
                break;
            case StiHop:
                this.handleStiHop();
                break;
            case TPHop:
                this.handleTPHop();
                break;
            case None:
                break;
        }



    }
}

enum Modes{
    Strafe,
    StrafeFast,
    YPort,
    TPHop,
    StiHop,
    None
}
