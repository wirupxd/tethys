package me.alpha432.oyvey.util;

import me.alpha432.oyvey.OyVey;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketHeldItemChange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class InventoryUtils
        implements Util {
    public static void switchToHotbarSlot(int n, boolean bl) {
        if (InventoryUtils.mc.player.inventory.currentItem == n || n < 0) {
            return;
        }
        if (bl) {
            InventoryUtils.mc.player.connection.sendPacket(new CPacketHeldItemChange(n));
            InventoryUtils.mc.playerController.updateController();
        } else {
            InventoryUtils.mc.player.connection.sendPacket(new CPacketHeldItemChange(n));
            InventoryUtils.mc.player.inventory.currentItem = n;
            InventoryUtils.mc.playerController.updateController();
        }
    }

    public static void switchToHotbarSlot(Class clazz, boolean bl) {
        int n = InventoryUtils.findHotbarBlock(clazz);
        if (n > -1) {
            InventoryUtils.switchToHotbarSlot(n, bl);
        }
    }

    public static boolean isNull(ItemStack itemStack) {
        return itemStack == null || itemStack.getItem() instanceof ItemAir;
    }

    public static int findHotbarBlock(Class clazz) {
        for (int i = 0; i < 9; ++i) {
            Block block;
            ItemStack itemStack = InventoryUtils.mc.player.inventory.getStackInSlot(i);
            if (itemStack == ItemStack.EMPTY) continue;
            if (clazz.isInstance(itemStack.getItem())) {
                return i;
            }
            if (!(itemStack.getItem() instanceof ItemBlock) || !clazz.isInstance(block = ((ItemBlock)((Object)itemStack.getItem())).getBlock())) continue;
            return i;
        }
        return -1;
    }

    public static int findHotbarBlock(Block block) {
        for (int i = 0; i < 9; ++i) {
            Block block2;
            ItemStack itemStack = InventoryUtils.mc.player.inventory.getStackInSlot(i);
            if (itemStack == ItemStack.EMPTY || !(itemStack.getItem() instanceof ItemBlock) || (block2 = ((ItemBlock)((Object)itemStack.getItem())).getBlock()) != block) continue;
            return i;
        }
        return -1;
    }

    public static int getItemHotbar(Item item) {
        for (int i = 0; i < 9; ++i) {
            Item item2 = InventoryUtils.mc.player.inventory.getStackInSlot(i).getItem();
            if (Item.getIdFromItem((Item)item2) != Item.getIdFromItem((Item)item)) continue;
            return i;
        }
        return -1;
    }

    public static int findStackInventory(Item item) {
        return InventoryUtils.findStackInventory(item, false);
    }

    public static int findStackInventory(Item item, boolean bl) {
        int n;
        for (n = bl ? 0 : 9; n < 36; ++n) {
            Item item2 = InventoryUtils.mc.player.inventory.getStackInSlot(n).getItem();
            if (Item.getIdFromItem((Item)item) != Item.getIdFromItem((Item)item2)) continue;
            return n + (n < 9 ? 36 : 0);
        }
        return -1;
    }

    public static int findItemInventorySlot(Item item, boolean bl) {
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(-1);
        for (Map.Entry<Integer, ItemStack> entry : InventoryUtils.getInventoryAndHotbarSlots().entrySet()) {
            if (entry.getValue().getItem() != item || entry.getKey() == 45 && !bl) continue;
            atomicInteger.set(entry.getKey());
            return atomicInteger.get();
        }
        return atomicInteger.get();
    }

    public static List<Integer> findEmptySlots(boolean bl) {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (Map.Entry<Integer, ItemStack> object : InventoryUtils.getInventoryAndHotbarSlots().entrySet()) {
            if (!object.getValue().isEmpty && object.getValue().getItem() != Items.AIR) continue;
            arrayList.add(object.getKey());
        }
        if (bl) {
            for (int i = 1; i < 5; ++i) {
                Slot slot = (Slot)InventoryUtils.mc.player.inventoryContainer.inventorySlots.get(i);
                ItemStack itemStack = slot.getStack();
                if (!itemStack.isEmpty() && itemStack.getItem() != Items.AIR) continue;
                arrayList.add(i);
            }
        }
        return arrayList;
    }

    public static int findInventoryBlock(Class clazz, boolean bl) {
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(-1);
        for (Map.Entry<Integer, ItemStack> entry : InventoryUtils.getInventoryAndHotbarSlots().entrySet()) {
            if (!InventoryUtils.isBlock(entry.getValue().getItem(), clazz) || entry.getKey() == 45 && !bl) continue;
            atomicInteger.set(entry.getKey());
            return atomicInteger.get();
        }
        return atomicInteger.get();
    }

    public static boolean isBlock(Item item, Class clazz) {
        if (item instanceof ItemBlock) {
            Block block = ((ItemBlock)((Object)item)).getBlock();
            return clazz.isInstance(block);
        }
        return false;
    }

    public static void confirmSlot(int n) {
        InventoryUtils.mc.player.connection.sendPacket(new CPacketHeldItemChange(n));
        InventoryUtils.mc.player.inventory.currentItem = n;
        InventoryUtils.mc.playerController.updateController();
    }

    public static Map<Integer, ItemStack> getInventoryAndHotbarSlots() {
        return InventoryUtils.getInventorySlots(9, 44);
    }

    private static Map<Integer, ItemStack> getInventorySlots(int n, int n2) {
        HashMap<Integer, ItemStack> hashMap = new HashMap<Integer, ItemStack>();
        for (int i = n; i <= n2; ++i) {
            hashMap.put(i, (ItemStack)InventoryUtils.mc.player.inventoryContainer.getInventory().get(i));
        }
        return hashMap;
    }

    public static boolean[] switchItem(boolean bl, int n, boolean bl2, Switch switch_, Class clazz) {
        boolean[] blArray = new boolean[]{bl2, false};
        switch (switch_) {
            case NORMAL: {
                if (!bl && !bl2) {
                    InventoryUtils.switchToHotbarSlot(InventoryUtils.findHotbarBlock(clazz), false);
                    blArray[0] = true;
                } else if (bl && bl2) {
                    InventoryUtils.switchToHotbarSlot(n, false);
                    blArray[0] = false;
                }
                blArray[1] = true;
                break;
            }
            case SILENT: {
                if (!bl && !bl2) {
                    InventoryUtils.switchToHotbarSlot(InventoryUtils.findHotbarBlock(clazz), true);
                    blArray[0] = true;
                } else if (bl && bl2) {
                    blArray[0] = false;
                    OyVey.inventoryManager.recoverSilent(n);
                }
                blArray[1] = true;
                break;
            }
            case NONE: {
                blArray[1] = bl || InventoryUtils.mc.player.inventory.currentItem == InventoryUtils.findHotbarBlock(clazz);
            }
        }
        return blArray;
    }

    public static boolean[] switchItemToItem(boolean bl, int n, boolean bl2, Switch switch_, Item item) {
        boolean[] blArray = new boolean[]{bl2, false};
        switch (switch_) {
            case NORMAL: {
                if (!bl && !bl2) {
                    InventoryUtils.switchToHotbarSlot(InventoryUtils.getItemHotbar(item), false);
                    blArray[0] = true;
                } else if (bl && bl2) {
                    InventoryUtils.switchToHotbarSlot(n, false);
                    blArray[0] = false;
                }
                blArray[1] = true;
                break;
            }
            case SILENT: {
                if (!bl && !bl2) {
                    InventoryUtils.switchToHotbarSlot(InventoryUtils.getItemHotbar(item), true);
                    blArray[0] = true;
                } else if (bl && bl2) {
                    blArray[0] = false;
                    OyVey.inventoryManager.recoverSilent(n);
                }
                blArray[1] = true;
                break;
            }
            case NONE: {
                blArray[1] = bl || InventoryUtils.mc.player.inventory.currentItem == InventoryUtils.getItemHotbar(item);
            }
        }
        return blArray;
    }

    public static boolean holdingItem(Class clazz) {
        boolean bl = false;
        ItemStack itemStack = InventoryUtils.mc.player.getHeldItemMainhand();
        bl = InventoryUtils.isInstanceOf(itemStack, clazz);
        if (!bl) {
            ItemStack itemStack2 = InventoryUtils.mc.player.getHeldItemOffhand();
            bl = InventoryUtils.isInstanceOf(itemStack, clazz);
        }
        return bl;
    }

    public static boolean isInstanceOf(ItemStack itemStack, Class clazz) {
        if (itemStack == null) {
            return false;
        }
        Item item = itemStack.getItem();
        if (clazz.isInstance(item)) {
            return true;
        }
        if (item instanceof ItemBlock) {
            Block block = Block.getBlockFromItem((Item)item);
            return clazz.isInstance(block);
        }
        return false;
    }

    public static int getEmptyXCarry() {
        for (int i = 1; i < 5; ++i) {
            Slot slot = (Slot)InventoryUtils.mc.player.inventoryContainer.inventorySlots.get(i);
            ItemStack itemStack = slot.getStack();
            if (!itemStack.isEmpty() && itemStack.getItem() != Items.AIR) continue;
            return i;
        }
        return -1;
    }

    public static boolean isSlotEmpty(int n) {
        Slot slot = (Slot)InventoryUtils.mc.player.inventoryContainer.inventorySlots.get(n);
        ItemStack itemStack = slot.getStack();
        return itemStack.isEmpty();
    }

    public static int convertHotbarToInv(int n) {
        return 36 + n;
    }

    public static boolean areStacksCompatible(ItemStack stack1, ItemStack stack2) {
        if (!stack1.getItem().equals(stack2.getItem())) {
            return false;
        }
        if (stack1.getItem() instanceof ItemBlock && stack2.getItem() instanceof ItemBlock) {
            Block block1 = ((ItemBlock) stack1.getItem()).getBlock();
            Block block2 = ((ItemBlock) stack2.getItem()).getBlock();
            if (!block1.material.equals(block2.material)) {
                return false;
            }
        }
        if (!stack1.getDisplayName().equals(stack2.getDisplayName())) {
            return false;
        }
        return stack1.getItemDamage() == stack2.getItemDamage();
    }

    public static EntityEquipmentSlot getEquipmentFromSlot(int n) {
        if (n == 5) {
            return EntityEquipmentSlot.HEAD;
        }
        if (n == 6) {
            return EntityEquipmentSlot.CHEST;
        }
        if (n == 7) {
            return EntityEquipmentSlot.LEGS;
        }
        return EntityEquipmentSlot.FEET;
    }

    public static int findArmorSlot(EntityEquipmentSlot entityEquipmentSlot, boolean bl) {
        int n = -1;
        float f = 0.0f;
        for (int i = 9; i < 45; ++i) {
            ItemArmor itemArmor;
            ItemStack itemStack = Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.AIR || !(itemStack.getItem() instanceof ItemArmor) || (itemArmor = (ItemArmor)((Object)itemStack.getItem())).getEquipmentSlot() != entityEquipmentSlot) continue;
            float f2 = itemArmor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel((Enchantment)Enchantments.PROTECTION, (ItemStack)itemStack);
            boolean bl2 = bl && EnchantmentHelper.hasBindingCurse((ItemStack)itemStack);
            boolean bl3 = bl2;
            if (!(f2 > f) || bl2) continue;
            f = f2;
            n = i;
        }
        return n;
    }

    public static int findArmorSlot(EntityEquipmentSlot entityEquipmentSlot, boolean bl, boolean bl2) {
        int n = InventoryUtils.findArmorSlot(entityEquipmentSlot, bl);
        if (n == -1 && bl2) {
            float f = 0.0f;
            for (int i = 1; i < 5; ++i) {
                ItemArmor itemArmor;
                Slot slot = (Slot)InventoryUtils.mc.player.inventoryContainer.inventorySlots.get(i);
                ItemStack itemStack = slot.getStack();
                if (itemStack.getItem() == Items.AIR || !(itemStack.getItem() instanceof ItemArmor) || (itemArmor = (ItemArmor)((Object)itemStack.getItem())).getEquipmentSlot() != entityEquipmentSlot) continue;
                float f2 = itemArmor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel((Enchantment)Enchantments.PROTECTION, (ItemStack)itemStack);
                boolean bl3 = bl && EnchantmentHelper.hasBindingCurse((ItemStack)itemStack);
                boolean bl4 = bl3;
                if (!(f2 > f) || bl3) continue;
                f = f2;
                n = i;
            }
        }
        return n;
    }

    public static int findItemInventorySlot(Item item, boolean bl, boolean bl2) {
        int n = InventoryUtils.findItemInventorySlot(item, bl);
        if (n == -1 && bl2) {
            for (int i = 1; i < 5; ++i) {
                Item item2;
                Slot slot = (Slot)InventoryUtils.mc.player.inventoryContainer.inventorySlots.get(i);
                ItemStack itemStack = slot.getStack();
                if (itemStack.getItem() == Items.AIR || (item2 = itemStack.getItem()) != item) continue;
                n = i;
            }
        }
        return n;
    }

    public static int findBlockSlotInventory(Class clazz, boolean bl, boolean bl2) {
        int n = InventoryUtils.findInventoryBlock(clazz, bl);
        if (n == -1 && bl2) {
            for (int i = 1; i < 5; ++i) {
                Block block;
                Slot slot = (Slot)InventoryUtils.mc.player.inventoryContainer.inventorySlots.get(i);
                ItemStack itemStack = slot.getStack();
                if (itemStack.getItem() == Items.AIR) continue;
                Item item = itemStack.getItem();
                if (clazz.isInstance(item)) {
                    n = i;
                    continue;
                }
                if (!(item instanceof ItemBlock) || !clazz.isInstance(block = ((ItemBlock)((Object)item)).getBlock())) continue;
                n = i;
            }
        }
        return n;
    }

    public static int getAmountOfItem(Item item) {
        int n = 0;
        for (ItemStackUtil itemStackUtil : InventoryUtils.getAllItems()) {
            if (itemStackUtil.itemStack == null || !itemStackUtil.itemStack.getItem().equals(item)) continue;
            n += itemStackUtil.itemStack.getCount();
        }
        return n;
    }

    public static boolean hasItem(Item item) {
        return InventoryUtils.getAmountOfItem(item) != 0;
    }

    public static ArrayList<ItemStackUtil> getAllItems() {
        ArrayList<ItemStackUtil> arrayList = new ArrayList<ItemStackUtil>();
        for (int i = 0; i < 36; ++i) {
            arrayList.add(new ItemStackUtil(InventoryUtils.getItemStack(i), i));
        }
        return arrayList;
    }

    public static ItemStack getItemStack(int n) {
        try {
            return InventoryUtils.mc.player.inventory.getStackInSlot(n);
        }
        catch (NullPointerException nullPointerException) {
            return null;
        }
    }

    public static int getSlot(Block block) {
        try {
            for (ItemStackUtil itemStackUtil : InventoryUtils.getAllItems()) {
                if (!Block.getBlockFromItem((Item)itemStackUtil.itemStack.getItem()).equals(block)) continue;
                return itemStackUtil.slotId;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return -1;
    }

    public static void switchItem1(int n, boolean bl) {
        int n2 = InventoryUtils.mc.player.inventory.currentItem;
        if (n < 9) {
            InventoryUtils.mc.player.inventory.currentItem = n;
        } else {
            int n3 = 8;
            for (int i = 0; i < 9; ++i) {
                if (InventoryUtils.getItemStack(i).getItem() != Items.AIR) continue;
                n3 = i;
                break;
            }
            InventoryUtils.clickSlot(n);
            if (bl) {
                InventoryUtils.sleep(200);
            }
            InventoryUtils.clickSlot(n3);
            if (bl) {
                InventoryUtils.sleep(200);
            }
            InventoryUtils.clickSlot(n);
            if (bl) {
                InventoryUtils.sleep(200);
            }
            InventoryUtils.mc.player.inventory.currentItem = n3;
            if (bl) {
                InventoryUtils.sleep(100);
            }
        }
        if (n2 != InventoryUtils.mc.player.inventory.currentItem) {
            InventoryUtils.mc.playerController.updateController();
        }
    }

    public static void clickSlot(int n) {
        if (n != -1) {
            try {
                InventoryUtils.mc.playerController.windowClick(InventoryUtils.mc.player.openContainer.windowId, InventoryUtils.getClickSlot(n), 0, ClickType.PICKUP, InventoryUtils.mc.player);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public static int getClickSlot(int n) {
        if (n == -1) {
            return n;
        }
        if (n < 9) {
            return n += 36;
        }
        if (n == 39) {
            n = 5;
        } else if (n == 38) {
            n = 6;
        } else if (n == 37) {
            n = 7;
        } else if (n == 36) {
            n = 8;
        } else if (n == 40) {
            n = 45;
        }
        return n;
    }

    public static void sleep(int n) {
        try {
            Thread.sleep(n);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static class ItemStackUtil {
        public ItemStack itemStack;
        public int slotId;

        public ItemStackUtil(ItemStack itemStack, int n) {
            this.itemStack = itemStack;
            this.slotId = n;
        }
    }

    public static class Task {
        private final int slot;
        private final boolean update;
        private final boolean quickClick;

        public Task() {
            this.update = true;
            this.slot = -1;
            this.quickClick = false;
        }

        public Task(int n) {
            this.slot = n;
            this.quickClick = false;
            this.update = false;
        }

        public Task(int n, boolean bl) {
            this.slot = n;
            this.quickClick = bl;
            this.update = false;
        }

        public void run() {
            if (this.update) {
                Util.mc.playerController.updateController();
            }
            if (this.slot != -1) {
                Util.mc.playerController.windowClick(0, this.slot, 0, this.quickClick ? ClickType.QUICK_MOVE : ClickType.PICKUP, Util.mc.player);
            }
        }

        public boolean isSwitching() {
            return !this.update;
        }
    }

    public static enum Switch {
        NORMAL,
        SILENT,
        NONE;

    }
}
