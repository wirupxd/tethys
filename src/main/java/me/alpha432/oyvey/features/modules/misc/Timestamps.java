package me.alpha432.oyvey.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.TextUtil;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

//autor bepis

public class Timestamps extends Module {

    public Setting<TextUtil.Color> bracketColor = register(new Setting("BracketColor", TextUtil.Color.DARK_PURPLE));
    public Setting<TextUtil.Color> timeColor = register(new Setting("TimeColor", TextUtil.Color.LIGHT_PURPLE));
    public Setting<String> bracket = register(new Setting("Bracket", "["));
    public Setting<String> bracket2 = register(new Setting("Bracket2", "]"));
    private final Setting<Boolean> seconds = register(new Setting("Seconds", Boolean.valueOf(false), "seconds"));

    public Timestamps() {
        super("Timestamps", "Prefixes chat messages with the time", Category.MISC, true, false, false);
    }

    public String Timestamp() {
        String string = simpleDateFormat.format(date);
        String string2 = simpleDateFormat2.format(date);
        if (this.seconds.getValue().booleanValue()) {
            return TextUtil.coloredString(this.bracket.getPlannedValue(), this.bracketColor.getPlannedValue()) + TextUtil.coloredString(string2, this.timeColor.getPlannedValue()) + TextUtil.coloredString(this.bracket2.getPlannedValue(), this.bracketColor.getPlannedValue());
        } else {
            return TextUtil.coloredString(this.bracket.getPlannedValue(), this.bracketColor.getPlannedValue()) + TextUtil.coloredString(string, this.timeColor.getPlannedValue()) + TextUtil.coloredString(this.bracket2.getPlannedValue(), this.bracketColor.getPlannedValue());

        }
    }
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H:mm");
    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("H:mm:ss");



    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent clientChatReceivedEvent) {
        TextComponentString textComponentString = new TextComponentString(Timestamp() + ChatFormatting.RESET + " ");
        clientChatReceivedEvent.setMessage(textComponentString.appendSibling(clientChatReceivedEvent.getMessage()));
    }
}

