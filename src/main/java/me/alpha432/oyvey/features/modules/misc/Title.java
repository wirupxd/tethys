package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import org.lwjgl.opengl.Display;

public class Title extends Module {
    public final Setting<String> titel = register(new Setting("Title", OyVey.MODNAME + " " + OyVey.MODVER));
    public final Setting<Boolean> set = this.register(new Setting<Boolean>("Set Title", Boolean.FALSE));

    public Title() {
        super("Title", "changes the title", Category.MISC,true, false, false);
    }
    @Override
    public void onEnable() {
        Display.setTitle(this.titel.getValue());
        Command.sendMessage("Changed window title to " + this.titel.getValue());
        Title.super.enable();
    }
}