package com.wildfire.render.mixin;

import com.wildfire.main.GenderPlayer;
import com.wildfire.main.WildfireGender;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
    @ModifyConstant(method = "renderNameTag", constant = @Constant(intValue = -1))
    private int changeColor(int thing, Entity e) {
        if (!(e instanceof AbstractClientPlayer)) return thing;
        GenderPlayer aPlr = WildfireGender.getPlayerById(e.getUUID());
        if (aPlr == null) return thing;
        return aPlr.getPronounColorOnTick(e.tickCount);
    }
}