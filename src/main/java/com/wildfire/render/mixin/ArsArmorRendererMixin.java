package com.wildfire.render.mixin;

import com.hollingsworth.arsnouveau.client.renderer.item.ArmorRenderer;
import com.hollingsworth.arsnouveau.common.armor.AnimatedMagicArmor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bernie.ars_nouveau.geckolib3.model.AnimatedGeoModel;
import software.bernie.ars_nouveau.geckolib3.renderers.geo.GeoArmorRenderer;

@Mixin(value = ArmorRenderer.class, remap = false)
public class ArsArmorRendererMixin extends GeoArmorRenderer<AnimatedMagicArmor> {

    public ArsArmorRendererMixin(AnimatedGeoModel<AnimatedMagicArmor> modelProvider) {
        super(modelProvider);
    }

    @Inject(method="getTextureLocation(Lcom/hollingsworth/arsnouveau/common/armor/AnimatedMagicArmor;)Lnet/minecraft/resources/ResourceLocation;", at = @At("HEAD"))
    private void fixNullPtr(AnimatedMagicArmor instance, CallbackInfoReturnable<ResourceLocation> cir) {
        if (this.itemStack == null) {
            this.itemStack = instance.getDefaultInstance();
        }
    }
}
