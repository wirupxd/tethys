package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class EChestFarmer
        extends Module {
    public EChestFarmer() {
        super("EChestFarmer", "Farms enderschests automatically for you.", Module.Category.PLAYER, false, false, false);
    }

    @Override
    public void onEnable() {
        BlockPos blockPos = EntityUtil.getPlayerPosWithEntity().add(0, -1, 0);
        if (EntityUtil.isSafe(EChestFarmer.mc.player)) {
            EChestFarmer.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString("Make sure to have echests in your hotbar and hold down Left Click."), 1);
            EChestFarmer.mc.player.rotationPitch = 90.0f;
            EChestFarmer.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(ItemPickaxe.class);
            EChestFarmer.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
            EChestFarmer.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
            this.disable();
        }
    }
}
