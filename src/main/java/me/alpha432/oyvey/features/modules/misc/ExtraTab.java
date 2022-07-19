package me.alpha432.oyvey.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;

public class ExtraTab
        extends Module {
    private static ExtraTab INSTANCE = new ExtraTab();
    public Setting<Boolean> pingDisplay = this.register(new Setting<Boolean>("Ping", true));
    public Setting<Boolean> coloredPing = this.register(new Setting<Boolean>("Colored", true));
    public Setting<Integer> size = this.register(new Setting<Integer>("Size", 250, 1, 1000));

    public ExtraTab() {
        super("ExtraTab", "Extends Tab.", Module.Category.MISC, false, false, false);
        this.setInstance();
    }

    public static String getPlayerName(NetworkPlayerInfo networkPlayerInfo) {
        String string;
        String string2 = string = networkPlayerInfo.getDisplayName() != null ? networkPlayerInfo.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName((Team)networkPlayerInfo.getPlayerTeam(), (String)networkPlayerInfo.getGameProfile().getName());
        if (ExtraTab.getINSTANCE().pingDisplay.getValue().booleanValue()) {
            if (ExtraTab.getINSTANCE().coloredPing.getValue().booleanValue()) {
                if (networkPlayerInfo.getResponseTime() <= 50) {
                    return string + ChatFormatting.GREEN + " " + (ExtraTab.getINSTANCE().pingDisplay.getValue() != false ? Integer.valueOf(networkPlayerInfo.getResponseTime()) : "");
                }
                if (networkPlayerInfo.getResponseTime() <= 100) {
                    return string + ChatFormatting.GOLD + " " + (ExtraTab.getINSTANCE().pingDisplay.getValue() != false ? Integer.valueOf(networkPlayerInfo.getResponseTime()) : "");
                }
                if (networkPlayerInfo.getResponseTime() <= 150) {
                    return string + ChatFormatting.RED + " " + (ExtraTab.getINSTANCE().pingDisplay.getValue() != false ? Integer.valueOf(networkPlayerInfo.getResponseTime()) : "");
                }
                if (networkPlayerInfo.getResponseTime() <= 1000) {
                    return string + ChatFormatting.DARK_RED + " " + (ExtraTab.getINSTANCE().pingDisplay.getValue() != false ? Integer.valueOf(networkPlayerInfo.getResponseTime()) : "");
                }
            } else {
                return string + ChatFormatting.GRAY + " " + (ExtraTab.getINSTANCE().pingDisplay.getValue() != false ? Integer.valueOf(networkPlayerInfo.getResponseTime()) : "");
            }
        }
        if (OyVey.friendManager.isFriend(string)) {
            return ChatFormatting.AQUA + string;
        }
        return string;
    }

    public static ExtraTab getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ExtraTab();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

