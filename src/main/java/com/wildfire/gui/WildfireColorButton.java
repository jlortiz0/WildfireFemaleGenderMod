package com.wildfire.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class WildfireColorButton extends WildfireButton {
    private final int[] colors;
    public WildfireColorButton(int x, int y, int w, PressAction onPress, int... colors) {
        this(x, y, w, onPress, null, colors);
    }

    public WildfireColorButton(int x, int y, int w, PressAction onPress, Supplier<Text> tooltip, int... colors) {
        super(x, y, w, w, Text.empty(), onPress, tooltip);
        this.colors = colors;
    }

    public int[] getColors() {
        return colors;
    }

    @Override
    public void renderButton(DrawContext ctx, int mouseX, int mouseY, float partialTicks) {
        int clr = 0x222222 + (84 << 24);
        if (this.isSelected()) clr = 0x666666 + (84 << 24);
        ctx.fill(this.getX(), this.getY(), this.getX() + getWidth(), this.getY() + height, clr);
        float pieceH = (float) (height - 2) / colors.length;
        for (int i = 0; i < colors.length; i++) {
            ctx.fill(this.getX() + 1, this.getY() + (int)(pieceH * i) + 1, this.getX() + getWidth() - 1, this.getY() + (int)(pieceH * (i + 1)) + 1, this.colors[i] + (184 << 24));
        }

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        if (this.isSelected() && this.tooltip != null) {
            ctx.drawTooltip(MinecraftClient.getInstance().textRenderer, this.tooltip.get(), mouseX, mouseY);
        }
    }

}
