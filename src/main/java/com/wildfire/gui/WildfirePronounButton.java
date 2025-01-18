package com.wildfire.gui;

import com.wildfire.main.PronounColor;
import com.wildfire.render.ColorFontRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class WildfirePronounButton extends WildfireButton {
    private final Supplier<PronounColor> getColor;

    public WildfirePronounButton(int x, int y, int w, int h, Text text, ButtonWidget.PressAction onPress, Supplier<PronounColor> colorGetter) {
        super(x, y, w, h, text, onPress);
        this.getColor = colorGetter;
    }

    @Override
    public void renderWidget(DrawContext ctx, int mouseX, int mouseY, float partialTicks) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        TextRenderer font = minecraft.textRenderer;
        int clr = 0x444444 + (84 << 24);
        if(this.isSelected()) clr = 0x666666 + (84 << 24);
        if(!this.active)  clr = 0x222222 + (84 << 24);
        if(!transparent) ctx.fill(getX(), getY(), getX() + getWidth(), getY() + height, clr);
        ColorFontRenderer.drawWithColors(font, ctx, this.getMessage().getString(), getX() + (this.width / 2) - (font.getWidth(this.getMessage()) / 2) + 1, getY() + (int) Math.ceil((float) height / 2f) - font.fontHeight / 2, this.getColor.get().colors);
    }
}
