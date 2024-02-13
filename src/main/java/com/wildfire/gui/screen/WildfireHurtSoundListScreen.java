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

package com.wildfire.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wildfire.gui.WildfireButton;
import com.wildfire.gui.WildfireHurtSoundList;
import com.wildfire.main.GenderPlayer;
import com.wildfire.main.HurtSound;
import com.wildfire.main.WildfireGender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.UUID;


public class WildfireHurtSoundListScreen extends Screen {

	private ResourceLocation TXTR_BACKGROUND;

 	public static GenderPlayer aPlr;

	WildfireHurtSoundList SOUND_LIST;
	private Minecraft client;
	private final BaseWildfireScreen parent;
	public WildfireHurtSoundListScreen(Minecraft mc, UUID plr, BaseWildfireScreen parent) {
		super(Component.translatable("wildfire_gender.tooltip.hurt_sounds"));
		aPlr = WildfireGender.getPlayerById(plr);
		client = mc;
		this.parent = parent;
	}

	@Override
	public boolean isPauseScreen() { return false; }

	@Override
  	public void init() {
	  	Minecraft mc = Minecraft.getInstance();

	    int x = this.width / 2;
	    int y = this.height / 2 - 20;
		/*this.addButton(new SteinButton(this.width / 2 - 60, y + 75, 66, 15, new TranslationTextComponent("wildfire_gender.player_list.settings_button"), button -> {
			mc.displayGuiScreen(new WildfireSettingsScreen(SteinPlayerListScreen.this));
		}));*/

	    SOUND_LIST = new WildfireHurtSoundList(this, 118, (y - 61), (y + 71), aPlr.getHurtSounds());
		SOUND_LIST.setRenderBackground(false);
		SOUND_LIST.setRenderTopAndBottom(false);
	    this.addRenderableWidget(this.SOUND_LIST);

		this.addWidget(new WildfireButton(this.width / 2 + 53, y - 74, 9, 9, Component.translatable("wildfire_gender.label.exit"), button -> Minecraft.getInstance().setScreen(this.parent)));

	    this.TXTR_BACKGROUND = new ResourceLocation(WildfireGender.MODID, "textures/gui/player_list.png");

	    super.init();
  	}

	public void setHurtSound(HurtSound sound) {
		if (aPlr.updateHurtSounds(sound)) {
			if (sound.getSnd() != null) {
				client.player.playSound(sound.getSnd(),1, 1);
			}
			GenderPlayer.saveGenderInfo(aPlr);
		}
	}

	@Override
	public void render(@Nonnull PoseStack m, int f1, int f2, float f3) {
		super.renderBackground(m);
		Minecraft mc = Minecraft.getInstance();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		if (this.TXTR_BACKGROUND != null) {
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, this.TXTR_BACKGROUND);
		}
		int i = (this.width - 132) / 2;
		int j = (this.height - 156) / 2 - 20;
		blit(m, i, j, 0, 0, 192, 174);

		int x = (this.width / 2);
		int y = (this.height / 2) - 20;

		super.render(m, f1, f2, f3);

		double scale = mc.getWindow().getGuiScale();
		int left = x - 59;
		int bottom = y - 32;
		int width = 118;
		int height = 134;
		RenderSystem.enableScissor((int) (left * scale), (int) (bottom * scale),
				(int) (width * scale), (int) (height * scale));

		SOUND_LIST.render(m, f1, f2, f3);
		RenderSystem.disableScissor();

		this.font.draw(m, Component.translatable("wildfire_gender.tooltip.hurt_sounds"), x - 60, y - 73, 4473924);
	}
}