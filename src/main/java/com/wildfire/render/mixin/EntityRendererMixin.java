package com.wildfire.render.mixin;

import com.mojang.math.Matrix4f;
import com.wildfire.main.GenderPlayer;
import com.wildfire.main.WildfireGender;
import com.wildfire.render.ColorFontRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
    @Redirect(method = "renderNameTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;drawInBatch(Lnet/minecraft/network/chat/Component;FFIZLcom/mojang/math/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;ZII)I", ordinal = 1))
    private int drawWithColors(Font font, Component name, float xOff, float yOff, int color, boolean shadow, Matrix4f m, MultiBufferSource buffer, boolean italic, int alpha, int light, Entity e) {
        if (!(e instanceof AbstractClientPlayer)) return font.drawInBatch(name, xOff, yOff, color, shadow, m, buffer, italic, alpha, light);
        GenderPlayer aPlr = WildfireGender.getPlayerById(e.getUUID());
        if (aPlr == null) return font.drawInBatch(name, xOff, yOff, color, shadow, m, buffer, italic, alpha, light);
        int[] colors = aPlr.getPronounColor();
        if (colors.length == 1) return font.drawInBatch(name, xOff, yOff, colors[0], shadow, m, buffer, italic, alpha, light);
        return ColorFontRenderer.renderWithColors(font, name.getString(), xOff, yOff, colors, m, buffer, light);
    }
}