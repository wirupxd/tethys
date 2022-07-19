package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GappleFinder extends Module
{
    public GappleFinder() {
        super("GappleFinder", "finds gapples", Category.MISC, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (GappleFinder.mc.world.isRemote) {
            return;
        }
        final WorldClient world = GappleFinder.mc.world;
        if (!world.playerEntities.isEmpty()) {
            final EntityPlayer entityPlayer = world.playerEntities.get(0);
            for (final TileEntity tileEntity : world.loadedTileEntityList) {
                if (tileEntity instanceof TileEntityLockableLoot) {
                    final TileEntityLockableLoot tileEntityLockableLoot = (TileEntityLockableLoot)tileEntity;
                    if (tileEntityLockableLoot.getLootTable() == null) {
                        continue;
                    }
                    tileEntityLockableLoot.fillWithLoot(entityPlayer);
                    for (int i = 0; i < tileEntityLockableLoot.getSizeInventory(); ++i) {
                        final ItemStack stackInSlot = tileEntityLockableLoot.getStackInSlot(i);
                        if (stackInSlot.getItem() == Items.GOLDEN_APPLE && stackInSlot.getItemDamage() == 1) {
                            writeToFile("Dungeon Chest with ench gapple at: " + tileEntityLockableLoot.getPos().getX() + " " + tileEntityLockableLoot.getPos().getY() + " " + tileEntityLockableLoot.getPos().getZ());
                        }
                        if (stackInSlot.getItem() == Items.ENCHANTED_BOOK && EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stackInSlot) > 0) {
                            writeToFile("Dungeon Chest with Mending Book: " + tileEntityLockableLoot.getPos().getX() + " " + tileEntityLockableLoot.getPos().getY() + " " + tileEntityLockableLoot.getPos().getZ());
                        }
                    }
                }
            }
            for (final Entity entity : world.loadedEntityList) {
                if (entity instanceof EntityMinecartContainer) {
                    final EntityMinecartContainer entityMinecartContainer = (EntityMinecartContainer)entity;
                    if (entityMinecartContainer.getLootTable() == null) {
                        continue;
                    }
                    entityMinecartContainer.addLoot(entityPlayer);
                    for (int j = 0; j < entityMinecartContainer.itemHandler.getSlots(); ++j) {
                        final ItemStack stackInSlot2 = entityMinecartContainer.itemHandler.getStackInSlot(j);
                        if (stackInSlot2.getItem() == Items.GOLDEN_APPLE && stackInSlot2.getItemDamage() == 1) {
                            writeToFile("Minecart with ench gapple at: " + entityMinecartContainer.posX + " " + entityMinecartContainer.posY + " " + entityMinecartContainer.posZ);
                        }
                        if (stackInSlot2.getItem() == Items.ENCHANTED_BOOK && EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stackInSlot2) > 0) {
                            writeToFile("Minecart with Mending at: " + entityMinecartContainer.posX + " " + entityMinecartContainer.posY + " " + entityMinecartContainer.posZ);
                        }
                    }
                }
            }
        }
        return;
    }

    protected static void writeToFile(final String s) {
        try (final FileWriter fileWriter = new FileWriter("tethyscoords.txt", true);
             final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             final PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
            printWriter.println(s);
        }
        catch (final IOException ex) {}
    }
}
