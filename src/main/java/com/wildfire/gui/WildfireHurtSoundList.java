/*
Wildfire's Female Gender Mod is a female gender mod created for Minecraft.
Copyright (C) 2022  WildfireRomeo

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
import com.mojang.blaze3d.vertex.PoseStack;
import com.wildfire.gui.screen.WildfireHurtSoundListScreen;
import com.wildfire.main.HurtSound;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;

public class WildfireHurtSoundList extends ObjectSelectionList<WildfireHurtSoundList.Entry>
{
    private HurtSound cur;

    private final int listWidth;

    private final WildfireHurtSoundListScreen parent;

    public WildfireHurtSoundList(WildfireHurtSoundListScreen parent, int listWidth, int top, int bottom, HurtSound cur)
    {
        super(parent.getMinecraft(), parent.width-4, parent.height, top-6, bottom, 20);
        this.parent = parent;
        this.listWidth = listWidth;
        this.cur = cur;
        for (HurtSound hs : HurtSound.values()) {
            addEntry(new Entry(hs));
        }
    }

    @Override
    protected int getScrollbarPosition()
    {
        return parent.width / 2 + 55;
    }

    @Override
    public int getRowWidth()
    {
        return this.listWidth;
    }

    @Override
    protected void renderBackground(@Nonnull PoseStack mStack) {}

    public class Entry extends ObjectSelectionList.Entry<WildfireHurtSoundList.Entry> {

        public final HurtSound nInfo;
        private final WildfireButton btnOpenGUI;

        private Entry(final HurtSound nInfo) {
            this.nInfo = nInfo;
            btnOpenGUI = new WildfireButton(0, 0, 112, 20, Component.empty(), button -> parent.setHurtSound(nInfo));
            btnOpenGUI.active = true;
        }

        @Override
        public void render(@Nonnull PoseStack m, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks) {
            Font font = minecraft.font;

            font.draw(m, nInfo.getName(), left + 23, top + 6, 0xFFFFFF);
            this.btnOpenGUI.x = left;
            this.btnOpenGUI.y = top;
            this.btnOpenGUI.render(m, mouseX, mouseY, partialTicks);

            RenderSystem.setShaderTexture(0, AbstractWidget.WIDGETS_LOCATION);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            blit(m, left + 3, top + 3, cur == nInfo ? 208 : 224, 0, 16, 16);
        }


        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (this.btnOpenGUI.mouseClicked(mouseX, mouseY, button)) {
                cur = nInfo;
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            return this.btnOpenGUI.mouseReleased(mouseX, mouseY, button);
        }

        @Nonnull
        @Override
        public Component getNarration() {
            return Component.empty();
        }
    }
}
