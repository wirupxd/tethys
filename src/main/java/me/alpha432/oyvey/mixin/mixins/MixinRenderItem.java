package me.alpha432.oyvey.mixin.mixins;

import me.alpha432.oyvey.features.modules.render.EnchantColor;
import me.alpha432.oyvey.features.modules.render.Viewmodel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({RenderItem.class})
public abstract class MixinRenderItem {
    @Shadow protected abstract void renderModel(IBakedModel model, int color);

    @ModifyArg(method={"renderEffect"}, at=@At(value="INVOKE", target="net/minecraft/client/renderer/RenderItem.renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V"))
    private int renderEffect(int n) {
        EnchantColor enchantColor = EnchantColor.getINSTANCE();
        return enchantColor.isEnabled() ? enchantColor.getColor() : n;
    }

    @Inject(method = "renderItemModel", at = @At("INVOKE"))
    public void renderItem(ItemStack stack, IBakedModel bakedmodel, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo ci) {
        if(transform == null)
            return;

        if(Viewmodel.INSTANCE == null)
            return;

        if (Viewmodel.INSTANCE.isEnabled() && (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)) {
            float scale = Viewmodel.INSTANCE.scale.getValue();
            GL11.glScalef(scale / 10, scale / 10, scale / 10);
            float translateX = Viewmodel.INSTANCE.translateX.getValue();
            float translateY = Viewmodel.INSTANCE.translateY.getValue();
            float translateZ = Viewmodel.INSTANCE.translateZ.getValue();
            if (transform.equals(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND)) {
                if (Minecraft.getMinecraft().player.getActiveHand() == EnumHand.OFF_HAND && Minecraft.getMinecraft().player.isHandActive() && Viewmodel.INSTANCE.pauseOnEat.getValue())
                    return;

                GL11.glTranslated(translateX / 15, translateY / 15, translateZ / 15);
            } else {
                if (Minecraft.getMinecraft().player.getActiveHand() == EnumHand.MAIN_HAND && Minecraft.getMinecraft().player.isHandActive() && Viewmodel.INSTANCE.pauseOnEat.getValue())
                    return;

                GL11.glTranslated(-translateX / 15, translateY / 15, translateZ / 15);
            }
        }

    }

}
