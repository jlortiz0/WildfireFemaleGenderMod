package com.wildfire.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;

public class WildfireColorButton extends WildfireButton {
    private final int[] colors;
    public WildfireColorButton(int x, int y, int w, Button.OnPress onPress, int... colors) {
        super(x, y, w, w, TextComponent.EMPTY, onPress, NO_TOOLTIP);
        this.colors = colors;
    }

    public int[] getColors() {
        return colors;
    }

    @Override
    public void renderButton(PoseStack m, int mouseX, int mouseY, float partialTicks) {
        int clr = 0x222222 + (84 << 24);
        if (this.isHoveredOrFocused()) clr = 0x666666 + (84 << 24);
        fill(m, x, y, x + getWidth(), y + height, clr);
        float pieceH = (float) (height - 2) / colors.length;
        for (int i = 0; i < colors.length; i++) {
            fill(m, x + 1, y + (int)(pieceH * i) + 1, x + getWidth() - 1, y + (int)(pieceH * (i + 1)) + 1, this.colors[i] + (184 << 24));
        }

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        if (this.isHoveredOrFocused()) {
            this.renderToolTip(m, mouseX, mouseY);
        }
    }
}
