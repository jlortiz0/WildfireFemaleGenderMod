package com.wildfire.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.wildfire.gui.WildfireButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class WildfireColorButton extends WildfireButton {
    private final Formatting color;
    public WildfireColorButton(int x, int y, int w, int h, Formatting color, PressAction onPress, TooltipSupplier onTooltip) {
        super(x, y, w, h, new LiteralText(""), onPress, onTooltip);
        this.color = color;
    }

    public WildfireColorButton(int x, int y, int w, int h, Formatting color, PressAction onPress) {
        this(x, y, w, h, color, onPress, NO_TOOLTIP);
    }

    public WildfireColorButton(int x, int y, int w, Formatting color, PressAction onPress) {
        this(x, y, w, w, color, onPress);
    }

    @Override
    public void renderButton(MatrixStack m, int mouseX, int mouseY, float partialTicks) {
        int clr = 0x222222 + (84 << 24);
        if (this.isHovered()) clr = 0x666666 + (84 << 24);
        fill(m, x, y, x + getWidth(), y + height, clr);
        fill(m, x + 2, y + 2, x + getWidth() - 2, y + height - 2, this.color.getColorValue() + (184 << 22));

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        if (this.isHovered()) {
            this.renderTooltip(m, mouseX, mouseY);
        }
    }
}
