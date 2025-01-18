package com.wildfire.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.wildfire.main.PronounColor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.function.BiConsumer;

@Environment(EnvType.CLIENT)
public class WildfireColorButton extends WildfireButton {
    private final PronounColor color;
    private final BiConsumer<DrawContext, String> tooltipCallback;
    public WildfireColorButton(int x, int y, int w, ButtonWidget.PressAction onPress, BiConsumer<DrawContext, String> tooltipCallback, PronounColor color) {
        super(x, y, w, w, Text.empty(), onPress);
        this.color = color;
        this.tooltipCallback = tooltipCallback;
    }

    public PronounColor getColor() {
        return color;
    }

    @Override
    public void renderWidget(DrawContext ctx, int mouseX, int mouseY, float partialTicks) {
        int clr = 0x222222 + (84 << 24);
        if (this.isSelected()) clr = 0x666666 + (84 << 24);
        ctx.fill(getX(), getY(), getX() + getWidth(), getY() + height, clr);
        float pieceH = (float) (height - 2) / color.colors.length;
        for (int i = 0; i < color.colors.length; i++) {
            ctx.fill(getX() + 1, getY() + (int)(pieceH * i) + 1, getX() + getWidth() - 1, getY() + (int)(pieceH * (i + 1)) + 1, this.color.colors[i] + (184 << 24));
        }

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        if (this.isSelected() && this.color.name != null) {
            this.tooltipCallback.accept(ctx, this.color.name);
        }
    }
}
