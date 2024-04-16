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
import com.wildfire.gui.WildfirePlayerList;
import com.wildfire.main.GenderPlayer;
import com.wildfire.main.HurtSound;
import com.wildfire.main.WildfireGender;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.UUID;


public class WildfirePlayerListScreen extends Screen {

	private static final UUID CREATOR_UUID = UUID.fromString("33c937ae-6bfc-423e-a38e-3a613e7c1256");
	private ResourceLocation TXTR_BACKGROUND;
	private static final ResourceLocation TXTR_RIBBON = new ResourceLocation(WildfireGender.MODID, "textures/bc_ribbon.png");

	@Nullable
	private Component tooltip = null;

 	public static GenderPlayer HOVER_PLAYER;

	WildfirePlayerList PLAYER_LIST;
	private Minecraft client;
	public WildfirePlayerListScreen(Minecraft mc) {
		super(Component.translatable("wildfire_gender.player_list.title"));
		this.client = mc;
		Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(true);
	}

	@Override
	public void onClose() {
		Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(false);
		super.onClose();
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

	    PLAYER_LIST = new WildfirePlayerList(this, 118, (y - 61), (y + 71));
		PLAYER_LIST.setRenderBackground(false);
		PLAYER_LIST.setRenderTopAndBottom(false);
	    this.addWidget(this.PLAYER_LIST);

		this.addRenderableWidget(new WildfireButton(this.width / 2 + 52, y - 74, 9, 9, Component.translatable("wildfire_gender.label.exit"), button -> Minecraft.getInstance().setScreen(null)));

	    this.TXTR_BACKGROUND = new ResourceLocation(WildfireGender.MODID, "textures/gui/player_list.png");

	    super.init();
  	}

	@Override
	public void render(@Nonnull PoseStack m, int f1, int f2, float f3) {
		HOVER_PLAYER = null;
		this.setTooltip(null);
		PLAYER_LIST.refreshList();


	    super.renderBackground(m);
		Minecraft mc = Minecraft.getInstance();
	    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	    if(this.TXTR_BACKGROUND != null) {
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
        RenderSystem.enableScissor((int)(left  * scale), (int) (bottom * scale),
				(int)(width * scale), (int) (height * scale));

	    PLAYER_LIST.render(m, f1, f2, f3);
	  	RenderSystem.disableScissor();

	    if(HOVER_PLAYER != null) {
			int dialogX = x + 75;
			int dialogY = y - 73;
			Player pEntity = mc.level.getPlayerByUUID(HOVER_PLAYER.uuid);
			if(pEntity != null) {
				this.font.drawShadow(m, pEntity.getDisplayName().copy().withStyle(ChatFormatting.UNDERLINE), dialogX, dialogY - 2, 0xFFFFFF);

				this.font.drawShadow(m, Component.translatable("wildfire_gender.player_list.gender", HOVER_PLAYER.getPronouns()), dialogX, dialogY + 10, HOVER_PLAYER.getPronounColorOnTick(pEntity.tickCount));
				this.font.drawShadow(m, Component.translatable("wildfire_gender.wardrobe.slider.breast_size", Math.round(HOVER_PLAYER.getBustSize() * 100)), dialogX, dialogY + 20, 0xBBBBBB);
				this.font.drawShadow(m, Component.translatable("wildfire_gender.char_settings.physics", Component.translatable(HOVER_PLAYER.hasBreastPhysics() ? "wildfire_gender.label.enabled" : "wildfire_gender.label.disabled")), dialogX, dialogY + 40, 0xBBBBBB);
				this.font.drawShadow(m, Component.translatable("wildfire_gender.player_list.bounce_multiplier", HOVER_PLAYER.getBounceMultiplier()), dialogX + 6, dialogY + 50, 0xBBBBBB);
				this.font.drawShadow(m, Component.translatable("wildfire_gender.player_list.breast_momentum", Math.round(HOVER_PLAYER.getFloppiness() * 100)), dialogX + 6, dialogY + 60, 0xBBBBBB);

				HurtSound hs = HOVER_PLAYER.getHurtSounds();
				this.font.drawShadow(m, Component.translatable("wildfire_gender.player_list.female_sounds", Component.literal(hs.getName())), dialogX, dialogY + 80, 0xBBBBBB);
				if (HOVER_PLAYER.isBilkable())
					this.font.drawShadow(m, Component.translatable("wildfire_gender.player_list.bilkable"), dialogX, dialogY + 90, 0xBBBBBB);
				WardrobeBrowserScreen.drawEntityOnScreen(x - 110, y + 45, 45, (x - 300), (y - 26 - f2), pEntity);
			}
		}

	    this.font.draw(m, Component.translatable("wildfire_gender.player_list.title"), x - 60, y - 73, 4473924);

		boolean withCreator = false;
		PlayerInfo[] playersC = this.minecraft.getConnection().getOnlinePlayers().toArray(new PlayerInfo[0]);

		for (PlayerInfo loadedPlayer : playersC) {
			if (loadedPlayer.getProfile().getId().equals(CREATOR_UUID)) {
				withCreator = true;
			}
		}

		if(withCreator) {
			drawCenteredString(m, this.font, Component.translatable("wildfire_gender.label.with_creator"), this.width / 2, y + 100, 0xFF00FF);
		}

		//Breast Cancer Awareness Month Donation Prompt
		if(Calendar.getInstance().get(Calendar.MONTH) == Calendar.OCTOBER) {
			fill(m, x - 159, y + 106, x + 159, y + 136, 0x55000000);
			font.draw(m, ChatFormatting.ITALIC + "Hey, it's Breast Cancer Awareness Month!", this.width / 2 - 155, y + 110, 0xFFFFFF);
			font.draw(m, "Click here to donate to " + ChatFormatting.LIGHT_PURPLE + "Susan G. Komen Foundation" + ChatFormatting.WHITE + "!", this.width / 2 - 155, y + 124, 0xAAAAAA);
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, this.TXTR_RIBBON);
			Screen.blit(m, x + 130, y + 109, 26, 26, 0, 0, 20, 20, 20, 20);
		}
		if (tooltip != null) {
			this.renderTooltip(m, tooltip, f1, f2);
		}
  	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		int x = (this.width / 2);
		int y = (this.height / 2) - 20;

		if(mouseX > x - 159 && mouseY > y + 106 && mouseX < x + 159 && mouseY < y + 136) {
			this.client.setScreen(new ConfirmLinkScreen((bool) -> {
				if (bool) {
					Util.getPlatform().openUri("https://www.komen.org/how-to-help/donate/");
				}
				this.client.setScreen(this);
			}, "https://www.komen.org/how-to-help/donate/", true));
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	public void setTooltip(@Nullable Component tooltip) {
  		this.tooltip = tooltip;
	}

    @Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

		return super.keyPressed(keyCode, scanCode, modifiers);
  	}

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state) {

	    return super.mouseReleased(mouseX, mouseY, state);
  	}
}