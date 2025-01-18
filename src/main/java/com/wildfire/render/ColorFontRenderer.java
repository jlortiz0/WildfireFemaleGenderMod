package com.wildfire.render;

import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.GlyphRenderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.Style;
import org.joml.Matrix4f;

public class ColorFontRenderer {
    public static int drawWithColors(TextRenderer font, DrawContext ctx, String s, float xPos, float yPos, int[] colors) {
        VertexConsumerProvider.Immediate buffer = ctx.getVertexConsumers();
        int ret = renderWithColors(font, s, xPos, yPos, colors, ctx.getMatrices().peek().getPositionMatrix(), buffer, TextRenderer.TextLayerType.NORMAL, 15728880);
        buffer.drawCurrentLayer();
        return ret;
    }

    public static int renderWithColors(TextRenderer font, String s, float xPos, float yPos, int[] colors, Matrix4f m, VertexConsumerProvider buffer, TextRenderer.TextLayerType layerType, int light) {
        float segSize = font.getTextHandler().getWidth(s) / colors.length;
        float acc = 0;
        int c = 0;
        FontStorage fs = font.getFontStorage(Style.DEFAULT_FONT_ID);
        VertexConsumer $$18 = buffer.getBuffer(fs.getRectangleRenderer().getLayer(layerType));
        for (int i = 0; i < s.length(); i++) {
            float charw = fs.getGlyph(s.charAt(i), false).getAdvance();
            acc += charw;
            GlyphRenderer glyph = fs.getGlyphRenderer(s.charAt(i));

            if (c < colors.length - 1 && acc >= segSize) {
                float temp = (acc - segSize) / charw;
                drawGlyph(glyph, xPos, yPos, $$18, m, colors[c], colors[c + 1], 1 - temp, light, charw);
                c++;
                acc -= segSize;
            } else {
                drawGlyph(glyph, xPos, yPos, $$18, m, colors[c], colors[c], 0, light, charw);
            }
            xPos += charw;
        }
        return (int) xPos;
    }

    private static void drawGlyph(GlyphRenderer glyph, float xOff, float yOff, VertexConsumer buffer, Matrix4f m, int color1, int color2, float slice, int light, float advance) {
        float spaceSz = 1 / advance;
        if (slice < 0.3f) color1 = color2;
        if (slice < 0.3f || slice + spaceSz > 0.7f) slice = 0;

        float $$11 = xOff + glyph.minX;
        float $$12 = xOff + glyph.maxX;
        float $$13 = glyph.minY;
        float $$14 = glyph.maxY;
        float $$15 = yOff + $$13;
        float $$16 = yOff + $$14;
        float r = (float)(color1 >> 16 & 255) / 255.0F;
        float g = (float)(color1 >> 8 & 255) / 255.0F;
        float b = (float)(color1 & 255) / 255.0F;

        if (slice == 0) {
            buffer.vertex(m, $$11, $$15, 0.0F).color(r, g, b, 1).texture(glyph.minU, glyph.minV).light(light);
            buffer.vertex(m, $$11, $$16, 0.0F).color(r, g, b, 1).texture(glyph.minU, glyph.maxV).light(light);
            buffer.vertex(m, $$12, $$16, 0.0F).color(r, g, b, 1).texture(glyph.maxU, glyph.maxV).light(light);
            buffer.vertex(m, $$12, $$15, 0.0F).color(r, g, b, 1).texture(glyph.maxU, glyph.minV).light(light);
            return;
        }

        float widScale = advance / (glyph.maxX - glyph.minX);
        float ud = (glyph.maxU - glyph.minU) * (slice - 0.1f) * widScale;
        float ud2 = (glyph.maxU - glyph.minU) * 0.2f * widScale;
        float ld = advance * (slice - 0.1f);
        float ld2 = advance * 0.2f;
        float r2 = (float)(color2 >> 16 & 255) / 255.0F;
        float g2 = (float)(color2 >> 8 & 255) / 255.0F;
        float b2 = (float)(color2 & 255) / 255.0F;

        buffer.vertex(m, $$11, $$15, 0.0F).color(r, g, b, 1).texture(glyph.minU, glyph.minV).light(light);
        buffer.vertex(m, $$11, $$16, 0.0F).color(r, g, b, 1).texture(glyph.minU, glyph.maxV).light(light);
        buffer.vertex(m, $$11 + ld, $$16, 0.0F).color(r, g, b, 1).texture(glyph.minU + ud, glyph.maxV).light(light);
        buffer.vertex(m, $$11 + ld, $$15, 0.0F).color(r, g, b, 1).texture(glyph.minU + ud, glyph.minV).light(light);

        buffer.vertex(m, Math.max($$11 + ld, $$11), $$15, 0.0F).color(r, g, b, 1).texture(glyph.minU + ud, glyph.minV).light(light);
        buffer.vertex(m, Math.max($$11 + ld, $$11), $$16, 0.0F).color(r, g, b, 1).texture(glyph.minU + ud, glyph.maxV).light(light);
        ud += ud2;
        ld += ld2;
        buffer.vertex(m, Math.min($$11 + ld, $$12), $$16, 0.0F).color(r2, g2, b2, 1).texture(glyph.minU + ud, glyph.maxV).light(light);
        buffer.vertex(m, Math.min($$11 + ld, $$12), $$15, 0.0F).color(r2, g2, b2, 1).texture(glyph.minU + ud, glyph.minV).light(light);

        buffer.vertex(m, $$11 + ld, $$15, 0.0F).color(r2, g2, b2, 1).texture(glyph.minU + ud, glyph.minV).light(light);
        buffer.vertex(m, $$11 + ld, $$16, 0.0F).color(r2, g2, b2, 1).texture(glyph.minU + ud, glyph.maxV).light(light);
        buffer.vertex(m, $$12, $$16, 0.0F).color(r2, g2, b2, 1).texture(glyph.maxU, glyph.maxV).light(light);
        buffer.vertex(m, $$12, $$15, 0.0F).color(r2, g2, b2, 1).texture(glyph.maxU, glyph.minV).light(light);
    }
}
