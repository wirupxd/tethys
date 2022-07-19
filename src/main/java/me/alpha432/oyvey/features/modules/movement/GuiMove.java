package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class GuiMove extends Module {
    private static final KeyBinding[] keys = new KeyBinding[]{GuiMove.mc.gameSettings.keyBindForward, GuiMove.mc.gameSettings.keyBindBack, GuiMove.mc.gameSettings.keyBindLeft, GuiMove.mc.gameSettings.keyBindRight, GuiMove.mc.gameSettings.keyBindJump, GuiMove.mc.gameSettings.keyBindSprint};

    public GuiMove() {
        super("InvMove", "flushed", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (GuiMove.mc.currentScreen instanceof GuiChat || GuiMove.mc.currentScreen == null) {
            return;
        }
        for (KeyBinding bind : keys) {
            KeyBinding.setKeyBindState((int)bind.getKeyCode(), (boolean) Keyboard.isKeyDown((int)bind.getKeyCode()));
        }
        if (GuiMove.mc.currentScreen == null) {
            for (KeyBinding bind : keys) {
                if (Keyboard.isKeyDown((int)bind.getKeyCode())) continue;
                KeyBinding.setKeyBindState((int)bind.getKeyCode(), (boolean)false);
            }
        }
        if (!(GuiMove.mc.currentScreen instanceof GuiChat)) {
            if (Keyboard.isKeyDown((int)200)) {
                GuiMove.mc.player.rotationPitch -= 5.0f;
            }
            if (Keyboard.isKeyDown((int)208)) {
                GuiMove.mc.player.rotationPitch += 5.0f;
            }
            if (Keyboard.isKeyDown((int)205)) {
                GuiMove.mc.player.rotationYaw += 5.0f;
            }
            if (Keyboard.isKeyDown((int)203)) {
                GuiMove.mc.player.rotationYaw -= 5.0f;
            }
            if (GuiMove.mc.player.rotationPitch > 90.0f) {
                GuiMove.mc.player.rotationPitch = 90.0f;
            }
            if (GuiMove.mc.player.rotationPitch < -90.0f) {
                GuiMove.mc.player.rotationPitch = -90.0f;
            }
        }
    }
}

