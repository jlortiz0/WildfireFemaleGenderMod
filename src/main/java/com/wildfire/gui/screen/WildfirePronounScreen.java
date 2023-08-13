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
import com.wildfire.gui.WildfireColorButton;
import com.wildfire.main.GenderPlayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

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
		this.addSelectableChild(this.textFieldWidget);
		this.setInitialFocus(this.textFieldWidget);

		this.addDrawableChild(new WildfireButton(this.width / 2 - 100, y + 110, 200, 20, new TranslatableText("wildfire_gender.acknowledge.cancel"),
				button -> MinecraftClient.getInstance().setScreen(parent)));
		this.addDrawableChild(new WildfireButton(this.width / 2 - 100, y + 84, 200, 20, new TranslatableText("wildfire_gender.acknowledge.confirm"),
				button -> {
					if (aPlr.updatePronouns(textFieldWidget.getText())) {
						GenderPlayer.saveGenderInfo(aPlr);
					}
					MinecraftClient.getInstance().setScreen(parent);
				}));

		for (int i = 1; i < 16; i++) {
			Formatting f = Formatting.byColorIndex(i);
			this.addDrawableChild(new WildfireColorButton(x - 176 + i * 21, y + 32, 18, this::setColor, f.getColorValue()));
		}
		// Trans
		this.addDrawableChild(new WildfireColorButton(x - 160, y + 53, 22, this::setColor, 0x6ac2e4, 0xeb92ea, 0xffffff, 0xeb92ea, 0x6ac2e4));
		// NB
		this.addDrawableChild(new WildfireColorButton(x - 137, y + 53, 22, this::setColor, 0xebf367, 0xffffff, 0x7210bc, 0x333233));
		// Agender
		this.addDrawableChild(new WildfireColorButton(x - 114, y + 53, 22, this::setColor, 0x222222, 0xbcc4c7, 0xffffff, 0xb7f684, 0xffffff, 0xbcc4c7, 0x222222));
		// Genderqueer
		this.addDrawableChild(new WildfireColorButton(x - 91, y + 53, 22, this::setColor, 0xca78ef, 0xffffff, 0x2db418));
		// Genderfluid
		this.addDrawableChild(new WildfireColorButton(x - 68, y + 53, 22, this::setColor, 0xfbacf9, 0xffffff, 0x9c2bd0, 0x333233, 0x2f4dd8));
		// Demiboy
		this.addDrawableChild(new WildfireColorButton(x - 45, y + 53, 22, this::setColor, 0x7f7f7f, 0x9a9fa1, 0xa9ffff, 0xffffff, 0xa9ffff, 0x9a9fa1, 0x7f7f7f));
		// Demigirl
		this.addDrawableChild(new WildfireColorButton(x - 22, y + 53, 22, this::setColor, 0x7f7f7f, 0x9a9fa1, 0xfcb1ff, 0xffffff, 0xfcb1ff, 0x9a9fa1, 0x7f7f7f));
		// Bigender
		this.addDrawableChild(new WildfireColorButton(x + 1, y + 53, 22, this::setColor, 0xc479a2, 0xeda5cd, 0xd6c7e8, 0xffffff, 0xd6c7e8, 0x9ac7e8, 0x6d82d1));
		// Intersex
		this.addDrawableChild(new WildfireColorButton(x + 24, y + 53, 22, this::setColor, 0xd8abcf, 0xffffff, 0xa0cdee, 0xf0b7d6, 0xffffff, 0xd8abcf));
		// Pride
		this.addDrawableChild(new WildfireColorButton(x + 47, y + 53, 22, this::setColor, 0xe40303, 0xff8c00, 0xffed00, 0x008026, 0x24408e, 0x732982));
		// Genderfae
		this.addDrawableChild(new WildfireColorButton(x + 70, y + 53, 22, this::setColor, 0x97c3a5, 0xc3deae, 0xf9facd, 0xffffff, 0xfca2c4, 0xdb8ae4, 0xa97edd));
		// Genderfaun
		this.addDrawableChild(new WildfireColorButton(x + 93, y + 53, 22, this::setColor, 0xfcc689, 0xfff09d, 0xfbf9cc, 0xffffff, 0x8edfd8, 0x8dabdc, 0x9781eb));
		// Genderflux
		this.addDrawableChild(new WildfireColorButton(x + 116, y + 53, 22, this::setColor, 0xf57694, 0xf2a3b9, 0xcfcfcf, 0x7be1f5, 0x3ecdfa, 0xfff48c));
		// Pangender
		this.addDrawableChild(new WildfireColorButton(x + 139, y + 53, 22, this::setColor, 0xfff798, 0xffddcd, 0xffebfb, 0xffffff, 0xffebfb, 0xffddcd, 0xfff798));

		super.init();
	}

	public void setColor(ButtonWidget btn) {
		if (!(btn instanceof WildfireColorButton button)) return;
		int[] colors = button.getColors();
		if (aPlr.updatePronounColor(colors)) {
			GenderPlayer.saveGenderInfo(aPlr);
		}
	}

	@Override
	public void render(MatrixStack m, int f1, int f2, float f3) {
		super.renderBackground(m);
		MinecraftClient mc = MinecraftClient.getInstance();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		int x = (this.width / 2);
		int y = (this.height / 2);

		textFieldWidget.setEditableColor(aPlr.getPronounColorOnTick(mc.player.age));
		this.textFieldWidget.render(m, f1, f2, f3);
		fill(m, x - 161, y, x + 162, y + 48, 0x55000000);
		super.render(m, f1, f2, f3);

		this.textRenderer.draw(m, new TranslatableText("wildfire_gender.pronouns_list.title"), x - 40, 20, 16777215);
	}
}