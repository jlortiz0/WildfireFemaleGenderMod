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
import com.wildfire.gui.WildfireButton;
import com.wildfire.main.GenderPlayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.text.Normalizer;
import java.util.UUID;


public class WildfirePronounScreen extends BaseWildfireScreen {
	private Identifier TXTR_BACKGROUND;

	private TextFieldWidget textFieldWidget;
	private final GenderPlayer aPlr;

	public WildfirePronounScreen(Screen parent, UUID plr) {
		super(new TranslatableText("wildfire_gender.player_list.female_sounds"), parent, plr);
		aPlr = getPlayer();
	}

	@Override
	public void init() {
		int x = this.width / 2;
		int y = this.height / 2 - 30;

		textFieldWidget = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, y, 200, 20, new TranslatableText("wildfire_gender.pronoun_list.title"));
		textFieldWidget.setMaxLength(16);
		textFieldWidget.setTextFieldFocused(true);
		textFieldWidget.setText(aPlr.getPronouns());
		textFieldWidget.setEditableColor(aPlr.getPronounColor().getColorValue());
		this.addSelectableChild(this.textFieldWidget);
		this.setInitialFocus(this.textFieldWidget);

		this.addDrawableChild(new WildfireButton(this.width / 2 - 100, (this.height * 3) / 4, 200, 20, new TranslatableText("wildfire_gender.acknowledge.cancel"),
				button -> MinecraftClient.getInstance().setScreen(parent)));
		this.addDrawableChild(new WildfireButton(this.width / 2 - 100, (this.height * 3) / 4 - 22, 200, 20, new TranslatableText("wildfire_gender.acknowledge.confirm"),
				button -> {
					aPlr.updatePronouns(textFieldWidget.getText());
					MinecraftClient.getInstance().setScreen(parent);
				}));

		for (int i = 1; i < 16; i++) {
			Formatting f = Formatting.byColorIndex(i);
			this.addDrawableChild(new WildfireColorButton(x - 178 + i * 21, y + 32, 19, f, button -> this.setColor(f)));
		}

		super.init();
	}

	public void setColor(Formatting f) {
		if (aPlr.updatePronounColor(f)) {
			textFieldWidget.setEditableColor(f.getColorValue());
		}
	}

	@Override
	public void render(MatrixStack m, int f1, int f2, float f3) {
		super.renderBackground(m);
		MinecraftClient mc = MinecraftClient.getInstance();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		if(this.TXTR_BACKGROUND != null) {
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, this.TXTR_BACKGROUND);
			int i = (this.width - 132) / 2;
			int j = (this.height - 156) / 2 - 20;
			drawTexture(m, i, j, 0, 0, 192, 174);
		}

		int x = (this.width / 2);
		int y = (this.height / 2);

		this.textFieldWidget.render(m, f1, f2, f3);
		fill(m, x - 160, y, x + 160, y + 24, 0x55000000);
		super.render(m, f1, f2, f3);

		this.textRenderer.draw(m, new TranslatableText("wildfire_gender.pronouns_list.title"), x - 40, 20, 16777215);
	}
}