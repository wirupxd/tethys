// Decompiled with: CFR 0.152
// Class Version: 8
package me.alpha432.oyvey.mixin.mixins;

import me.alpha432.oyvey.features.modules.client.Capes;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(value={AbstractClientPlayer.class})
public abstract class MixinAbstractClientPlayer {
    @Shadow
    @Nullable
    protected abstract NetworkPlayerInfo getPlayerInfo();

    @Inject(method={"getLocationCape"}, at={@At(value="HEAD")}, cancellable=true)
    public void getLocationCape(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        if (Capes.getInstance().isEnabled()) {
            NetworkPlayerInfo networkPlayerInfo = this.getPlayerInfo();
            UUID uUID = null;
            if (networkPlayerInfo != null) {
                uUID = this.getPlayerInfo().getGameProfile().getId();
            }
            ResourceLocation resourceLocation = Capes.getCapeResource((AbstractClientPlayer)((Object)this));
            if (uUID != null && Capes.hasCape(uUID)) {
                callbackInfoReturnable.setReturnValue(resourceLocation);
            }
        }
    }
}
