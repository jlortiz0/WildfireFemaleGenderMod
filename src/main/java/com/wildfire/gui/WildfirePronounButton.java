package com.wildfire.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.function.Function;

public class WildfirePronounButton extends WildfireButton {
    private final Function<Integer, Integer> getColor;

    public WildfirePronounButton(int x, int y, int w, int h, Component text, Button.OnPress onPress, Function<Integer, Integer> colorGetter) {
        super(x, y, w, h, text, onPress, NO_TOOLTIP);
        this.getColor = colorGetter;
    }

    @Override
    public void renderButton(PoseStack m, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        int clr = 0x444444 + (84 << 24);
        if(this.isHoveredOrFocused()) clr = 0x666666 + (84 << 24);
        if(!this.active)  clr = 0x222222 + (84 << 24);
        if(!transparent) fill(m, x, y, x + getWidth(), y + height, clr);

        font.draw(m, this.getMessage(), x + (this.width / 2) - (font.width(this.getMessage()) / 2) + 1, y + (int) Math.ceil((float) height / 2f) - font.lineHeight / 2, this.getColor.apply(minecraft.player.tickCount));
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }
}
