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

package com.wildfire.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.wildfire.api.IGenderArmor;
import com.wildfire.main.*;
import com.wildfire.physics.BreastPhysics;
import com.wildfire.render.WildfireModelRenderer.BreastModelBox;
import com.wildfire.render.WildfireModelRenderer.OverlayModelBox;
import com.wildfire.render.WildfireModelRenderer.PositionTextureVertex;
import java.util.UUID;
import javax.annotation.Nonnull;

import com.wildfire.render.WildfireModelRenderer.BulgeModelBox;
import com.wildfire.render.WildfireModelRenderer.BunModelBox;
import com.wildfire.render.armor.EmptyGenderArmor;
import dev.emi.trinkets.api.SlotGroup;
import dev.emi.trinkets.api.SlotType;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.TrinketInventory;
import io.github.apace100.apoli.power.ModelColorPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginLayers;
import io.github.apace100.origins.registry.ModComponents;
import moe.kawaaii.TransparentCosmetics.TransparentArmorMaterial;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class GenderLayer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

	private BreastModelBox lBreast, rBreast;
	private OverlayModelBox lBreastWear, rBreastWear;
	private BreastModelBox lBoobArmor, rBoobArmor;
	private BulgeModelBox bulgeModel, bulgeModelArmor;
	private OverlayModelBox bulgeWear;
	private BunModelBox lBun, rBun;
	private OverlayModelBox lBunWear, rBunWear;
	private BreastModelBox lBunArmor, rBunArmor;

	private float preBreastSize = 0f;
	private float preBulgeSize = 0f;
	private float preBunSize = 0f;

	private ModelColorPower modelColor = null;
	private Identifier lastOrigin = null;

	public GenderLayer(FeatureRendererContext render) {
		super(render);

		lBreast = new BreastModelBox(64, 64, 16, 17, -4F, 0.0F, 0F, 4, 5, 4, 0.0F, false);
		rBreast = new BreastModelBox(64, 64, 20, 17, 0, 0.0F, 0F, 4, 5, 4, 0.0F, false);
		lBreastWear = new OverlayModelBox(true,64, 64, 17, 34, -4F, 0.0F, 0F, 4, 5, 3, 0.0F, false);
		rBreastWear = new OverlayModelBox(false,64, 64, 21, 34, 0, 0.0F, 0F, 4, 5, 3, 0.0F, false);

		lBoobArmor = new BreastModelBox(64, 32, 16, 17, -4F, 0.0F, 0F, 4, 5, 3, 0.0F, false);
		rBoobArmor = new BreastModelBox(64, 32, 20, 17, 0, 0.0F, 0F, 4, 5, 3, 0.0F, false);

		bulgeModel = new BulgeModelBox(64, 64, 5, 20, -1F, 0.0F, 0F, 2, 2, 2, 0.0F, false);
		bulgeWear = new OverlayModelBox(false, 64, 64, 20, 41, -1F, 0F, 0F, 2, 2, 2, 0F, false);
		bulgeModelArmor = new BulgeModelBox(64, 32, 5, 20, -1F, 0.0F, 0F, 2, 2, 2, 0.0F, false);

		lBun = new BunModelBox(64, 64, 28, 24, -4F, 0.0F, 0F, 4, 4, 4, 0.0F, false);
		rBun = new BunModelBox(64, 64, 32, 24, 0, 0.0F, 0F, 4, 4, 4, 0.0F, true);
		lBunWear = new OverlayModelBox(true,64, 64, 0, 34, -4F, 0.0F, 0F, 4, 4, 3, 0.0F, false);
		rBunWear = new OverlayModelBox(false,64, 64, 0, 34, 0, 0.0F, 0F, 4, 4, 3, 0.0F, true);

		lBunArmor = new BreastModelBox(64, 32, 0, 16, -4F, 0.0F, 0F, 4, 4, 3, 0.0F, false);
		rBunArmor = new BreastModelBox(64, 32, 0, 16, 0, 0.0F, 0F, 4, 4, 3, 0.0F, false);
	}

	private static final Map<String, Identifier> ARMOR_LOCATION_CACHE = new HashMap<>();

	private static final Map<String, Identifier> ARMOR_TEXTURE_CACHE = new HashMap<String, Identifier>();

	public Identifier getArmorResource(ArmorItem item, boolean legs, @Nullable String overlay) {

		String string = "textures/models/armor/" + item.getMaterial().getName() + "_layer_" + (legs ? 2 : 1) + (overlay == null ? "" : "_" + overlay) + ".png";
		return (Identifier) ARMOR_TEXTURE_CACHE.computeIfAbsent(string, Identifier::new);
	}

	@Override
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int packedLightIn, @Nonnull AbstractClientPlayerEntity ent, float limbAngle,
					   float limbDistance, float partialTicks, float animationProgress, float headYaw, float headPitch) {
		if (ent.isInvisibleTo(MinecraftClient.getInstance().player)) {
			//Exit early if the entity shouldn't actually be seen
			return;
		}
		//Surround with a try/catch to fix for essential mod.
		try {
			//0.5 or 0
			UUID playerUUID = ent.getUuid();
			//System.out.println(playerUUID);
			GenderPlayer plr = WildfireGender.getPlayerById(playerUUID);
			if(plr == null) return;

			ItemStack armorStack = ent.getEquippedStack(EquipmentSlot.CHEST);

			//for(String val : TrinketsApi.getTrinketComponent(MinecraftClient.getInstance().player).get().getInventory().get("chest").get("cosmetic")) {
			//System.out.println(TrinketsApi.getTrinketComponent(MinecraftClient.getInstance().player).get().getInventory().get("chest").get("cosmetic").getStack(0));
			//}
			//System.out.println(TrinketsApi.getTrinketComponent(MinecraftClient.getInstance().player).get().getInventory().keySet());

			//Note: When the stack is empty the helper will fall back to an implementation that returns the proper data
			IGenderArmor armorConfig = WildfireHelper.getArmorConfig(armorStack);


			//Cosmetic armor
			if (FabricLoader.getInstance().isModLoaded("trinkets")) {
				Map<String, TrinketInventory> tm = TrinketsApi.getTrinketComponent(MinecraftClient.getInstance().player).get().getInventory().get("chest");
				TrinketInventory ti = null;
				if (tm != null) {
					ti = tm.get("cosmetic");
				}
				if (ti != null && ti.getStack(0).getItem() != Items.AIR) {
					armorStack = ti.getStack(0);
					armorConfig = WildfireHelper.getArmorConfig(armorStack);
				}
			}

			boolean isChestplateOccupied = armorConfig.coversBreasts();
			if (armorConfig.alwaysHidesBreasts() || !plr.showBreastsInArmor() && isChestplateOccupied) {
				//If the armor always hides breasts or there is armor and the player configured breasts
				// to be hidden when wearing armor, we can just exit early rather than doing any calculations
				return;
			}
			ItemStack armorStack2 = ent.getEquippedStack(EquipmentSlot.LEGS);
			IGenderArmor armorConfig2 = WildfireHelper.getArmorConfig(armorStack2);
			if (FabricLoader.getInstance().isModLoaded("trinkets")) {
				Map<String, TrinketInventory> tm = TrinketsApi.getTrinketComponent(MinecraftClient.getInstance().player).get().getInventory().get("legs");
				TrinketInventory ti = null;
				if (tm != null) {
					ti = tm.get("cosmetic");
				}
				if (ti != null && ti.getStack(0).getItem() != Items.AIR) {
					armorStack2 = ti.getStack(0);
					armorConfig2 = WildfireHelper.getArmorConfig(armorStack2);
				}
			}
			boolean isLeggingsOccupied = armorConfig2.coversBreasts();

			PlayerEntityRenderer rend = (PlayerEntityRenderer) MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(ent);
			PlayerEntityModel<AbstractClientPlayerEntity> model = rend.getModel();

			Breasts breasts = plr.getBreasts();
			float breastOffsetX = Math.round((Math.round(breasts.getXOffset() * 100f) / 100f) * 10) / 10f;
			float breastOffsetY = -Math.round((Math.round(breasts.getYOffset() * 100f) / 100f) * 10) / 10f;
			float breastOffsetZ = -Math.round((Math.round(breasts.getZOffset() * 100f) / 100f) * 10) / 10f;

			BreastPhysics leftBreastPhysics = plr.getLeftBreastPhysics();
			final float bSize = leftBreastPhysics.getBreastSize(partialTicks);
			float outwardAngle = (Math.round(breasts.getCleavage() * 100f) / 100f) * 100f;
			outwardAngle = Math.min(outwardAngle, 10);

			if (FabricLoader.getInstance().isModLoaded("origins")) {
				OriginComponent component = ModComponents.ORIGIN.get(ent);
				OriginLayer layer = OriginLayers.getLayer(new Identifier("origins", "origin"));
				Origin or = component.getOrigin(layer);
				if (lastOrigin == null || !or.getIdentifier().equals(lastOrigin)) {
					lastOrigin = or.getIdentifier();
					this.modelColor = null;
					for (PowerType<? extends Power> p : or.getPowerTypes()) {
						if (p.get(ent) instanceof ModelColorPower) {
							this.modelColor = (ModelColorPower) p.get(ent);
							break;
						}
					}
				}
			}

			float reducer = 0;
			if (bSize < 0.84f) reducer++;
			if (bSize < 0.72f) reducer++;

			if (preBreastSize != bSize) {
				lBreast = new BreastModelBox(64, 64, 16, 17, -4F, 0.0F, 0F, 4, 5, (int) (4 - breastOffsetZ - reducer), 0.0F, false);
				rBreast = new BreastModelBox(64, 64, 20, 17, 0, 0.0F, 0F, 4, 5, (int) (4 - breastOffsetZ - reducer), 0.0F, false);
				preBreastSize = bSize;
			}

			Bulge bulge = plr.getBulge();
			float bulgeOffsetX = Math.round((Math.round(bulge.getXOffset() * 100f) / 100f) * 10) / 10f;
			float bulgeOffsetY = -Math.round((Math.round(bulge.getYOffset() * 100f) / 100f) * 10) / 10f;
			float bulgeOffsetZ = -Math.round((Math.round(bulge.getZOffset() * 100f) / 100f) * 10) / 10f;
			final float buSize = bulge.getSize();
			reducer = 0;
			if (buSize < 0.84f) reducer++;
			if (buSize < 0.72f) reducer++;
			if (preBulgeSize != buSize) {
				bulgeModel = new BulgeModelBox(64, 64, 5, 20, -1F, 0.0F, 0F, 2, 2, (int) (2 - bulgeOffsetZ - reducer), 0.0F, false);
				preBulgeSize = buSize;
			}

			Buns buns = plr.getBuns();
			float bunsOffsetX = Math.round((Math.round(buns.getXOffset() * 100f) / 100f) * 10) / 10f;
			float bunsOffsetY = -Math.round((Math.round(buns.getYOffset() * 100f) / 100f) * 10) / 10f;
			float bunsOffsetZ = -Math.round((Math.round(buns.getZOffset() * 100f) / 100f) * 10) / 10f;
			final float btSize = plr.getBunsSize();
			float bOutwardAngle = (Math.round(buns.getGap() * 100f) / 100f) * 100f;
			bOutwardAngle = Math.min(bOutwardAngle, 10);
			reducer = 0;
			if (btSize < 0.84f) reducer++;
			if (btSize < 0.72f) reducer++;
			if (preBunSize != btSize) {
				lBun = new BunModelBox(64, 64, 28, 24, -4F, 0.0F, 0F, 4, 4, (int) (4 - bunsOffsetZ - reducer), 0.0F, false);
				rBun = new BunModelBox(64, 64, 32, 24, 0, 0.0F, 0F, 4, 4, (int) (4 - bunsOffsetZ - reducer), 0.0F, true);
				preBunSize = btSize;
			}

			//DEPENDENCIES
			float overlayRed = 1;
			float overlayGreen = 1;
			float overlayBlue = 1;
			//Note: We only render if the entity is not visible to the player, so we can assume it is visible to the player
			float overlayAlpha = ent.isInvisible() ? 0.15F : 1;
			if (modelColor != null) {
				overlayRed = modelColor.getRed();
				overlayGreen = modelColor.getGreen();
				overlayBlue = modelColor.getBlue();
				overlayAlpha = ent.isInvisible() ? 0.15F : modelColor.getAlpha();
			}

			RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

			float lTotal = MathHelper.lerp(partialTicks, leftBreastPhysics.getPreBounceY(), leftBreastPhysics.getBounceY());
			float lTotalX = MathHelper.lerp(partialTicks, leftBreastPhysics.getPreBounceX(), leftBreastPhysics.getBounceX());
			float leftBounceRotation = MathHelper.lerp(partialTicks, leftBreastPhysics.getPreBounceRotation(), leftBreastPhysics.getBounceRotation());
			float rTotal;
			float rTotalX;
			float rightBounceRotation;
			if (breasts.isUniboob()) {
				rTotal = lTotal;
				rTotalX = lTotalX;
				rightBounceRotation = leftBounceRotation;
			} else {
				BreastPhysics rightBreastPhysics = plr.getRightBreastPhysics();
				rTotal = MathHelper.lerp(partialTicks, rightBreastPhysics.getPreBounceY(), rightBreastPhysics.getBounceY());
				rTotalX = MathHelper.lerp(partialTicks, rightBreastPhysics.getPreBounceX(), rightBreastPhysics.getBounceX());
				rightBounceRotation = MathHelper.lerp(partialTicks, rightBreastPhysics.getPreBounceRotation(), rightBreastPhysics.getBounceRotation());
			}
			float breastSize = bSize * 1.5f;
			if (breastSize > 0.7f) breastSize = 0.7f;
			if (bSize > 0.7f) {
				breastSize = bSize;
			}
			float bulgeSize = buSize * 1.5f;
			if (bulgeSize > 0.7f) bulgeSize = 0.7f;
			if (buSize > 0.7f) {
				bulgeSize = buSize;
			}
			float bunsSize = btSize * 1.5f;
			if (bunsSize > 0.7f) bunsSize = 0.7f;
			if (buSize > 0.7f) {
				bunsSize = btSize;
			}

			if (breastSize < 0.02f && bulgeSize < 0.02f && bunsSize < 0.02f) return;
			if (breastSize >= 0.02f) breastSize = bSize + 0.5f * Math.abs(bSize - 0.7f) * 2f;
			if (bulgeSize >= 0.02f) bulgeSize = buSize + 0.5f * Math.abs(buSize - 0.7f) * 2f;
			if (bunsSize >= 0.02f) bunsSize = btSize + 0.5f * Math.abs(btSize - 0.7f) * 2f;

			float zOff = 0.0625f - (bSize * 0.0625f);
			float zBuOff = 0.0625f - (buSize * 0.0625f);
			float zBtOff = 0.0625f - (btSize * 0.0625f);

			//matrixStack.translate(0, 0, zOff);
			//System.out.println(bounceRotation);

			float resistance = MathHelper.clamp(armorConfig.physicsResistance(), 0, 1);
			//Note: We only check if the breathing animation should be enabled if the chestplate's physics resistance
			// is less than or equal to 0.5 so that if we won't be rendering it we can avoid doing extra calculations
			boolean breathingAnimation = resistance <= 0.5F &&
					(!ent.isSubmergedInWater() || StatusEffectUtil.hasWaterBreathing(ent) ||
							ent.world.getBlockState(new BlockPos(ent.getX(), ent.getEyeY(), ent.getZ())).isOf(Blocks.BUBBLE_COLUMN));
			boolean bounceEnabled = plr.hasBreastPhysics() && (!isChestplateOccupied || plr.hasArmorBreastPhysics() && resistance < 1); //oh, you found this?

			int combineTex = LivingEntityRenderer.getOverlay(ent, 0);
			RenderLayer type = RenderLayer.getEntityTranslucent(rend.getTexture(ent));
			if (breastSize >= 0.02f) {
				renderBreastWithTransforms(ent, model.body, armorStack, matrixStack, vertexConsumerProvider, type, packedLightIn, combineTex, overlayRed, overlayGreen,
						overlayBlue, overlayAlpha, bounceEnabled, lTotalX, lTotal, leftBounceRotation, breastSize, breastOffsetX, breastOffsetY, breastOffsetZ, zOff,
						outwardAngle, breasts.isUniboob(), isChestplateOccupied, breathingAnimation, true);
				renderBreastWithTransforms(ent, model.body, armorStack, matrixStack, vertexConsumerProvider, type, packedLightIn, combineTex, overlayRed, overlayGreen,
						overlayBlue, overlayAlpha, bounceEnabled, rTotalX, rTotal, rightBounceRotation, breastSize, -breastOffsetX, breastOffsetY, breastOffsetZ, zOff,
						-outwardAngle, breasts.isUniboob(), isChestplateOccupied, breathingAnimation, false);
			}
			if (bulgeSize >= 0.02f) {
				renderBulgeWithTransforms(ent, model.body, armorStack2, matrixStack, vertexConsumerProvider, type, packedLightIn, combineTex, overlayRed, overlayGreen,
						overlayBlue, overlayAlpha, bulgeOffsetX, bulgeSize, bulgeOffsetY,
						bulgeOffsetZ, zBuOff, isLeggingsOccupied);
			}
			if (bunsSize >= 0.02f) {
				renderBunWithTransforms(ent, model.body, armorStack2, matrixStack, vertexConsumerProvider, type, packedLightIn, combineTex, overlayRed, overlayGreen,
						overlayBlue, overlayAlpha, false, lTotalX, lTotal, leftBounceRotation, bunsSize, bunsOffsetX, bunsOffsetY, bunsOffsetZ, zBtOff,
						bOutwardAngle, buns.isUnibun(), isLeggingsOccupied, false, true);
				renderBunWithTransforms(ent, model.body, armorStack2, matrixStack, vertexConsumerProvider, type, packedLightIn, combineTex, overlayRed, overlayGreen,
						overlayBlue, overlayAlpha, false, rTotalX, rTotal, rightBounceRotation, bunsSize, -bunsOffsetX, bunsOffsetY, bunsOffsetZ, zBtOff,
						-bOutwardAngle, buns.isUnibun(), isLeggingsOccupied, false, false);
			}
			RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void renderBreastWithTransforms(AbstractClientPlayerEntity entity, ModelPart body, ItemStack armorStack, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider,
											RenderLayer breastRenderType, int packedLightIn, int combineTex, float red, float green, float blue, float alpha, boolean bounceEnabled, float totalX, float total,
											float bounceRotation, float breastSize, float breastOffsetX, float breastOffsetY, float breastOffsetZ, float zOff, float outwardAngle, boolean uniboob,
											boolean isChestplateOccupied, boolean breathingAnimation, boolean left) {
		matrixStack.push();
		//Surround with a try/catch to fix for essential mod.
		try {
			matrixStack.translate(body.pivotX * 0.0625f, body.pivotY * 0.0625f, body.pivotZ * 0.0625f);
			if (body.roll != 0.0F) {
				matrixStack.multiply(new Quaternion(0f, 0f, body.roll, false));
			}
			if (body.yaw != 0.0F) {
				matrixStack.multiply(new Quaternion(0f, body.yaw, 0f, false));
			}
			if (body.pitch != 0.0F) {
				matrixStack.multiply(new Quaternion(body.pitch, 0f, 0f, false));
			}

			if (bounceEnabled) {
				matrixStack.translate(totalX / 32f, 0, 0);
				matrixStack.translate(0, total / 32f, 0);
			}

			matrixStack.translate(breastOffsetX * 0.0625f, 0.05625f + (breastOffsetY * 0.0625f), zOff - 0.0625f * 2f + (breastOffsetZ * 0.0625f)); //shift down to correct position

			if (!uniboob) {
				matrixStack.translate(-0.0625f * 2 * (left ? 1 : -1), 0, 0);
			}
			if (bounceEnabled) {
				matrixStack.multiply(new Quaternion(0, bounceRotation, 0, true));
			}
			if (!uniboob) {
				matrixStack.translate(0.0625f * 2 * (left ? 1 : -1), 0, 0);
			}

			float rotationMultiplier = 0;
			if (bounceEnabled) {
				matrixStack.translate(0, -0.035f * breastSize, 0); //shift down to correct position
				rotationMultiplier = -total / 12f;
			}
			float totalRotation = breastSize + rotationMultiplier;
			if (!bounceEnabled) {
				totalRotation = breastSize;
			}
			if (totalRotation > breastSize + 0.2F) {
				totalRotation = breastSize + 0.2F;
			}
			totalRotation = Math.min(totalRotation, 1); //hard limit for MAX

			if (isChestplateOccupied) {
				matrixStack.translate(0, 0, 0.01f);
			}

			matrixStack.multiply(new Quaternion(0, outwardAngle, 0, true));
			matrixStack.multiply(new Quaternion(-35f * totalRotation, 0, 0, true));

			if (breathingAnimation) {
				float f5 = -MathHelper.cos(entity.age * 0.09F) * 0.45F + 0.45F;
				matrixStack.multiply(new Quaternion(f5, 0, 0, true));
			}

			matrixStack.scale(0.9995f, 1f, 1f); //z-fighting FIXXX

			renderBreast(entity, armorStack, matrixStack, vertexConsumerProvider, breastRenderType, packedLightIn, combineTex, red, green, blue, alpha, left);
		} catch(Exception e) {
			e.printStackTrace();
		}
		matrixStack.pop();
	}

	private void renderBreast(AbstractClientPlayerEntity entity, ItemStack armorStack, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, RenderLayer breastRenderType,
							  int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha, boolean left) {
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(breastRenderType);
		renderBox(left ? lBreast : rBreast, matrixStack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		if (entity.isPartVisible(PlayerModelPart.JACKET)) {
			matrixStack.translate(0, 0, -0.015f);
			matrixStack.scale(1.05f, 1.05f, 1.05f);
			renderBox(left ? lBreastWear : rBreastWear, matrixStack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		}
		//TODO: Eventually we may want to expose a way via the API for mods to be able to override rendering
		// be it because they are not an armor item or the way they render their armor item is custom
		//Render Breast Armor
		if (!armorStack.isEmpty() && armorStack.getItem() instanceof ArmorItem armorItem) {
			Identifier armorTexture = getArmorResource(armorItem, false, null);
			Identifier overlayTexture = null;
			float armorR = 1f;
			float armorG = 1f;
			float armorB = 1f;
			if (armorItem instanceof DyeableArmorItem dyeableItem) {
				//overlayTexture = getArmorResource(entity, armorStack, EquipmentSlot.CHEST, "overlay");
				int color = dyeableItem.getColor(armorStack);
				armorR = (float) (color >> 16 & 255) / 255.0F;
				armorG = (float) (color >> 8 & 255) / 255.0F;
				armorB = (float) (color & 255) / 255.0F;
			}
			matrixStack.push();
			matrixStack.translate(left ? 0.001f : -0.001f, 0.015f, -0.015f);
			matrixStack.scale(1.05f, 1, 1);
			WildfireModelRenderer.BreastModelBox armor = left ? lBoobArmor : rBoobArmor;
			RenderLayer armorType = RenderLayer.getArmorCutoutNoCull(armorTexture);
			VertexConsumer armorVertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, armorType, false, armorStack.hasGlint());
			renderBox(armor, matrixStack, armorVertexConsumer, packedLightIn, OverlayTexture.DEFAULT_UV, armorR, armorG, armorB, alpha);
			if (overlayTexture != null) {
				RenderLayer overlayType = RenderLayer.getArmorCutoutNoCull(overlayTexture);
				VertexConsumer overlayVertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, overlayType, false, armorStack.hasGlint());
				renderBox(armor, matrixStack, overlayVertexConsumer, packedLightIn, OverlayTexture.DEFAULT_UV, red, green, blue, alpha);
			}
			matrixStack.pop();
		}
	}
        
	private void renderBulgeWithTransforms(AbstractClientPlayerEntity entity, ModelPart body, ItemStack armorStack, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider,
										   RenderLayer breastRenderType, int packedLightIn, int combineTex, float red, float green, float blue, float alpha,
										   float bounceRotation, float breastSize, float breastOffsetY, float breastOffsetZ, float zOff,
										   boolean isChestplateOccupied) {
		matrixStack.push();
		//Surround with a try/catch to fix for essential mod.
		try {
			matrixStack.translate(body.pivotX * 0.0625f, body.pivotY * 0.0625f, body.pivotZ * 0.0625f);
			if (body.roll != 0.0F) {
				matrixStack.multiply(new Quaternion(0f, 0f, body.roll, false));
			}
			if (body.yaw != 0.0F) {
				matrixStack.multiply(new Quaternion(0f, body.yaw, 0f, false));
			}
			if (body.pitch != 0.0F) {
				matrixStack.multiply(new Quaternion(body.pitch, 0f, 0f, false));
			}


			matrixStack.translate(0, 0.70f + (breastOffsetY * 0.0625f), zOff - 0.2 + (breastOffsetZ * 0.0625f)); //shift down to correct position

			float totalRotation = breastSize * 0.75f + bounceRotation - (float)Math.PI / 2 - 0.25f;

			if (isChestplateOccupied) {
				matrixStack.translate(0, 0, 0.01f);
			}

			matrixStack.multiply(new Quaternion(-35f * totalRotation, 0, 0, true));

			matrixStack.scale(0.9995f, 1f, 1f); //z-fighting FIXXX

			renderBulge(entity, armorStack, matrixStack, vertexConsumerProvider, breastRenderType, packedLightIn, combineTex, red, green, blue, alpha);
		} catch(Exception e) {
			e.printStackTrace();
		}
		matrixStack.pop();
	}

	private void renderBulge(AbstractClientPlayerEntity entity, ItemStack armorStack, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, RenderLayer breastRenderType,
							 int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(breastRenderType);
		renderBox(bulgeModel, matrixStack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		if (entity.isPartVisible(PlayerModelPart.JACKET)) {
			matrixStack.translate(0, 0, -0.015f);
			matrixStack.scale(1.05f, 1.05f, 1.05f);
			renderBox(bulgeWear, matrixStack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		}
		//TODO: Eventually we may want to expose a way via the API for mods to be able to override rendering
		// be it because they are not an armor item or the way they render their armor item is custom
		//Render Breast Armor
		if (!armorStack.isEmpty() && armorStack.getItem() instanceof ArmorItem armorItem) {
			Identifier armorTexture = getArmorResource(armorItem, true, null);
			Identifier overlayTexture = null;
			float armorR = 1f;
			float armorG = 1f;
			float armorB = 1f;
			if (armorItem instanceof DyeableArmorItem dyeableItem) {
				//overlayTexture = getArmorResource(entity, armorStack, EquipmentSlot.CHEST, "overlay");
				int color = dyeableItem.getColor(armorStack);
				armorR = (float) (color >> 16 & 255) / 255.0F;
				armorG = (float) (color >> 8 & 255) / 255.0F;
				armorB = (float) (color & 255) / 255.0F;
			}
			matrixStack.push();
			matrixStack.translate(0, -0.015f, -0.015f);
			matrixStack.scale(1.05f, 1.1f, 1);
			WildfireModelRenderer.BulgeModelBox armor = bulgeModelArmor;
			RenderLayer armorType = RenderLayer.getArmorCutoutNoCull(armorTexture);
			VertexConsumer armorVertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, armorType, false, armorStack.hasGlint());
			renderBox(armor, matrixStack, armorVertexConsumer, packedLightIn, OverlayTexture.DEFAULT_UV, armorR, armorG, armorB, alpha);
			if (overlayTexture != null) {
				RenderLayer overlayType = RenderLayer.getArmorCutoutNoCull(overlayTexture);
				VertexConsumer overlayVertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, overlayType, false, armorStack.hasGlint());
				renderBox(armor, matrixStack, overlayVertexConsumer, packedLightIn, OverlayTexture.DEFAULT_UV, red, green, blue, alpha);
			}
			matrixStack.pop();
		}
	}

	private void renderBunWithTransforms(AbstractClientPlayerEntity entity, ModelPart body, ItemStack armorStack, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider,
											RenderLayer breastRenderType, int packedLightIn, int combineTex, float red, float green, float blue, float alpha, boolean bounceEnabled, float totalX, float total,
											float bounceRotation, float breastSize, float breastOffsetX, float breastOffsetY, float breastOffsetZ, float zOff, float outwardAngle, boolean uniboob,
											boolean isChestplateOccupied, boolean breathingAnimation, boolean left) {
		matrixStack.push();
		//Surround with a try/catch to fix for essential mod.
		try {
			matrixStack.translate(body.pivotX * 0.0625f, body.pivotY * 0.0625f, body.pivotZ * 0.0625f);
			if (body.roll != 0.0F) {
				matrixStack.multiply(new Quaternion(0f, 0f, body.roll, false));
			}
			if (body.yaw != 0.0F) {
				matrixStack.multiply(new Quaternion(0f, body.yaw, 0f, false));
			}
			if (body.pitch != 0.0F) {
				matrixStack.multiply(new Quaternion(body.pitch, 0f, 0f, false));
			}

			if (bounceEnabled) {
				matrixStack.translate(totalX / 32f, 0, 0);
				matrixStack.translate(0, total / 32f, 0);
			}

			matrixStack.translate(breastOffsetX * 0.0625f, 0.60625f + (breastOffsetY * 0.0625f), -zOff + 0.0625f * 4.5f + (breastOffsetZ * 0.0625f)); //shift down to correct position

			if (!uniboob) {
				matrixStack.translate(-0.0625f * 2 * (left ? 1 : -1), 0, 0);
			}
			if (bounceEnabled) {
				matrixStack.multiply(new Quaternion(0, bounceRotation, 0, true));
			}
			if (!uniboob) {
				matrixStack.translate(0.0625f * 2 * (left ? 1 : -1), 0, 0);
			}

			float rotationMultiplier = 0;
			if (bounceEnabled) {
				matrixStack.translate(0, -0.035f * breastSize, 0); //shift down to correct position
				rotationMultiplier = -total / 12f;
			}
			float totalRotation = breastSize + rotationMultiplier;
			if (!bounceEnabled) {
				totalRotation = breastSize;
			}
			if (totalRotation > breastSize + 0.2F) {
				totalRotation = breastSize + 0.2F;
			}
			totalRotation = Math.min(totalRotation, 1); //hard limit for MAX

			if (isChestplateOccupied) {
				matrixStack.translate(0, 0, 0.01f);
			}

			matrixStack.multiply(new Quaternion(0, outwardAngle, 0, true));
			matrixStack.multiply(new Quaternion(-35f * totalRotation, 180.0f, 0, true));

			if (breathingAnimation) {
				float f5 = -MathHelper.cos(entity.age * 0.09F) * 0.45F + 0.45F;
				matrixStack.multiply(new Quaternion(f5, 0, 0, true));
			}

			matrixStack.scale(0.9995f, 1f, 1f); //z-fighting FIXXX

			renderBuns(entity, armorStack, matrixStack, vertexConsumerProvider, breastRenderType, packedLightIn, combineTex, red, green, blue, alpha, left);
		} catch(Exception e) {
			e.printStackTrace();
		}
		matrixStack.pop();
	}

	private void renderBuns(AbstractClientPlayerEntity entity, ItemStack armorStack, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, RenderLayer breastRenderType,
							  int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha, boolean left) {
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(breastRenderType);
		renderBox(left ? lBun : rBun, matrixStack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		if (entity.isPartVisible(left ? PlayerModelPart.LEFT_PANTS_LEG : PlayerModelPart.RIGHT_PANTS_LEG)) {
			matrixStack.translate(0, 0, -0.015f);
			matrixStack.scale(1.05f, 1.05f, 1.05f);
			renderBox(left ? lBunWear: rBunWear, matrixStack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		}
		//TODO: Eventually we may want to expose a way via the API for mods to be able to override rendering
		// be it because they are not an armor item or the way they render their armor item is custom
		//Render Breast Armor
		if (!armorStack.isEmpty() && armorStack.getItem() instanceof ArmorItem armorItem) {
			Identifier armorTexture = getArmorResource(armorItem, true, null);
			Identifier overlayTexture = null;
			float armorR = 1f;
			float armorG = 1f;
			float armorB = 1f;
			if (armorItem instanceof DyeableArmorItem dyeableItem) {
				//overlayTexture = getArmorResource(entity, armorStack, EquipmentSlot.CHEST, "overlay");
				int color = dyeableItem.getColor(armorStack);
				armorR = (float) (color >> 16 & 255) / 255.0F;
				armorG = (float) (color >> 8 & 255) / 255.0F;
				armorB = (float) (color & 255) / 255.0F;
			}
			matrixStack.push();
			matrixStack.translate(left ? 0.001f : -0.001f, -0.015f, -0.015f);
			matrixStack.scale(1.05f, 1.1f, 1);
			WildfireModelRenderer.BreastModelBox armor = left ? lBunArmor : rBunArmor;
			RenderLayer armorType = RenderLayer.getArmorCutoutNoCull(armorTexture);
			VertexConsumer armorVertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, armorType, false, armorStack.hasGlint());
			renderBox(armor, matrixStack, armorVertexConsumer, packedLightIn, OverlayTexture.DEFAULT_UV, armorR, armorG, armorB, alpha);
			if (overlayTexture != null) {
				RenderLayer overlayType = RenderLayer.getArmorCutoutNoCull(overlayTexture);
				VertexConsumer overlayVertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, overlayType, false, armorStack.hasGlint());
				renderBox(armor, matrixStack, overlayVertexConsumer, packedLightIn, OverlayTexture.DEFAULT_UV, red, green, blue, alpha);
			}
			matrixStack.pop();
		}
	}

	private static void renderBox(WildfireModelRenderer.ModelBox model, MatrixStack matrixStack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn,
								  float red, float green, float blue, float alpha) {
		Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
		Matrix3f matrix3f = matrixStack.peek().getNormalMatrix();
		for (WildfireModelRenderer.TexturedQuad quad : model.quads) {
			Vec3f vector3f = new Vec3f(quad.normal.getX(), quad.normal.getY(), quad.normal.getZ());
			vector3f.transform(matrix3f);
			float normalX = vector3f.getX();
			float normalY = vector3f.getY();
			float normalZ = vector3f.getZ();
			for (PositionTextureVertex vertex : quad.vertexPositions) {
				float j = vertex.x() / 16.0F;
				float k = vertex.y() / 16.0F;
				float l = vertex.z() / 16.0F;
				Vector4f vector4f = new Vector4f(j, k, l, 1.0F);
				vector4f.transform(matrix4f);
				bufferIn.vertex(vector4f.getX(), vector4f.getY(), vector4f.getZ(), red, green, blue, alpha, vertex.texturePositionX(), vertex.texturePositionY(), packedOverlayIn, packedLightIn, normalX, normalY, normalZ);
			}
		}
	}
}
