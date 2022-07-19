package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;

public class NoInterp extends Module {
    public static NoInterp INSTANCE;

    public NoInterp() {
        super("NoInterp", "changes the title", Category.MISC,true, false, false);
        INSTANCE = this;
    }
}
