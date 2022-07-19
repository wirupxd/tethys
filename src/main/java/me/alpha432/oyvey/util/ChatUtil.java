package me.alpha432.oyvey.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class ChatUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final String prefix = "\u00a75[\u00a7Tethys\u00a75]\u00a7r ";

    public static void printMessage(String string) {
        if (ChatUtil.mc.player == null) {
            return;
        }
        ChatUtil.mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(prefix + string));
    }

    public static void printMessageWithID(String string, int n) {
        if (ChatUtil.mc.player == null) {
            return;
        }
        ChatUtil.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(prefix + string), n);
    }
}
