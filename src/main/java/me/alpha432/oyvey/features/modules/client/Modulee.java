package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class Modulee extends Module{
    private static Modulee INSTANCE = new Modulee();
    private static Modulee instance;
    public Setting<Boolean> notifyToggles = this.register(new Setting("ChatNotify", Boolean.valueOf(false), "notifys in chat"));
    public Setting<Boolean> Sound = this.register(new Setting<Boolean>("Sound", true, n -> this.notifyToggles.getValue()));

    public Modulee() {
        super("Module", "modul setings", Category.CLIENT, true, false, false);
        instance = this;
        setInstance();
        setEnabled(true);
    }

    @Override
    public void onDisable()
    {
        super.onDisable();
        setEnabled(true);
    }

    public static Modulee getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Modulee();
        return INSTANCE;
    }
    private void setInstance() {
        INSTANCE = this;
    }
    }

