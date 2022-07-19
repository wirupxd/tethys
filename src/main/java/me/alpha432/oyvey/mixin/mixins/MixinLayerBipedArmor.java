package me.alpha432.oyvey.mixin.mixins;

import me.alpha432.oyvey.features.modules.render.NoArmor;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LayerBipedArmor.class)
public abstract class MixinLayerBipedArmor {

    @Shadow
    protected abstract void setModelVisible(ModelBiped modelVisible);

    /**
     * @author h0lloww for drainware
     */
    @Overwrite
    protected void setModelSlotVisible(ModelBiped p_188359_1_, EntityEquipmentSlot slotIn) {
        setModelVisible(p_188359_1_);

        switch (slotIn) {
            case HEAD:
                p_188359_1_.bipedHead.showModel = !NoArmor.INSTANCE.isEnabled() || NoArmor.INSTANCE.helmet.getValue();
                p_188359_1_.bipedHeadwear.showModel = !NoArmor.INSTANCE.isEnabled() || NoArmor.INSTANCE.helmet.getValue();
                break;
            case CHEST:
                p_188359_1_.bipedBody.showModel = !NoArmor.INSTANCE.isEnabled() || NoArmor.INSTANCE.chestplate.getValue();
                p_188359_1_.bipedRightArm.showModel = !NoArmor.INSTANCE.isEnabled() || NoArmor.INSTANCE.chestplate.getValue();
                p_188359_1_.bipedLeftArm.showModel = !NoArmor.INSTANCE.isEnabled() || NoArmor.INSTANCE.chestplate.getValue();
                break;
            case LEGS:
                p_188359_1_.bipedBody.showModel = !NoArmor.INSTANCE.isEnabled() || NoArmor.INSTANCE.thighHighs.getValue();
                p_188359_1_.bipedRightLeg.showModel = !NoArmor.INSTANCE.isEnabled() || NoArmor.INSTANCE.thighHighs.getValue();
                p_188359_1_.bipedLeftLeg.showModel = !NoArmor.INSTANCE.isEnabled() || NoArmor.INSTANCE.thighHighs.getValue();
                break;
            case FEET:
                p_188359_1_.bipedRightLeg.showModel = !NoArmor.INSTANCE.isEnabled() || NoArmor.INSTANCE.boots.getValue();
                p_188359_1_.bipedLeftLeg.showModel = !NoArmor.INSTANCE.isEnabled() || NoArmor.INSTANCE.boots.getValue();
        }
    }

}
