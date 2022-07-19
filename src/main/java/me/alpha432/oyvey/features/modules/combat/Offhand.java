package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class Offhand
        extends Module {
    Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.Crystal));
    Setting<Boolean> forceClose = this.register(new Setting<Boolean>("ForceClose", false));
    Setting<Float> totemHealth = this.register(new Setting<Object>("TotemHealth", Float.valueOf(20.1f), Float.valueOf(0.1f), Float.valueOf(36.0f), v -> this.mode.getValue() != Mode.Totem));
    Setting<Float> totemHoleHealth = this.register(new Setting<Object>("TotemHoleHealth", Float.valueOf(13.0f), Float.valueOf(0.1f), Float.valueOf(36.0f), v -> this.mode.getValue() != Mode.Totem));
    Setting<Float> fallDistance = this.register(new Setting<Object>("FallDistance", Float.valueOf(40.0f), Float.valueOf(0.1f), Float.valueOf(90.0f), v -> this.mode.getValue() != Mode.Totem));
    Setting<Boolean> totemElytra = this.register(new Setting<Object>("TotemElytra", Boolean.valueOf(true), v -> this.mode.getValue() != Mode.Totem));
    Setting<Boolean> gapSword = this.register(new Setting<Object>("GapSword", Boolean.valueOf(true), v -> this.mode.getValue() != Mode.GApple));
    Setting<Boolean> noWaste = this.register(new Setting<Object>("NoWaste", Boolean.valueOf(true), v -> this.mode.getValue() != Mode.GApple && this.gapSword.getValue() != false));
    Setting<Boolean> forceGap = this.register(new Setting<Object>("ForceGap", Boolean.valueOf(true), v -> this.mode.getValue() != Mode.GApple && this.gapSword.getValue() != false));

    public Offhand() {
        super("AutoTotem", "Allows you to switch up your Offhand.", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (Offhand.fullNullCheck()) {
            return;
        }
        this.SwitchOffHandIfNeed();
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send e) {
        if (Offhand.fullNullCheck() || this.isDisabled()) {
            return;
        }
        if (this.noWaste.getValue().booleanValue() && this.gapSword.getValue().booleanValue() && this.mode.getValue() != Mode.GApple && e.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && InventoryUtil.heldItem(Items.DIAMOND_SWORD, InventoryUtil.Hand.Main) && Offhand.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            CPacketPlayerTryUseItemOnBlock p = (CPacketPlayerTryUseItemOnBlock)e.getPacket();
            if (p.hand == EnumHand.OFF_HAND) {
                e.setCanceled(true);
            }
        }
    }

    private void SwitchOffHandIfNeed() {
        Item i = this.getItemType();
        if (Offhand.mc.player.getHeldItemOffhand().getItem() != i) {
            int l_Slot;
            if (Offhand.mc.currentScreen instanceof GuiContainer || Offhand.mc.currentScreen instanceof GuiInventory) {
                if (this.forceClose.getValue().booleanValue()) {
                    Offhand.mc.player.closeScreen();
                } else {
                    return;
                }
            }
            if ((l_Slot = this.GetItemSlot(i)) != -1) {
                Offhand.mc.playerController.windowClick(Offhand.mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, Offhand.mc.player);
                Offhand.mc.playerController.windowClick(Offhand.mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, Offhand.mc.player);
                Offhand.mc.playerController.windowClick(Offhand.mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, Offhand.mc.player);
                Offhand.mc.playerController.updateController();
            }
        }
    }

    public Item getItemType() {
        switch (this.mode.getValue()) {
            case Totem: {
                if (this.gapSword.getValue().booleanValue() && InventoryUtil.heldItem(Items.DIAMOND_SWORD, InventoryUtil.Hand.Main) && Offhand.mc.gameSettings.keyBindUseItem.isKeyDown()) {
                    return Items.GOLDEN_APPLE;
                }
                return Items.TOTEM_OF_UNDYING;
            }
            case Crystal: {
                if (!(!(EntityUtil.getHealth(Offhand.mc.player) < this.totemHealth.getValue().floatValue()) || EntityUtil.isPlayerSafe(Offhand.mc.player) || !this.forceGap.getValue().booleanValue() || this.gapSword.getValue().booleanValue() && InventoryUtil.heldItem(Items.DIAMOND_SWORD, InventoryUtil.Hand.Main) && Offhand.mc.gameSettings.keyBindUseItem.isKeyDown())) {
                    return Items.TOTEM_OF_UNDYING;
                }
                if (EntityUtil.getHealth(Offhand.mc.player) < this.totemHoleHealth.getValue().floatValue() && EntityUtil.isPlayerSafe(Offhand.mc.player) && this.forceGap.getValue().booleanValue() && (!this.gapSword.getValue().booleanValue() || !InventoryUtil.heldItem(Items.DIAMOND_SWORD, InventoryUtil.Hand.Main) || !Offhand.mc.gameSettings.keyBindUseItem.isKeyDown())) {
                    return Items.TOTEM_OF_UNDYING;
                }
                if (!(!(Offhand.mc.player.motionY < 0.0) || !(Offhand.mc.player.fallDistance > this.fallDistance.getValue().floatValue()) || Offhand.mc.player.isElytraFlying() || !this.forceGap.getValue().booleanValue() || this.gapSword.getValue().booleanValue() && InventoryUtil.heldItem(Items.DIAMOND_SWORD, InventoryUtil.Hand.Main) && Offhand.mc.gameSettings.keyBindUseItem.isKeyDown())) {
                    return Items.TOTEM_OF_UNDYING;
                }
                if (this.totemElytra.getValue().booleanValue() && Offhand.mc.player.isElytraFlying() && this.forceGap.getValue().booleanValue() && (!this.gapSword.getValue().booleanValue() || !InventoryUtil.heldItem(Items.DIAMOND_SWORD, InventoryUtil.Hand.Main) || !Offhand.mc.gameSettings.keyBindUseItem.isKeyDown())) {
                    return Items.TOTEM_OF_UNDYING;
                }
                if (this.gapSword.getValue().booleanValue() && InventoryUtil.heldItem(Items.DIAMOND_SWORD, InventoryUtil.Hand.Main) && Offhand.mc.gameSettings.keyBindUseItem.isKeyDown()) {
                    return Items.GOLDEN_APPLE;
                }
                return Items.END_CRYSTAL;
            }
            case GApple: {
                if (EntityUtil.getHealth(Offhand.mc.player) < this.totemHealth.getValue().floatValue() && !EntityUtil.isPlayerSafe(Offhand.mc.player)) {
                    return Items.TOTEM_OF_UNDYING;
                }
                if (EntityUtil.getHealth(Offhand.mc.player) < this.totemHoleHealth.getValue().floatValue() && EntityUtil.isPlayerSafe(Offhand.mc.player)) {
                    return Items.TOTEM_OF_UNDYING;
                }
                if (Offhand.mc.player.motionY < 0.0 && Offhand.mc.player.fallDistance > this.fallDistance.getValue().floatValue() && !Offhand.mc.player.isElytraFlying()) {
                    return Items.TOTEM_OF_UNDYING;
                }
                if (this.totemElytra.getValue().booleanValue() && Offhand.mc.player.isElytraFlying()) {
                    return Items.TOTEM_OF_UNDYING;
                }
                return Items.GOLDEN_APPLE;
            }
        }
        return Items.TOTEM_OF_UNDYING;
    }

    public int GetItemSlot(Item item) {
        if (Offhand.mc.player == null) {
            return 0;
        }
        for (int i = 0; i < Offhand.mc.player.inventoryContainer.getInventory().size(); ++i) {
            ItemStack s;
            if (i == 0 || i == 5 || i == 6 || i == 7 || i == 8 || (s = (ItemStack)Offhand.mc.player.inventoryContainer.getInventory().get(i)).isEmpty() || s.getItem() != item) continue;
            return i;
        }
        return -1;
    }

    static enum Mode {
        Totem,
        Crystal,
        GApple;

    }
}