package me.alpha432.oyvey.mixin.mixins;

import me.alpha432.oyvey.features.modules.render.RenderTweaks;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderPlayer.class})
public abstract class MixinRenderPlayer
        extends RenderLivingBase<AbstractClientPlayer> {
    public MixinRenderPlayer(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

    @Shadow
    public abstract ModelPlayer getMainModel();

    @Shadow
    protected abstract void setModelVisibilities(AbstractClientPlayer var1);

    @Inject(method={"setModelVisibilities"}, at={@At(value="TAIL")})
    private void setModelVisibilitiesI(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        ModelPlayer model = this.getMainModel();
        if (RenderTweaks.INSTANCE.isEnabled() && RenderTweaks.INSTANCE.sneak.getValue()) {
            model.isSneak = true;
        }
    }
    @Overwrite
    public void doRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (MinecraftForge.EVENT_BUS.post(new RenderPlayerEvent.Pre(entity, (RenderPlayer)((Object)this), partialTicks, x, y, z))) {
            return;
        }
        if (!entity.isUser() || this.renderManager.renderViewEntity == entity) {
            double d0 = y;
            if (entity.isSneaking() || RenderTweaks.INSTANCE.isEnabled() && RenderTweaks.INSTANCE.sneak.getValue()) {
                d0 = y - 0.125;
            }
            this.setModelVisibilities(entity);
            GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
            super.doRender(entity, x, d0, z, entityYaw, partialTicks);
            GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
        }
        MinecraftForge.EVENT_BUS.post(new RenderPlayerEvent.Post(entity, (RenderPlayer)((Object)this), partialTicks, x, y, z));
    }
}
