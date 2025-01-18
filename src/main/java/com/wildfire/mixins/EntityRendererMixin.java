package com.wildfire.mixins;

import com.wildfire.main.WildfireGender;
import com.wildfire.main.entitydata.PlayerConfig;
import com.wildfire.render.ColorFontRenderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Redirect(at=@At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/Text;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)I", ordinal = 1), method="renderLabelIfPresent")
    private int renderColorLabel(TextRenderer instance, Text text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, TextRenderer.TextLayerType layerType, int backgroundColor, int light, Entity e) {
        if (!(e instanceof AbstractClientPlayerEntity)) return instance.draw(text, x, y, color, shadow, matrix, vertexConsumers, layerType, backgroundColor, light);
        PlayerConfig aPlr = WildfireGender.getPlayerById(e.getUuid());
        if (aPlr == null) return instance.draw(text, x, y, color, shadow, matrix, vertexConsumers, layerType, backgroundColor, light);
        int[] colors = aPlr.getPronounColor().colors;
        if (colors.length == 1) return instance.draw(text, x, y, colors[0], shadow, matrix, vertexConsumers, layerType, backgroundColor, light);
        return ColorFontRenderer.renderWithColors(instance, text.getString(), x, y, colors, matrix, vertexConsumers, layerType, light);
    }
}
