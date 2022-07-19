package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.player.Freecam;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.List;

public class FastFall extends Module {

    Setting<Double> speed = this.register(new Setting<Double>("Speed", 3.0, 0.1, 10.0));
    Setting<Double> height = this.register(new Setting<Double>("Height", 10.0, 0.1, 90.0));
    Setting<Boolean> noLag = this.register(new Setting<Boolean>("NoLag", true));
    List<Block> incelBlocks = Arrays.asList(Blocks.BED, Blocks.SLIME_BLOCK);


    public FastFall() {
        super("FastFall", "Go down", Module.Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (FastFall.fullNullCheck() || this.shouldReturn()) {
            return;
        }
        if (this.noLag.getValue().booleanValue() && OyVey.packetManager.caughtPlayerPosLook()) {
            return;
        }
        RayTraceResult trace = FastFall.mc.world.rayTraceBlocks(FastFall.mc.player.getPositionVector(), new Vec3d(FastFall.mc.player.posX, FastFall.mc.player.posY - this.height.getValue(), FastFall.mc.player.posZ), false, false, false);
        if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK && FastFall.mc.world.getBlockState(new BlockPos(FastFall.mc.player.posX, FastFall.mc.player.posY - 0.1, FastFall.mc.player.posZ)).getBlock() != this.incelBlocks) {
            FastFall.mc.player.motionY = -this.speed.getValue().doubleValue();
        }
    }

    boolean shouldReturn() {
        return FastFall.mc.player.isElytraFlying() || PlayerUtil.isClipping() || EntityUtil.isInLiquid() || FastFall.mc.player.isOnLadder() || FastFall.mc.player.capabilities.isFlying || FastFall.mc.player.motionY > 0.0 || FastFall.mc.gameSettings.keyBindJump.isKeyDown() || FastFall.mc.player.isEntityInsideOpaqueBlock() || FastFall.mc.player.noClip || !FastFall.mc.player.onGround || Freecam.getInstance().isEnabled();
    }
}

