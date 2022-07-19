// Decompiled with: CFR 0.152
// Class Version: 8
package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.event.events.BlockEvent;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.player.Speedmine;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.BlockUtil;
import me.alpha432.oyvey.util.RenderUtil;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class InstantMine
        extends Module {
    private BlockPos renderBlock;
    private BlockPos lastBlock;
    private boolean packetCancel = false;
    private final Timer breakTimer = new Timer();
    private EnumFacing direction;
    private boolean broke = false;
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 65, 0, 500));
    private final Setting<Boolean> picOnly = this.register(new Setting<Boolean>("PicOnly", true));

    public InstantMine() {
        super("InstantMine", "Instantly mine blocks", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onRender3D(Render3DEvent render3DEvent) {
        if (this.renderBlock != null) {
            Color color = new Color(93, 2, 198, 150);
            RenderUtil.drawBoxESP(this.renderBlock, color, false, color, 1.2f, true, true, 120, false);
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send send) {
        CPacketPlayerDigging cPacketPlayerDigging;
        if (send.getPacket() instanceof CPacketPlayerDigging && (cPacketPlayerDigging = (CPacketPlayerDigging)send.getPacket()).getAction() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK && this.packetCancel) {
            send.setCanceled(true);
        }
    }

    @Override
    public void onTick() {
        if (this.renderBlock != null && this.breakTimer.passedMs(this.delay.getValue().intValue())) {
            this.breakTimer.reset();
            if (this.picOnly.getValue().booleanValue() && InstantMine.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() != Items.DIAMOND_PICKAXE) {
                return;
            }
            InstantMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.renderBlock, this.direction));
            return;
        }
        try {
            InstantMine.mc.playerController.blockHitDelay = 0;
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @SubscribeEvent
    public void onBlockEvent(BlockEvent blockEvent) {
        if (Speedmine.fullNullCheck()) {
            return;
        }
        if (blockEvent.getStage() == 3 && Speedmine.mc.playerController.curBlockDamageMP > 0.1f) {
            Speedmine.mc.playerController.isHittingBlock = true;
        }
        if (blockEvent.getStage() == 4 && BlockUtil.canBreak(blockEvent.pos)) {
            Speedmine.mc.playerController.isHittingBlock = false;
            if (this.canBreak(blockEvent.pos)) {
                if (this.lastBlock == null || blockEvent.pos.getX() != this.lastBlock.getX() || blockEvent.pos.getY() != this.lastBlock.getY() || blockEvent.pos.getZ() != this.lastBlock.getZ()) {
                    this.packetCancel = false;
                    InstantMine.mc.player.swingArm(EnumHand.MAIN_HAND);
                    InstantMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, blockEvent.pos, blockEvent.facing));
                    this.packetCancel = true;
                } else {
                    this.packetCancel = true;
                }
                InstantMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockEvent.pos, blockEvent.facing));
                this.renderBlock = blockEvent.pos;
                this.lastBlock = blockEvent.pos;
                this.direction = blockEvent.facing;
                blockEvent.setCanceled(true);
            }
        }
    }

    private boolean canBreak(BlockPos blockPos) {
        IBlockState iBlockState = InstantMine.mc.world.getBlockState(blockPos);
        Block block = iBlockState.getBlock();
        return block.getBlockHardness(iBlockState, InstantMine.mc.world, blockPos) != -1.0f;
    }
}
