package me.alpha432.oyvey.mixin.mixins;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.client.MainMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( GuiMainMenu.class )
public class MixinGuiMainMenu extends GuiScreen {
    @Inject( method = { "drawScreen" }, at = { @At( "TAIL" ) }, cancellable = true )
    public void drawText( int mouseX, int mouseY, float partialTicks, CallbackInfo ci ) {
        if (MainMenu.INSTANCE.isEnabled())
            OyVey.textManager.drawStringWithShadow(OyVey.MODNAME, 2, MainMenu.INSTANCE.logoy.getValue().intValue(),0xFFFFFF);
    }
}