package me.alpha432.oyvey.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ConfigModule
        extends Module {
    private final Setting<String> configName = this.register(new Setting<String>("Config", "Default"));
    private final Setting<Boolean> Save = this.register(new Setting<Boolean>("Save", Boolean.FALSE));
    private final Setting<Boolean> Load = this.register(new Setting<Boolean>("Load", Boolean.FALSE));
    private final Setting<Boolean> List = this.register(new Setting<Boolean>("List", Boolean.FALSE));

    public ConfigModule() {
        super("Config", "fotr", Module.Category.CLIENT, true, false, false);
        this.setEnabled(true);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.setEnabled(true);
    }

    @Override
    public void onUpdate() {
        if (this.Save.getValue().booleanValue()) {
            OyVey.configManager.saveConfig(this.configName.getValue());
            Command.sendMessage(ChatFormatting.GREEN + "Config '" + this.configName.getValue() + "' has been saved.");
            this.Save.setValue(false);
            return;
        }
        if (this.Load.getValue().booleanValue()) {
            if (OyVey.configManager.configExists(this.configName.getValue())) {
                OyVey.configManager.loadConfig(this.configName.getValue());
                Command.sendMessage(ChatFormatting.GREEN + "Config '" + this.configName.getValue() + "' has been loaded.");
            } else {
                Command.sendMessage(ChatFormatting.RED + "Config '" + this.configName.getValue() + "' does not exist.");
            }
            this.Load.setValue(false);
            return;
        }
        if (this.List.getValue().booleanValue()) {
            String configs = "Configs: ";
            File file = new File("Tethys/");
            java.util.List<File> directories = Arrays.stream(file.listFiles()).filter(File::isDirectory).filter(f -> !f.getName().equals("util")).collect(Collectors.toList());
            StringBuilder builder = new StringBuilder(configs);
            for (File file1 : directories)
                builder.append(file1.getName() + ", ");
            configs = builder.toString();
            Command.sendMessage(configs);
            this.List.setValue(false);
        }   return;
    }
}

