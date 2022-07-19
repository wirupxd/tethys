// Decompiled with: CFR 0.152
// Class Version: 8
package me.alpha432.oyvey.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Surround
        extends Module {
    public static boolean isPlacing = false;
    private final Setting<Integer> blocksPerTick = this.register(new Setting<Integer>("BlocksPerTick", 12, 1, 20));
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 0, 0, 250));
    private final Setting<Boolean> noGhost = this.register(new Setting<Boolean>("PacketPlace", true));
    private final Setting<Boolean> center = this.register(new Setting<Boolean>("TPCenter", false));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    private final Setting<Boolean> attack = this.register(new Setting<Boolean>("Attack", true));
    private final Setting<Boolean> blockChange = this.register(new Setting<Boolean>("BlockChange", true));
    private final Setting<Boolean> render = this.register(new Setting<Boolean>("Render", true));
    private final Setting<Integer> red = this.register(new Setting<Integer>("Red", 102, 0, 255));
    private final Setting<Integer> green = this.register(new Setting<Integer>("Green", 0, 0, 255));
    private final Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 255, 0, 255));
    private final Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 160, 0, 255));
    private final Timer timer = new Timer();
    private int isSafe;
    private BlockPos startPos;
    private boolean didPlace = false;
    private int lastHotbarSlot;
    private boolean isSneaking;
    private int placements = 0;
    private int obbySlot = -1;
    private boolean offHand = false;
    public static Surround INSTANCE;
    List<Vec3d> offsets = new ArrayList<Vec3d>();
    List<BlockPos> ghostPlace = new ArrayList<BlockPos>();
    private BlockPos renderPos;

    public static Surround getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Surround();
        }
        return INSTANCE;
    }

    @Override
    public void onToggle() {
        this.renderPos = null;
    }

    public Surround() {
        super("Surround", "Surrounds you with Obsidian", Module.Category.COMBAT, true, false, false);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        if (Surround.fullNullCheck()) {
            this.disable();
        }
        this.lastHotbarSlot = Surround.mc.player.inventory.currentItem;
        this.startPos = EntityUtil.getRoundedBlockPos(Surround.mc.player);
        if (this.center.getValue().booleanValue()) {
            OyVey.positionManager.setPositionPacket((double)this.startPos.getX() + 0.5, this.startPos.getY(), (double)this.startPos.getZ() + 0.5, true, true, true);
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST, receiveCanceled=true)
    public void onPacketReceive(PacketEvent.Receive event) {
        if (Surround.nullCheck()) {
            return;
        }
        if (event.getPacket() instanceof SPacketBlockChange && this.blockChange.getValue().booleanValue()) {
            SPacketBlockChange sPacketBlockChange = (SPacketBlockChange)event.getPacket();
            if (sPacketBlockChange.blockState.getBlock() == Blocks.AIR && Surround.mc.player.getDistance(sPacketBlockChange.getBlockPosition().getX(), sPacketBlockChange.getBlockPosition().getY(), sPacketBlockChange.getBlockPosition().getZ()) < 1.75) {
                mc.addScheduledTask(() -> this.doFeetPlace());
            }
        }
    }

    @Override
    public void onTick() {
        mc.addScheduledTask(() -> this.doFeetPlace());
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.render.getValue().booleanValue() && this.renderPos != null) {
            RenderUtil.drawBox(this.renderPos, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()));
        }
    }

    @Override
    public void onDisable() {
        if (Surround.nullCheck()) {
            return;
        }
        isPlacing = false;
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
    }

    @Override
    public String getDisplayInfo() {
        switch (this.isSafe) {
            case 0: {
                return ChatFormatting.RED + "Unsafe";
            }
            case 1: {
                return ChatFormatting.YELLOW + "Safe";
            }
        }
        return ChatFormatting.GREEN + "Safe";
    }

    private boolean isInterceptedByCrystal(BlockPos pos) {
        for (Entity entity : Surround.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal) || entity.equals(Surround.mc.player) || entity instanceof EntityItem || !new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) continue;
            return true;
        }
        return false;
    }

    private boolean isInterceptedByOther(BlockPos pos) {
        for (Entity entity : Surround.mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderCrystal || entity instanceof EntityItem || !new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) continue;
            return true;
        }
        return false;
    }

    private void doFeetPlace() {
        if (this.check()) {
            return;
        }
        this.renderPos = null;
        this.ghostPlace = new ArrayList<BlockPos>();
        this.offsets = new ArrayList<Vec3d>();
        if (this.isChestBelow()) {
            this.offsets.add(Surround.mc.player.getPositionVector().add(1.0, 0.0, 0.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(-1.0, 0.0, 0.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(0.0, 0.0, 1.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(0.0, 0.0, -1.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(1.0, 1.0, 0.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(-1.0, 1.0, 0.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(0.0, 1.0, 1.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(0.0, 1.0, -1.0));
        } else {
            this.offsets.add(Surround.mc.player.getPositionVector().add(0.0, -1.0, 0.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(1.0, -1.0, 0.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(-1.0, -1.0, 0.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(0.0, -1.0, 1.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(0.0, -1.0, -1.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(1.0, 0.0, 0.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(-1.0, 0.0, 0.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(0.0, 0.0, 1.0));
            this.offsets.add(Surround.mc.player.getPositionVector().add(0.0, 0.0, -1.0));
        }
        ArrayList<BlockPos> blockPosList = new ArrayList<BlockPos>();
        for (Vec3d vec3d : this.offsets) {
            BlockPos pos = new BlockPos(vec3d);
            if (Surround.mc.world.getBlockState(pos).getBlock() != Blocks.AIR) continue;
            blockPosList.add(pos);
        }
        if (blockPosList.isEmpty()) {
            return;
        }
        for (BlockPos blockPos : blockPosList) {
            if (this.placements > this.blocksPerTick.getValue()) {
                return;
            }
            if (this.isInterceptedByOther(blockPos)) continue;
            if (this.isInterceptedByCrystal(blockPos)) {
                if (!this.attack.getValue().booleanValue()) continue;
                EntityEnderCrystal crystal = null;
                for (Entity entity : Surround.mc.world.loadedEntityList) {
                    if (entity == null || (double)Surround.mc.player.getDistance(entity) > 2.4 || !(entity instanceof EntityEnderCrystal) || entity.isDead) continue;
                    crystal = (EntityEnderCrystal)entity;
                }
                if (crystal != null) {
                    if (this.rotate.getValue().booleanValue()) {
                        RotationUtil.faceEntity(crystal);
                    }
                    mc.getConnection().sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                    mc.getConnection().sendPacket(new CPacketUseEntity(crystal));
                }
            }
            this.renderPos = blockPos;
            this.placeBlock(blockPos);
            ++this.placements;
        }
    }

    public boolean isBurrow() {
        Block block = Surround.mc.world.getBlockState(new BlockPos(Surround.mc.player.getPositionVector().add(0.0, 0.2, 0.0))).getBlock();
        return block == Blocks.OBSIDIAN || block == Blocks.ENDER_CHEST;
    }

    private boolean isChestBelow() {
        return !this.isBurrow() && EntityUtil.isOnChest(Surround.mc.player);
    }

    private boolean check() {
        if (Surround.nullCheck()) {
            return true;
        }
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (obbySlot == -1 && eChestSot == -1) {
            this.toggle();
        }
        this.offHand = InventoryUtil.isBlock(Surround.mc.player.getHeldItemOffhand().getItem(), BlockObsidian.class);
        isPlacing = false;
        this.didPlace = false;
        this.placements = 0;
        this.obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int echestSlot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (this.isOff()) {
            return true;
        }
        if (this.obbySlot == -1 && !this.offHand && echestSlot == -1) {
            Command.sendMessage("<" + this.getDisplayName() + "> " + ChatFormatting.RED + "No blocks in hotbar disabling...");
            this.disable();
            return true;
        }
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        if (Surround.mc.player.inventory.currentItem != this.lastHotbarSlot && Surround.mc.player.inventory.currentItem != this.obbySlot && Surround.mc.player.inventory.currentItem != echestSlot) {
            this.lastHotbarSlot = Surround.mc.player.inventory.currentItem;
        }
        if (!this.startPos.equals(EntityUtil.getRoundedBlockPos(Surround.mc.player))) {
            this.disable();
            return true;
        }
        return !this.timer.passedMs(this.delay.getValue().intValue());
    }

    private void placeBlock(BlockPos pos) {
        try {
            int originalSlot = Surround.mc.player.inventory.currentItem;
            int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
            if (obbySlot == -1 && eChestSot == -1) {
                this.toggle();
            }
            isPlacing = true;
            Surround.mc.player.inventory.currentItem = obbySlot == -1 ? eChestSot : obbySlot;
            Surround.mc.playerController.updateController();
            this.isSneaking = this.placeBlock(pos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.rotate.getValue(), this.noGhost.getValue(), this.isSneaking);
            Surround.mc.player.inventory.currentItem = originalSlot;
            Surround.mc.playerController.updateController();
            this.didPlace = true;
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public boolean placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
        boolean sneaking = false;
        EnumFacing side = BlockUtil.getFirstFacing(pos);
        if (side == null) {
            side = EnumFacing.DOWN;
        }
        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();
        Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        Block neighbourBlock = BlockUtil.mc.world.getBlockState(neighbour).getBlock();
        if (!BlockUtil.mc.player.isSneaking() && (BlockUtil.blackList.contains(neighbourBlock) || BlockUtil.shulkerList.contains(neighbourBlock))) {
            BlockUtil.mc.player.connection.sendPacket(new CPacketEntityAction(BlockUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            BlockUtil.mc.player.setSneaking(true);
            sneaking = true;
        }
        if (rotate) {
            RotationUtil.faceVector(hitVec, true);
        }
        BlockUtil.rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        Surround.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        BlockUtil.mc.rightClickDelayTimer = 0;
        return sneaking || isSneaking;
    }
}
 