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
import com.wildfire.api.IHurtSound;
import com.wildfire.gui.screen.WildfireHurtSoundListScreen;
import com.wildfire.gui.screen.WildfirePronounListScreen;
import com.wildfire.main.GenderPlayer;
import com.wildfire.main.WildfireGender;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;

public class WildfirePronounList extends EntryListWidget<WildfirePronounList.Entry> {
    private final int listWidth;

    private final WildfirePronounListScreen parent;

    private GenderPlayer.Pronouns cur;
    public WildfirePronounList(WildfirePronounListScreen parent, int listWidth, int top, int bottom, GenderPlayer.Pronouns cur) {
        super(MinecraftClient.getInstance(), parent.width-4, parent.height, top-6, bottom, 20);
        this.parent = parent;
        this.listWidth = listWidth;
        this.cur = cur;
        for (GenderPlayer.Pronouns i : GenderPlayer.Pronouns.values()) {
            addEntry(new Entry(i));
        }
    }

    @Override
    protected int getScrollbarPositionX()
    {
        return parent.width / 2 + 53;
    }

    @Override
    public int getRowWidth()
    {
        return this.listWidth;
    }

    @Override
    protected void renderBackground(MatrixStack mStack) {}

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {}

    @Environment(EnvType.CLIENT)
    public class Entry extends EntryListWidget.Entry<WildfirePronounList.Entry> {

        private final GenderPlayer.Pronouns nInfo;
        private final WildfireButton btnOpenGUI;

        private Entry(final GenderPlayer.Pronouns nInfo) {
            this.nInfo = nInfo;
            btnOpenGUI = new WildfireButton(0, 0, 112, 20, new LiteralText(""), button -> parent.setPronouns(nInfo));
            btnOpenGUI.active = nInfo != null;
        }

        @Override
        public void render(MatrixStack m, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks) {
            TextRenderer font = MinecraftClient.getInstance().textRenderer;

            if (nInfo != null) {
                btnOpenGUI.active = true;
                font.draw(m, nInfo.getDisplayName(), left + 23, top + 6, 0xFFFFFF);
            } else {
                btnOpenGUI.active = false;
                font.draw(m, new TranslatableText("wildfire_gender.label.too_far").formatted(Formatting.RED), left + 23, top + 11, 0xFFFFFF);
            }
            this.btnOpenGUI.x = left;
            this.btnOpenGUI.y = top;
            this.btnOpenGUI.render(m, mouseX, mouseY, partialTicks);
            RenderSystem.setShaderTexture(0, ClickableWidget.WIDGETS_TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            drawTexture(m, left + 3, top + 3, cur == nInfo ? 208 : 224, 0, 16, 16);
        }


        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if(this.btnOpenGUI.mouseClicked(mouseX, mouseY, button)) {
                cur = nInfo;
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            return this.btnOpenGUI.mouseReleased(mouseX, mouseY, button);
        }
    }

}
