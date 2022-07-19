package me.alpha432.oyvey.features.command.commands;

import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.render.ToolTips;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;

public class PeekCommand
        extends Command {
    public PeekCommand() {
        super("peek", new String[]{"<player>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            ItemStack stack = PeekCommand.mc.player.getHeldItemMainhand();
            if (stack != null && stack.getItem() instanceof ItemShulkerBox) {
                ToolTips.displayInv(stack, null);
                Command.sendMessage("Peeked into this shulker");
            } else {
                Command.sendMessage("You need to hold a Shulker in your mainhand");
                return;
            }
        }
    }
}
