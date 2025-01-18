/*
    Wildfire's Female Gender Mod is a female gender mod created for Minecraft.
    Copyright (C) 2023 WildfireRomeo

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.wildfire.gui.screen;

import com.wildfire.gui.GuiUtils;
import com.wildfire.gui.WildfireButton;
import com.wildfire.gui.WildfirePronounButton;
import com.wildfire.main.WildfireGender;
import com.wildfire.main.entitydata.PlayerConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Objects;
import java.util.UUID;

@Environment(EnvType.CLIENT)
public class WardrobeBrowserScreen extends BaseWildfireScreen {
	private static final Identifier BACKGROUND = Identifier.of(WildfireGender.MODID, "textures/gui/wardrobe_bg.png");


	public WardrobeBrowserScreen(Screen parent, UUID uuid) {
		super(Text.translatable("wildfire_gender.wardrobe.title"), parent, uuid);
	}

	@Override
  	public void init() {
	    int y = this.height / 2;
		PlayerConfig plr = Objects.requireNonNull(getPlayer(), "getPlayer()");

		this.addDrawableChild(new WildfirePronounButton(this.width / 2 - 42, y - 52, 158, 20, getPronounsLabel(plr.getPronouns()),
			button -> client.setScreen(new WildfirePronounScreen(WardrobeBrowserScreen.this, this.playerUUID)), plr::getPronounColor));

		this.addDrawableChild(new WildfireButton(this.width / 2 - 42, this.height / 2 - 32, 158, 20, Text.translatable("wildfire_gender.appearance_settings.title").append("..."),
				button -> client.setScreen(new WildfireBreastCustomizationScreen(WardrobeBrowserScreen.this, this.playerUUID))));

		this.addDrawableChild(new WildfireButton(this.width / 2 - 42, y - 12, 158, 20, Text.translatable("wildfire_gender.char_settings.title").append("..."),
				button -> client.setScreen(new WildfireCharacterSettingsScreen(WardrobeBrowserScreen.this, this.playerUUID))));

		this.addDrawableChild(new WildfireButton(this.width / 2 + 111, y - 63, 9, 9, Text.literal("X"),
				button -> client.setScreen(parent)));

	    super.init();
  	}

	private Text getPronounsLabel(String pronouns) {
		return Text.translatable("wildfire_gender.label.pronouns").append(" - ").append(pronouns);
	}

	@Override
	public void renderBackground(DrawContext ctx, int mouseX, int mouseY, float delta) {
		super.renderBackground(ctx, mouseX, mouseY, delta);
		PlayerConfig plr = getPlayer();
		if(plr == null) return;

		ctx.drawTexture(BACKGROUND, (this.width - 248) / 2, (this.height - 134) / 2, 0, 0, 256, 124, 256, 256);

		if(client != null && client.world != null) {
			int xP = this.width / 2 - 82;
			int yP = this.height / 2 + 32;
			PlayerEntity ent = client.world.getPlayerByUuid(this.playerUUID);
			if(ent != null) {
				GuiUtils.drawEntityOnScreen(ctx, xP, yP, 45, (xP - mouseX), (yP - 46 - mouseY), ent);
			} else {
				//player left, fallback
				close();
			}
		}
	}

	@Override
	public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
		super.render(ctx, mouseX, mouseY, delta);
		int x = this.width / 2;
	    int y = this.height / 2;
		ctx.drawText(textRenderer, title, x - 42, y - 62, 4473924, false);
	}
}