package com.wildfire.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Style;

public class ColorFontRenderer {
    private ColorFontRenderer() {}

    public static int drawWithColors(Font font, PoseStack m, String s, float xPos, float yPos, int[] colors) {
        MultiBufferSource.BufferSource buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        int ret = renderWithColors(font, s, xPos, yPos, colors, m.last().pose(), buffer, 15728880);
        buffer.endBatch();
        return ret;
    }

    public static int renderWithColors(Font font, String s, float xPos, float yPos, int[] colors, Matrix4f m, MultiBufferSource buffer, int light) {
        float segSize = font.getSplitter().stringWidth(s) / colors.length;
        float acc = 0;
        int c = 0;
        FontSet fs = font.getFontSet(Style.DEFAULT_FONT);
        VertexConsumer $$18 = buffer.getBuffer(fs.whiteGlyph().renderType(Font.DisplayMode.NORMAL));
        for (int i = 0; i < s.length(); i++) {
            float charw = fs.getGlyphInfo(s.charAt(i), false).getAdvance();
            acc += charw;
            BakedGlyph glyph = fs.getGlyph(s.charAt(i));

            if (c < colors.length - 1 && acc >= segSize) {
                float temp = charw - glyph.right + glyph.left;
                temp = ((acc - segSize - temp) / (glyph.right - glyph.left));
                drawGlyph(glyph, xPos, yPos, $$18, m, colors[c], colors[c + 1], 1 - temp, light);
                c++;
                acc -= segSize;
            } else {
                drawGlyph(glyph, xPos, yPos, $$18, m, colors[c], colors[c], 0, light);
            }
            xPos += fs.getGlyphInfo(s.charAt(i), false).getAdvance();
        }
        return (int) xPos;
    }
    
    private static void drawGlyph(BakedGlyph glyph, float xOff, float yOff, VertexConsumer buffer, Matrix4f m, int color1, int color2, float slice, int light) {
        float $$11 = xOff + glyph.left;
        float $$12 = xOff + glyph.right;
        float $$13 = glyph.up - 3.0F;
        float $$14 = glyph.down - 3.0F;
        float $$15 = yOff + $$13;
        float $$16 = yOff + $$14;
        float r = (float)(color1 >> 16 & 255) / 255.0F;
        float g = (float)(color1 >> 8 & 255) / 255.0F;
        float b = (float)(color1 & 255) / 255.0F;

        if (slice == 0) {
            buffer.vertex(m, $$11, $$15, 0.0F).color(r, g, b, 1).uv(glyph.u0, glyph.v0).uv2(light).endVertex();
            buffer.vertex(m, $$11, $$16, 0.0F).color(r, g, b, 1).uv(glyph.u0, glyph.v1).uv2(light).endVertex();
            buffer.vertex(m, $$12, $$16, 0.0F).color(r, g, b, 1).uv(glyph.u1, glyph.v1).uv2(light).endVertex();
            buffer.vertex(m, $$12, $$15, 0.0F).color(r, g, b, 1).uv(glyph.u1, glyph.v0).uv2(light).endVertex();
            return;
        }

        float ud = (glyph.u1 - glyph.u0) * (slice - 0.1f);
        float ud2 = (glyph.u1 - glyph.u0) * 0.2f;
        float ld = (glyph.right - glyph.left) * (slice - 0.1f);
        float ld2 = (glyph.right - glyph.left) * 0.2f;
        float r2 = (float)(color2 >> 16 & 255) / 255.0F;
        float g2 = (float)(color2 >> 8 & 255) / 255.0F;
        float b2 = (float)(color2 & 255) / 255.0F;

        if (slice > 0.1f) {
            buffer.vertex(m, $$11, $$15, 0.0F).color(r, g, b, 1).uv(glyph.u0, glyph.v0).uv2(light).endVertex();
            buffer.vertex(m, $$11, $$16, 0.0F).color(r, g, b, 1).uv(glyph.u0, glyph.v1).uv2(light).endVertex();
            buffer.vertex(m, $$11 + ld, $$16, 0.0F).color(r, g, b, 1).uv(glyph.u0 + ud, glyph.v1).uv2(light).endVertex();
            buffer.vertex(m, $$11 + ld, $$15, 0.0F).color(r, g, b, 1).uv(glyph.u0 + ud, glyph.v0).uv2(light).endVertex();
        }

        buffer.vertex(m, Math.max($$11 + ld, $$11), $$15, 0.0F).color(r, g, b, 1).uv(Math.max(glyph.u0 + ud, glyph.u0), glyph.v0).uv2(light).endVertex();
        buffer.vertex(m, Math.max($$11 + ld, $$11), $$16, 0.0F).color(r, g, b, 1).uv(Math.max(glyph.u0 + ud, glyph.u0), glyph.v1).uv2(light).endVertex();
        ud += ud2;
        ld += ld2;
        buffer.vertex(m, Math.min($$11 + ld, $$12), $$16, 0.0F).color(r2, g2, b2, 1).uv(Math.min(glyph.u0 + ud, glyph.u1), glyph.v1).uv2(light).endVertex();
        buffer.vertex(m, Math.min($$11 + ld, $$12), $$15, 0.0F).color(r2, g2, b2, 1).uv(Math.min(glyph.u0 + ud, glyph.u1), glyph.v0).uv2(light).endVertex();

        if (slice < 0.9f) {
            buffer.vertex(m, $$11 + ld, $$15, 0.0F).color(r2, g2, b2, 1).uv(glyph.u0 + ud, glyph.v0).uv2(light).endVertex();
            buffer.vertex(m, $$11 + ld, $$16, 0.0F).color(r2, g2, b2, 1).uv(glyph.u0 + ud, glyph.v1).uv2(light).endVertex();
            buffer.vertex(m, $$12, $$16, 0.0F).color(r2, g2, b2, 1).uv(glyph.u1, glyph.v1).uv2(light).endVertex();
            buffer.vertex(m, $$12, $$15, 0.0F).color(r2, g2, b2, 1).uv(glyph.u1, glyph.v0).uv2(light).endVertex();
        }
    }
}
