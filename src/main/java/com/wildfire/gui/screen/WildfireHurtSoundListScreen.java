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

package com.wildfire.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.wildfire.api.IHurtSound;
import com.wildfire.gui.WildfireButton;
import com.wildfire.gui.WildfireHurtSoundList;
import com.wildfire.main.GenderPlayer;
import com.wildfire.main.WildfireGender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.UUID;


public class WildfireHurtSoundListScreen extends BaseWildfireScreen {
	private Identifier TXTR_BACKGROUND;

	private WildfireHurtSoundList SOUND_LIST;
	private final GenderPlayer aPlr;

	public WildfireHurtSoundListScreen(Screen parent, UUID plr) {
		super(Text.translatable("wildfire_gender.player_list.female_sounds"), parent, plr);
		aPlr = getPlayer();
	}

	@Override
	public void init() {
		int y = this.height / 2 - 20;

		this.addDrawableChild(new WildfireButton(this.width / 2 + 53, y - 74, 9, 9, Text.translatable("wildfire_gender.label.exit"), button -> MinecraftClient.getInstance().setScreen(parent)));

		SOUND_LIST = new WildfireHurtSoundList(this, 118, (y - 61), (y + 71), aPlr.getHurtSounds());
		SOUND_LIST.setRenderBackground(false);
		SOUND_LIST.setRenderHorizontalShadows(false);
		this.addSelectableChild(this.SOUND_LIST);

		this.TXTR_BACKGROUND = new Identifier(WildfireGender.MODID, "textures/gui/player_list.png");

		super.init();
	}
    public void setHurtSound(IHurtSound sound) {
		if (aPlr.updateHurtSounds(sound.getId())) {
			if (sound.getSnd() != null) {
				client.player.playSound(sound.getSnd(), SoundCategory.PLAYERS, 1, 1);
			}
			GenderPlayer.saveGenderInfo(aPlr);
		}
    }

	@Override
	public void render(MatrixStack m, int f1, int f2, float f3) {
		super.renderBackground(m);
		MinecraftClient mc = MinecraftClient.getInstance();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		if(this.TXTR_BACKGROUND != null) {
			RenderSystem.setShader(GameRenderer::getPositionTexProgram);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, this.TXTR_BACKGROUND);
		}
		int i = (this.width - 132) / 2;
		int j = (this.height - 156) / 2 - 20;
		drawTexture(m, i, j, 0, 0, 192, 174);

		int x = (this.width / 2);
		int y = (this.height / 2) - 20;

		super.render(m, f1, f2, f3);

        double scale = mc.getWindow().getScaleFactor();
        int left = x - 59;
        int bottom = y - 32;
        int width = 118;
        int height = 134;
        RenderSystem.enableScissor((int)(left  * scale), (int) (bottom * scale),
				(int)(width * scale), (int) (height * scale));

		SOUND_LIST.render(m, f1, f2, f3);
		RenderSystem.disableScissor();

		this.textRenderer.draw(m, Text.translatable("wildfire_gender.hurt_sound_list.title"), x - 60, y - 73, 4473924);
	}
}