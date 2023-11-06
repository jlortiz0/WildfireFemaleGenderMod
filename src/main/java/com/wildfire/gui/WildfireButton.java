/*
Wildfire's Female Gender Mod is a female gender mod created for Minecraft.
Copyright (C) 2022 WildfireRomeo

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.wildfire.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class WildfireButton extends ButtonWidget {

   public boolean transparent = false;
   protected final Supplier<Text> tooltip;

   public WildfireButton(int x, int y, int w, int h, Text text, ButtonWidget.PressAction onPress, Supplier<Text> onTooltip) {
      super(x, y, w, h, text, onPress, DEFAULT_NARRATION_SUPPLIER);
      this.tooltip = onTooltip;
   }
   public WildfireButton(int x, int y, int w, int h, Text text, ButtonWidget.PressAction onPress) {
      this(x, y, w, h, text, onPress, null);
   }

   @Override
   public void renderButton(DrawContext ctx, int mouseX, int mouseY, float partialTicks) {
      MinecraftClient minecraft = MinecraftClient.getInstance();
      TextRenderer font = minecraft.textRenderer;
      int clr = 0x444444 + (84 << 24);
      if(this.isSelected()) clr = 0x666666 + (84 << 24);
      if(!this.active)  clr = 0x222222 + (84 << 24);
      if(!transparent) ctx.fill(this.getX(), this.getY(), this.getX() + getWidth(), this.getY() + height, clr);

      ctx.drawText(font, this.getMessage(), this.getX() + (this.width / 2) - (font.getWidth(this.getMessage()) / 2) + 1, this.getY() + (int) Math.ceil((float) height / 2f) - font.fontHeight / 2, active ? 0xFFFFFF : 0x666666, false);
      RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

      if(this.isSelected() && tooltip != null) {
         ctx.drawTooltip(font, tooltip.get(), mouseX, mouseY);
      }
   }

   public WildfireButton setTransparent(boolean b) {
      this.transparent = b;
      return this;
   }
}