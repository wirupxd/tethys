package me.alpha432.oyvey.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

import java.util.HashMap;

public class PopCounter
        extends Module {
    public static HashMap<String, Integer> TotemPopContainer = new HashMap();
    private static PopCounter INSTANCE = new PopCounter();

    public PopCounter() {
        super("PopCounter", "Counts other players totem pops.", Module.Category.MISC, true, false, false);
        this.setInstance();
    }

    public static PopCounter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PopCounter();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        TotemPopContainer.clear();
        return;
    }

    @Override
    public void onDisable() {
        TotemPopContainer.clear();
    }

    public void onDeath(EntityPlayer player) {
        if (TotemPopContainer.containsKey(player.getName())) {
            int l_Count = TotemPopContainer.get(player.getName());
            TotemPopContainer.remove(player.getName());
            if (l_Count == 1) {
                TextComponentString text = new TextComponentString(OyVey.friendManager.isFriend(player.getName()) ? (OyVey.commandManager.getClientMessage() + " " + ChatFormatting.AQUA  + "\u00A7l" +  player.getName() + ChatFormatting.RESET + " popped and died") : (OyVey.commandManager.getClientMessage() + " " + ChatFormatting.RED  + "\u00A7l" +  player.getName() + ChatFormatting.RESET + " popped and died"));
                Module.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(text, 1);
            } else {
                TextComponentString text = new TextComponentString(OyVey.friendManager.isFriend(player.getName()) ? (OyVey.commandManager.getClientMessage() + " " + ChatFormatting.AQUA  + "\u00A7l" +  player.getName() + ChatFormatting.RESET + " popped " + ChatFormatting.GREEN  + "\u00A7l" +  l_Count + ChatFormatting.RESET + " and died") : (OyVey.commandManager.getClientMessage() + " " + ChatFormatting.RED + "\u00A7l" +  player.getName() + ChatFormatting.RESET + " popped "  + ChatFormatting.GREEN + "\u00A7l" + l_Count + ChatFormatting.RESET + " and died"));
                Module.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(text, 1);
            }
        }
    }

    public void onTotemPop(EntityPlayer player) {
        if (PopCounter.fullNullCheck()) {
            return;
        }
        if (PopCounter.mc.player.equals(player)) {
            return;
        }
        int l_Count = 1;
        if (TotemPopContainer.containsKey(player.getName())) {
            l_Count = TotemPopContainer.get(player.getName());
            TotemPopContainer.put(player.getName(), ++l_Count);
        } else {
            TotemPopContainer.put(player.getName(), l_Count);
        }
        if (l_Count == 1) {
            TextComponentString text = new TextComponentString(OyVey.friendManager.isFriend(player.getName()) ? (OyVey.commandManager.getClientMessage() + " " + ChatFormatting.AQUA + "\u00A7l" + player.getName() + ChatFormatting.RESET + " popped") : (OyVey.commandManager.getClientMessage() + " " + ChatFormatting.RED  + "\u00A7l" +  player.getName() + ChatFormatting.RESET + " popped"));
            Module.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(text, 1);

        } else {
            TextComponentString text = new TextComponentString(OyVey.friendManager.isFriend(player.getName()) ? (OyVey.commandManager.getClientMessage() + " " + ChatFormatting.AQUA  + "\u00A7l" +  player.getName() + ChatFormatting.RESET + " popped " + ChatFormatting.GREEN  +  "\u00A7l" +  l_Count) : (OyVey.commandManager.getClientMessage() + " " + ChatFormatting.RED  + "\u00A7l" +  player.getName()  +  ChatFormatting.RESET + " popped " + ChatFormatting.GREEN  +  "\u00A7l" +  l_Count));
            Module.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(text, 1);
        }
    }
}

