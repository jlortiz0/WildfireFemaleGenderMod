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
import com.wildfire.api.IWildfireSound;
import com.wildfire.gui.WildfireButton;
import com.wildfire.gui.WildfireSoundList;
import com.wildfire.main.GenderPlayer;
import com.wildfire.main.WildfireGender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

public class WildfireSoundListScreen<T extends IWildfireSound> extends Screen {

	private ResourceLocation TXTR_BACKGROUND;
 	public static GenderPlayer aPlr;
	WildfireSoundList<T> SOUND_LIST;
	private final Minecraft client;
	private final BaseWildfireScreen parent;
	private final Function<GenderPlayer, T> current;
	private final BiFunction<GenderPlayer, T, Boolean> callback;

	public WildfireSoundListScreen(Minecraft mc, UUID plr, BaseWildfireScreen parent, Function<GenderPlayer, T> current, BiFunction<GenderPlayer, T, Boolean> callback) {
		super(Component.translatable("wildfire_gender.tooltip.hurt_sounds"));
		aPlr = WildfireGender.getPlayerById(plr);
		client = mc;
		this.parent = parent;
		this.current = current;
		this.callback = callback;
	}

	@Override
	public boolean isPauseScreen() { return false; }

	@Override
  	public void init() {
	    int x = this.width / 2;
	    int y = this.height / 2 - 20;
		/*this.addButton(new SteinButton(this.width / 2 - 60, y + 75, 66, 15, new TranslationTextComponent("wildfire_gender.player_list.settings_button"), button -> {
			mc.displayGuiScreen(new WildfireSettingsScreen(SteinPlayerListScreen.this));
		}));*/

	    SOUND_LIST = new WildfireSoundList<>(this, 118, (y - 61), (y + 70), current.apply(aPlr));
		SOUND_LIST.setRenderBackground(false);
		SOUND_LIST.setRenderTopAndBottom(false);
	    this.addWidget(this.SOUND_LIST);
		this.addRenderableWidget(new WildfireButton(x + 52, y - 74, 9, 9, Component.translatable("wildfire_gender.label.exit"), button -> Minecraft.getInstance().setScreen(this.parent)));

	    this.TXTR_BACKGROUND = new ResourceLocation(WildfireGender.MODID, "textures/gui/player_list.png");

	    super.init();
  	}

	public void setSound(T sound) {
		if (callback.apply(aPlr, sound)) {
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
		int bottom = y - 31;
		int width = 125;
		int height = 134;
		RenderSystem.enableScissor((int) (left * scale), (int) (bottom * scale),
				(int) (width * scale), (int) (height * scale));

		SOUND_LIST.render(m, f1, f2, f3);
		RenderSystem.disableScissor();

		this.font.draw(m, Component.translatable("wildfire_gender.tooltip.hurt_sounds"), x - 60, y - 73, 4473924);
	}
}