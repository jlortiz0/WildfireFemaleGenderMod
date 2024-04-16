package com.wildfire.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class WildfireToggleButton extends WildfireButton {
    private final Supplier<Boolean> state;

    public WildfireToggleButton(int x, int y, int w, int h, Component text, OnPress onPress, OnTooltip onTooltip, Supplier<Boolean> state) {
        super(x, y, w, h, text, onPress, onTooltip);
        this.state = state;
    }

    public void renderButton(@Nonnull PoseStack m, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        int clr = 0x444444 + (84 << 24);
        if(this.isHoveredOrFocused()) clr = 0x666666 + (84 << 24);
        if(!this.active)  clr = 0x222222 + (84 << 24);
        if(!transparent) fill(m, x, y, x + getWidth(), y + height, clr);
        if (this.state.get()) {
            // clr = this.state.get() ? ChatFormatting.GREEN.getColor() : ChatFormatting.RED.getColor();
            clr = this.active ? 0xff55ff55 : 0xff2a7f2a;
            fill(m, x, y, x + getWidth(), y + 2, clr);
            fill(m, x + getWidth() - 2, y, x + getWidth(), y + height, clr);
            fill(m, x + getWidth(), y + height - 2, x, y + height, clr);
            fill(m, x, y, x + 2, y + height, clr);
        }


        font.draw(m, this.getMessage(), x + (this.width / 2) - (font.width(this.getMessage()) / 2) + 1, y + (int) Math.ceil((float) height / 2f) - font.lineHeight / 2, active ? 0xFFFFFF : 0x666666);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        if(this.isHoveredOrFocused()) {
            this.renderToolTip(m, mouseX, mouseY);
        }
    }
}
