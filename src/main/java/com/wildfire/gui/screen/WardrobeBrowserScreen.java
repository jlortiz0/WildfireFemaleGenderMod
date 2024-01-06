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

import com.wildfire.gui.WildfirePronounButton;
import com.wildfire.main.WildfireGender;
import java.util.UUID;

import com.wildfire.gui.WildfireButton;
import com.wildfire.main.GenderPlayer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.joml.Vector3f;
import org.joml.Quaternionf;

public class WardrobeBrowserScreen extends BaseWildfireScreen {

	private Identifier BACKGROUND;

	public WardrobeBrowserScreen(Screen parent, UUID uuid) {
		super(Text.translatable("wildfire_gender.wardrobe.title"), parent, uuid);
	}

	@Override
	public void init() {
		int j = this.height / 2;

		GenderPlayer plr = getPlayer();

		this.addDrawableChild(new WildfirePronounButton(this.width / 2 - 42, j - 52, 158, 20, getGenderLabel(plr),
				button -> MinecraftClient.getInstance().setScreen(new WildfirePronounScreen(WardrobeBrowserScreen.this, this.playerUUID)), plr::getPronounColorOnTick));

		this.addDrawableChild(new WildfireButton(this.width / 2 - 42, j - 32, 158, 20, Text.translatable("wildfire_gender.breast_settings.title").append("..."),
				button -> MinecraftClient.getInstance().setScreen(new WildfireBreastCustomizationScreen(WardrobeBrowserScreen.this, this.playerUUID))));

		this.addDrawableChild(new WildfireButton(this.width / 2 - 42, j + 28, 158, 20, Text.translatable("wildfire_gender.char_settings.title").append("..."),
				button -> MinecraftClient.getInstance().setScreen(new WildfireCharacterSettingsScreen(WardrobeBrowserScreen.this, this.playerUUID))));

		if (WildfireGender.isCurseforgeNerfed) {
			this.addDrawableChild(new WildfireButton(this.width / 2 - 42, j - 12, 158, 20, Text.translatable("wildfire_gender.bulge_settings_disabled").formatted(Formatting.UNDERLINE),
					button -> this.client.setScreen(new ConfirmLinkScreen((bool) -> {
						if (bool) {
							Util.getOperatingSystem().open(WildfireGender.GITHUB_LINK);
						}
						this.client.setScreen(this);
					}, WildfireGender.GITHUB_LINK, false))));
		} else {
			this.addDrawableChild(new WildfireButton(this.width / 2 - 42, j - 12, 158, 20, Text.translatable("wildfire_gender.bulge_settings.title").append("..."),
					button -> MinecraftClient.getInstance().setScreen(new WildfireBulgeCustomizationScreen(WardrobeBrowserScreen.this, this.playerUUID))));
		}

		this.addDrawableChild(new WildfireButton(this.width / 2 - 42, j + 8, 158, 20, Text.translatable("wildfire_gender.buns_settings.title").append("..."),
				button -> MinecraftClient.getInstance().setScreen(new WildfireBunsCustomizationScreen(WardrobeBrowserScreen.this, this.playerUUID))));

		this.addDrawableChild(new WildfireButton(this.width / 2 + 111, j - 63, 9, 9, Text.translatable("wildfire_gender.label.exit"),
				button -> MinecraftClient.getInstance().setScreen(parent)));

		this.BACKGROUND = new Identifier(WildfireGender.MODID, "textures/gui/wardrobe_bg.png");

		super.init();
	}

	private Text getGenderLabel(GenderPlayer plr) {
		return Text.translatable("wildfire_gender.label.gender").append(" - ").append(plr.getPronouns());
	}

	@Override
	public void render(DrawContext ctx, int f1, int f2, float f3) {
		MinecraftClient minecraft = MinecraftClient.getInstance();
		GenderPlayer plr = getPlayer();
		super.renderBackground(ctx);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		int i = (this.width - 248) / 2;
		int j = (this.height - 134) / 2;
		ctx.drawTexture(BACKGROUND, i, j, 0, 0, 248, 156);

		if(plr == null) return;


		int x = this.width / 2;
		int y = this.height / 2;

		ctx.drawText(textRenderer, title, x - 42, y - 62, 4473924, false);

		try {
			RenderSystem.setShaderColor(1f, 1.0F, 1.0F, 1.0F);
			int xP = this.width / 2 - 82;
			int yP = this.height / 2 + 32;
			PlayerEntity ent = MinecraftClient.getInstance().world.getPlayerByUuid(this.playerUUID);
			if(ent != null) {
				drawEntityOnScreen(xP, yP, 45, (xP - f1), (yP - 76 - f2), MinecraftClient.getInstance().world.getPlayerByUuid(this.playerUUID));
			} else {
				//player left, fallback
				minecraft.setScreen(new WildfirePlayerListScreen(minecraft));
			}
		} catch(Exception e) {
			//error, fallback
			minecraft.setScreen(new WildfirePlayerListScreen(minecraft));
		}
		super.render(ctx, f1, f2, f3);
	}

	public static void drawEntityOnScreen(int x, int y, int size, float mouseX, float mouseY, LivingEntity entity) {
		drawEntityOnScreen(x, y, size, mouseX, mouseY, entity, 180.0f);
	}

	public static void drawEntityOnScreen(int x, int y, int size, float mouseX, float mouseY, LivingEntity entity, float initialYaw) {
		float f = (float)Math.atan((mouseX / 40.0F));
		float g = (float)Math.atan((mouseY / 40.0F));
		MatrixStack matrixStack = RenderSystem.getModelViewStack();
		matrixStack.push();
		matrixStack.translate(x, y, 1050.0D);
		matrixStack.scale(1.0F, 1.0F, -1.0F);
		RenderSystem.applyModelViewMatrix();
		MatrixStack matrixStack2 = new MatrixStack();
		matrixStack2.translate(0.0D, 0.0D, 800.0D);
		matrixStack2.scale((float)size, (float)size, (float)size);
		Quaternionf quaternion = new Quaternionf().rotateZ((float) Math.PI);
		Quaternionf quaternion2 = (new Quaternionf()).rotateX(g * (float) Math.PI / 9);
		quaternion.mul(quaternion2);
		matrixStack2.multiply(quaternion);
		float h = entity.bodyYaw;
		float i = entity.getYaw();
		float j = entity.getPitch();
		float k = entity.prevHeadYaw;
		float l = entity.headYaw;
		boolean m = entity.isInvisibleTo(MinecraftClient.getInstance().player);
		entity.bodyYaw = initialYaw + f * 20.0F;
		entity.setYaw(initialYaw + f * 40.0F);
		entity.setPitch(-g * 20.0F);
		entity.headYaw = entity.getYaw();
		entity.prevHeadYaw = entity.getYaw();
		entity.setInvisible(false);
		DiffuseLighting.method_34742();
		EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
		quaternion2.conjugate();
		entityRenderDispatcher.setRotation(quaternion2);
		entityRenderDispatcher.setRenderShadows(false);
		VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
		RenderSystem.runAsFancy(() -> entityRenderDispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixStack2, immediate, 15728880));
		immediate.draw();
		entityRenderDispatcher.setRenderShadows(true);
		entity.bodyYaw = h;
		entity.setYaw(i);
		entity.setPitch(j);
		entity.prevHeadYaw = k;
		entity.headYaw = l;
		entity.setInvisible(m);
		matrixStack.pop();
		RenderSystem.applyModelViewMatrix();
		DiffuseLighting.enableGuiDepthLighting();
	}
}