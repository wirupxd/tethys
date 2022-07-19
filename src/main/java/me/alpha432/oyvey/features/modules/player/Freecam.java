// Decompiled with: CFR 0.152
// Class Version: 8
package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.event.events.PushEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.MovementUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Freecam
        extends Module {
    Setting<Float> speed = this.register(new Setting<Float>("Speed", Float.valueOf(0.2f), Float.valueOf(0.1f), Float.valueOf(5.0f)));
    Setting<Boolean> view = this.register(new Setting<Boolean>("3D", false));
    Setting<Boolean> packet = this.register(new Setting<Boolean>("Packet", true));
    Setting<Boolean> legit = this.register(new Setting<Boolean>("Legit", false));
    Setting<Boolean> noSuicide = this.register(new Setting<Boolean>("NoSuicide", true));
    Setting<Float> health = this.register(new Setting<Object>("Health", Float.valueOf(18.0f), Float.valueOf(0.1f), Float.valueOf(36.0f), v -> this.noSuicide.getValue()));
    AxisAlignedBB oldBoundingBox;
    public static EntityOtherPlayerMP entity;
    static Freecam INSTANCE;
    Vec3d position;
    Entity riding;
    float yaw;
    float pitch;

    public Freecam() {
        super("Freecam", "", Module.Category.PLAYER, true, false, false);
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static Freecam getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Freecam();
        }
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        if (!Freecam.fullNullCheck()) {
            this.oldBoundingBox = Freecam.mc.player.getEntityBoundingBox();
            Freecam.mc.player.setEntityBoundingBox(new AxisAlignedBB(Freecam.mc.player.posX, Freecam.mc.player.posY, Freecam.mc.player.posZ, Freecam.mc.player.posX, Freecam.mc.player.posY, Freecam.mc.player.posZ));
            if (Freecam.mc.player.getRidingEntity() != null) {
                this.riding = Freecam.mc.player.getRidingEntity();
                Freecam.mc.player.dismountRidingEntity();
            }
            entity = new EntityOtherPlayerMP(Freecam.mc.world, Freecam.mc.session.getProfile());
            entity.copyLocationAndAnglesFrom(Freecam.mc.player);
            Freecam.entity.rotationYaw = Freecam.mc.player.rotationYaw;
            Freecam.entity.rotationYawHead = Freecam.mc.player.rotationYawHead;
            Freecam.entity.inventory.copyInventory(Freecam.mc.player.inventory);
            Freecam.mc.world.addEntityToWorld(726804364, entity);
            this.position = Freecam.mc.player.getPositionVector();
            this.yaw = Freecam.mc.player.rotationYaw;
            this.pitch = Freecam.mc.player.rotationPitch;
            Freecam.mc.player.noClip = true;
        }
    }

    @Override
    public void onDisable() {
        if (!Freecam.fullNullCheck()) {
            Freecam.mc.player.setEntityBoundingBox(this.oldBoundingBox);
            if (this.riding != null) {
                Freecam.mc.player.startRiding(this.riding, true);
            }
            if (entity != null) {
                Freecam.mc.world.removeEntity(entity);
            }
            if (this.position != null) {
                Freecam.mc.player.setPosition(this.position.x, this.position.y, this.position.z);
            }
            Freecam.mc.player.rotationYaw = this.yaw;
            Freecam.mc.player.rotationPitch = this.pitch;
            Freecam.mc.player.noClip = false;
        }
    }

    @Override
    public void onUpdate() {
        if (Freecam.fullNullCheck()) {
            return;
        }
        if (this.noSuicide.getValue().booleanValue() && (EntityUtil.getHealth(Freecam.mc.player) <= this.health.getValue().floatValue() || EntityUtil.getHealth(entity) <= this.health.getValue().floatValue())) {
            this.disable();
            return;
        }
        Freecam.mc.player.noClip = true;
        Freecam.mc.player.setVelocity(0.0, 0.0, 0.0);
        Freecam.mc.player.jumpMovementFactor = this.speed.getValue().floatValue();
        double[] dir = MathUtil.directionSpeed(this.speed.getValue().floatValue());
        if (MovementUtil.isMoving()) {
            MovementUtil.setMotion(dir[0], Freecam.mc.player.motionY, dir[1]);
        } else {
            Freecam.mc.player.motionX = 0.0;
            Freecam.mc.player.motionZ = 0.0;
        }
        Freecam.mc.player.setSprinting(false);
        if (this.view.getValue().booleanValue() && !Freecam.mc.gameSettings.keyBindSneak.isKeyDown() && !Freecam.mc.gameSettings.keyBindJump.isKeyDown()) {
            Freecam.mc.player.motionY = (double)this.speed.getValue().floatValue() * -MathUtil.degToRad(Freecam.mc.player.rotationPitch) * (double)Freecam.mc.player.movementInput.moveForward;
        }
        if (Freecam.mc.gameSettings.keyBindJump.isKeyDown()) {
            Freecam.mc.player.motionY += (double)this.speed.getValue().floatValue();
        }
        if (Freecam.mc.gameSettings.keyBindSneak.isKeyDown()) {
            Freecam.mc.player.motionY -= (double)this.speed.getValue().floatValue();
        }
    }

    @Override
    public void onLogout() {
        if (this.isEnabled()) {
            this.disable();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send e) {
        if (this.isDisabled()) {
            return;
        }
        if (this.legit.getValue().booleanValue() && entity != null && e.getPacket() instanceof CPacketPlayer) {
            ((CPacketPlayer)e.getPacket()).x = Freecam.entity.posX;
            ((CPacketPlayer)e.getPacket()).y = Freecam.entity.posY;
            ((CPacketPlayer)e.getPacket()).z = Freecam.entity.posZ;
            return;
        }
        if (this.packet.getValue().booleanValue()) {
            if (e.getPacket() instanceof CPacketPlayer) {
                e.setCanceled(true);
            }
        } else if (!(e.getPacket() instanceof CPacketUseEntity || e.getPacket() instanceof CPacketPlayerTryUseItem || e.getPacket() instanceof CPacketPlayerTryUseItemOnBlock || e.getPacket() instanceof CPacketPlayer || e.getPacket() instanceof CPacketVehicleMove || e.getPacket() instanceof CPacketChatMessage || e.getPacket() instanceof CPacketKeepAlive)) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive e) {
        Entity riding;
        if (this.isDisabled()) {
            return;
        }
        if (e.getPacket() instanceof SPacketSetPassengers && (riding = Freecam.mc.world.getEntityByID(((SPacketSetPassengers)e.getPacket()).getEntityId())) != null && riding == this.riding) {
            this.riding = null;
        }
        if (e.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook p = (SPacketPlayerPosLook)e.getPacket();
            if (this.packet.getValue().booleanValue()) {
                if (entity != null) {
                    entity.setPositionAndRotation(p.getX(), p.getY(), p.getZ(), p.getYaw(), p.getPitch());
                }
                this.position = new Vec3d(p.getX(), p.getY(), p.getZ());
                Freecam.mc.player.connection.sendPacket(new CPacketConfirmTeleport(p.getTeleportId()));
                e.setCanceled(true);
            } else {
                e.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPush(PushEvent e) {
        if (e.getStage() == 1 && this.isEnabled()) {
            e.setCanceled(true);
        }
    }
}
