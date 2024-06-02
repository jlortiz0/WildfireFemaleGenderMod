package com.wildfire.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.function.Function;

public class WildfirePronounButton extends WildfireButton {
    private final Function<Integer, Integer> getColor;

    public WildfirePronounButton(int x, int y, int w, int h, Text text, PressAction onPress, Function<Integer, Integer> colorGetter) {
        super(x, y, w, h, text, onPress);
        this.getColor = colorGetter;
    }

    @Override
    public void renderButton(MatrixStack m, int mouseX, int mouseY, float partialTicks) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        TextRenderer font = minecraft.textRenderer;
        int clr = 0x444444 + (84 << 24);
        if(this.isHovered()) clr = 0x666666 + (84 << 24);
        if(!this.active)  clr = 0x222222 + (84 << 24);
        if(!transparent) fill(m, x, y, x + getWidth(), y + height, clr);

        font.draw(m, this.getMessage(), x + (this.width / 2) - (font.getWidth(this.getMessage()) / 2) + 1, y + (int) Math.ceil((float) height / 2f) - font.fontHeight / 2, this.getColor.apply(minecraft.player.age));
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }
}
