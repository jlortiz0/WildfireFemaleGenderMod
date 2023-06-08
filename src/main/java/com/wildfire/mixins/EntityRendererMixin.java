package com.wildfire.mixins;

import com.wildfire.main.GenderPlayer;
import com.wildfire.main.WildfireGender;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
    @ModifyConstant(method = "renderLabelIfPresent", constant = @Constant(intValue = -1))
    private int changeColor(int thing, Entity e) {
        if (!(e instanceof AbstractClientPlayerEntity)) return thing;
        GenderPlayer aPlr = WildfireGender.getPlayerById(e.getUuid());
        if (aPlr == null) return thing;
        return aPlr.getPronounColorOnTick(e.age);
    }
}
