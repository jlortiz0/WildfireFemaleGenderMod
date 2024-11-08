package com.wildfire.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class WildfireColorButton extends WildfireButton {
    private final int[] colors;
    public WildfireColorButton(int x, int y, int w, PressAction onPress, int... colors) {
        this(x, y, w, onPress, null, colors);
    }

    public WildfireColorButton(int x, int y, int w, PressAction onPress, @Nullable Supplier<Text> tooltip, int... colors) {
        super(x, y, w, w, Text.empty(), onPress, tooltip);
        this.colors = colors;
    }

    public int[] getColors() {
        return colors;
    }

    @Override
    public void renderButton(MatrixStack m, int mouseX, int mouseY, float partialTicks) {
        int clr = 0x222222 + (84 << 24);
        if (this.isHovered()) clr = 0x666666 + (84 << 24);
        fill(m, getX(), getY(), getX() + getWidth(), getY() + height, clr);
        float pieceH = (float) (height - 2) / colors.length;
        for (int i = 0; i < colors.length; i++) {
            fill(m, getX() + 1, getY() + (int)(pieceH * i) + 1, getX() + getWidth() - 1, getY() + (int)(pieceH * (i + 1)) + 1, this.colors[i] + (184 << 24));
        }

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        if (this.tooltip != null && this.isHovered()) {
            MinecraftClient.getInstance().currentScreen.setTooltip(List.of(this.tooltip.get().asOrderedText()), this.getTooltipPositioner(), this.isFocused());
        }
    }

}
