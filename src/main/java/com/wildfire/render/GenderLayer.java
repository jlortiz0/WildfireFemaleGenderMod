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
import com.wildfire.physics.BulgePhysics;
import com.wildfire.physics.BunPhysics;
import com.wildfire.render.WildfireModelRenderer.*;
import com.wildfire.render.armor.EmptyGenderArmor;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.apoli.power.ModelColorPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginLayers;
import io.github.apace100.origins.registry.ModComponents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
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
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GenderLayer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

	private BreastModelBox lBreast, rBreast;
	private static final OverlayModelBox lBreastWear = new OverlayModelBox(true,64, 64, 17, 34, -4F, 0.0F, 0F, 4, 5, 3, 0.0F, false);
	private static final OverlayModelBox rBreastWear = new OverlayModelBox(false,64, 64, 21, 34, 0, 0.0F, 0F, 4, 5, 3, 0.0F, false);
	private static final BreastModelBox lBoobArmor = new BreastModelBox(64, 32, 16, 17, -4F, 0.0F, 0F, 4, 5, 3, 0.0F, false);
	private static final BreastModelBox rBoobArmor = new BreastModelBox(64, 32, 20, 17, 0, 0.0F, 0F, 4, 5, 3, 0.0F, false);
	private static final BreastModelBox lBoobArmorArclight = new BreastModelBox(460, 361, 116, 147, -4F, 0.0F, 0F, 4, 5, 3, 0.0F, false);
	private static final BreastModelBox rBoobArmorArclight = new BreastModelBox(460, 361, 200, 147, 0, 0.0F, 0F, 4, 5, 3, 0.0F, false);
	private BulgeModelBox bulgeModel, bulgeModelArmor, bulgeModelArmorArclight;
	private BulgeModelBox bulgeWear;
	private BunModelBox lBun, rBun;
	private static final BunModelBox lBunWear = new BunModelBox(64, 64, 28, 44, -4F, 0.0F, 0F, 4, 4, 4, 0.0F, false);
	private static final BunModelBox rBunWear = new BunModelBox(64, 64, 32, 44, 0, 0.0F, 0F, 4, 4, 4, 0.0F, true);
	private static final BreastModelBox lBunArmor = new BreastModelBox(64, 32, 0, 16, -4F, 0.0F, 0F, 4, 4, 4, 0.0F, false);
	private static final BreastModelBox rBunArmor = new BreastModelBox(64, 32, 0, 16, 0, 0.0F, 0F, 4, 4, 4, 0.0F, false);
	private static final BreastModelBox lBunArmorArclight = new BreastModelBox(539, 292, 308, 0, -4F, 0.0F, 0F, 4, 4, 4, 0.0F, false);
	private static final BreastModelBox rBunArmorArclight = new BreastModelBox(539, 292, 388, 0, 0, 0.0F, 0F, 4, 4, 4, 0.0F, false);

	private float preBreastSize = 0f;
	private float preBulgeSize = 0f;
	private float preBunSize = 0f;

	private ModelColorPower modelColor = null;
	private Identifier lastOrigin = null;

	public GenderLayer(FeatureRendererContext render) {
		super(render);

		lBreast = new BreastModelBox(64, 64, 16, 17, -4F, 0.0F, 0F, 4, 5, 4, 0.0F, false);
		rBreast = new BreastModelBox(64, 64, 20, 17, 0, 0.0F, 0F, 4, 5, 4, 0.0F, false);

		bulgeModel = new BulgeModelBox(64, 64, 5, 20, -1F, 0.0F, 0F, 2, 2, 3, 0.0F, false);
		bulgeWear = new BulgeModelBox(64, 64, 5, 36, -1F, 0F, 0F, 2, 2, 3, 0F, false);
		bulgeModelArmor = new BulgeModelBox(64, 32, 5, 20, -1F, 0.0F, 0F, 2, 2, 4, 0.0F, false);
		bulgeModelArmorArclight = new BulgeModelBox(539, 292, 103, 20, -1F, 0.0F, 0F, 2, 2, 4, 0.0F, false);

		lBun = new BunModelBox(64, 64, 28, 28, -4F, 0.0F, 0F, 4, 4, 4, 0.0F, false);
		rBun = new BunModelBox(64, 64, 32, 28, 0, 0.0F, 0F, 4, 4, 4, 0.0F, true);
	}
	private static final Map<String, Identifier> ARMOR_TEXTURE_CACHE = new HashMap<>();

	private static final Map<String, Identifier> ARMOR_LOC_CACHE = new HashMap<>();

	public Identifier getArmorResource(ArmorItem item, boolean legs) {
		Identifier ingrId = ARMOR_LOC_CACHE.computeIfAbsent(item.getMaterial().getName(), Identifier::new);
		String string = "textures/models/armor/" + ingrId.getPath() + "_layer_" + (legs ? 2 : 1) + ".png";
		return ARMOR_TEXTURE_CACHE.computeIfAbsent(string, (s) -> new Identifier(ingrId.getNamespace(), s));
	}

	@Override
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int packedLightIn, @Nonnull AbstractClientPlayerEntity ent, float limbAngle,
					   float limbDistance, float partialTicks, float animationProgress, float headYaw, float headPitch) {
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
				Map<String, TrinketInventory> tm = TrinketsApi.getTrinketComponent(ent).get().getInventory().get("chest");
				TrinketInventory ti = null;
				if (tm != null) {
					ti = tm.get("cosmetic");
				}
				if (ti != null && ti.getStack(0).getItem() != Items.AIR) {
					armorStack = ti.getStack(0);
					armorConfig = WildfireHelper.getArmorConfig(armorStack);
				}
			}
			TextureManager texMgr = MinecraftClient.getInstance().getTextureManager();
			if ((armorStack.getItem() instanceof ArmorItem) && texMgr.getOrDefault(getArmorResource((ArmorItem) armorStack.getItem(), false), null) == null) {
				armorStack = null;
				armorConfig = EmptyGenderArmor.INSTANCE;
			}
			boolean isChestplateOccupied = armorConfig.alwaysHidesBreasts() || (!plr.showBreastsInArmor() && armorConfig.coversBreasts());

			ItemStack armorStack2 = ent.getEquippedStack(EquipmentSlot.LEGS);
			IGenderArmor armorConfig2 = WildfireHelper.getArmorConfig(armorStack2);
			if (FabricLoader.getInstance().isModLoaded("trinkets")) {
				Map<String, TrinketInventory> tm = TrinketsApi.getTrinketComponent(ent).get().getInventory().get("legs");
				TrinketInventory ti = null;
				if (tm != null) {
					ti = tm.get("cosmetic");
				}
				if (ti != null && ti.getStack(0).getItem() != Items.AIR) {
					armorStack2 = ti.getStack(0);
					armorConfig2 = WildfireHelper.getArmorConfig(armorStack2);
				}
			}
			if ((armorStack2.getItem() instanceof ArmorItem) && texMgr.getOrDefault(getArmorResource((ArmorItem) armorStack2.getItem(), true), null) == null) {
				armorStack2 = null;
				armorConfig2 = EmptyGenderArmor.INSTANCE;
			}
			boolean isLeggingsOccupied = armorConfig2.alwaysHidesBreasts() || (!plr.showBreastsInArmor() && armorConfig2.coversBreasts());
			if (armorStack.isEmpty() && armorStack2.isEmpty() && ent.isInvisibleTo(MinecraftClient.getInstance().player)) {
				//Exit early if the entity shouldn't actually be seen
				return;
			}

			PlayerEntityRenderer rend = (PlayerEntityRenderer) MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(ent);
			PlayerEntityModel<AbstractClientPlayerEntity> model = rend.getModel();

			Breasts breasts = plr.getBreasts();
			float breastOffsetX = Math.round((Math.round(breasts.getXOffset() * 100f) / 100f) * 10) / 10f;
			float breastOffsetY = -Math.round((Math.round(breasts.getYOffset() * 100f) / 100f) * 10) / 10f;
			float breastOffsetZ = -Math.round((Math.round(breasts.getZOffset() * 100f) / 100f) * 10) / 10f;

			BreastPhysics leftBreastPhysics = plr.getLeftBreastPhysics();
			final float bSize = isChestplateOccupied ? 0 : leftBreastPhysics.getBreastSize(partialTicks);
			float outwardAngle = (Math.round(breasts.getCleavage() * 100f) / 100f) * 100f;
			outwardAngle = Math.min(outwardAngle, 10);

			if (FabricLoader.getInstance().isModLoaded("origins")) {
				OriginComponent component = ModComponents.ORIGIN.get(ent);
				OriginLayer layer = OriginLayers.getLayer(new Identifier("origins", "origin"));
				Origin or = component.getOrigin(layer);
				if (or != null && (lastOrigin == null || !or.getIdentifier().equals(lastOrigin))) {
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
			float bulgeRotation = Math.round((Math.round(bulge.getRotation() * 100f) / 100f) * 10) / 10f;
			float bulgeOffsetY = -Math.round((Math.round(bulge.getYOffset() * 100f) / 100f) * 10) / 10f;
			float bulgeOffsetZ = -Math.round((Math.round(bulge.getZOffset() * 100f) / 100f) * 10) / 10f;
			BulgePhysics bulgePhysics = plr.getBulgePhysics();
			final float buSize = WildfireGender.isCurseforgeNerfed ? 0 : (isLeggingsOccupied ? 0 : bulgePhysics.getBulgeSize(partialTicks));
			reducer = 0;
			if (buSize < 1f) reducer++;
			if (buSize < 0.5f) reducer++;
			if (preBulgeSize != buSize) {
				bulgeModel = new BulgeModelBox(64, 64, 5, 20, -1F, 0.0F, 0F, 2, 2, (int) (3 - bulgeOffsetZ - reducer), 0.0F, false);
				bulgeWear = new BulgeModelBox(64, 64, 5, 36, -1F, 0F, 0F, 2, 2, (int) (3 - bulgeOffsetZ - reducer), 0F, false);
				bulgeModelArmor = new BulgeModelBox(64, 32, 5, 20, -1F, 0.0F, 0F, 2, 2, (int) (4 - bulgeOffsetZ - reducer), 0.0F, false);
				bulgeModelArmorArclight = new BulgeModelBox(648, 292, 103, 20, -1F, 0.0F, 0F, 2, 2, (int) (4 - bulgeOffsetZ - reducer), 0.0F, false);
				preBulgeSize = buSize;
			}

			Buns buns = plr.getBuns();
			float bunsOffsetX = Math.round((Math.round(buns.getXOffset() * 100f) / 100f) * 10) / 10f;
			float bunsOffsetY = -Math.round((Math.round(buns.getYOffset() * 100f) / 100f) * 10) / 10f;
			float bunsOffsetZ = -Math.round((Math.round(buns.getZOffset() * 100f) / 100f) * 10) / 10f;
			BunPhysics leftBunPhysics = plr.getLeftBunPhysics();
			final float btSize = isLeggingsOccupied ? 0 : leftBunPhysics.getBunsSize(partialTicks);
			float bOutwardAngle = (Math.round(buns.getGap() * 100f) / 100f) * 100f;
			bOutwardAngle = Math.min(bOutwardAngle, 10);
			reducer = 0;
			if (btSize < 0.72f) reducer++;
			if (btSize < 0.33f) reducer++;
			if (preBunSize != btSize) {
				lBun = new BunModelBox(64, 64, 28, 28, -4F, 0.0F, 0F, 4, 4, (int) (4 - bunsOffsetZ - reducer), 0.0F, false);
				rBun = new BunModelBox(64, 64, 32, 28, 0, 0.0F, 0F, 4, 4, (int) (4 - bunsOffsetZ - reducer), 0.0F, true);
				preBunSize = btSize;
			}
			if (ent.hasVehicle()) {
				bunsOffsetX += 1.3;
				bOutwardAngle += 9;
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

			float bTotal = MathHelper.lerp(partialTicks, bulgePhysics.getPreBounceY(), bulgePhysics.getBounceY());
			float bTotalX = MathHelper.lerp(partialTicks, bulgePhysics.getPreBounceX(), bulgePhysics.getBounceX());
			float bBounceRotation = MathHelper.lerp(partialTicks, bulgePhysics.getPreBounceRotation(), bulgePhysics.getBounceRotation());

			float lBTotal = MathHelper.lerp(partialTicks, leftBunPhysics.getPreBounceY(), leftBunPhysics.getBounceY());
			float lBTotalX = MathHelper.lerp(partialTicks, leftBunPhysics.getPreBounceX(), leftBunPhysics.getBounceX());
			float leftBBounceRotation = MathHelper.lerp(partialTicks, leftBunPhysics.getPreBounceRotation(), leftBunPhysics.getBounceRotation());
			float rBTotal;
			float rBTotalX;
			float rightBBounceRotation;
			if (buns.isUnibun()) {
				rBTotal = lBTotal;
				rBTotalX = lBTotalX;
				rightBBounceRotation = leftBBounceRotation;
			} else {
				BunPhysics rightBunPhysics = plr.getRightBunPhysics();
				rBTotal = MathHelper.lerp(partialTicks, rightBunPhysics.getPreBounceY(), rightBunPhysics.getBounceY());
				rBTotalX = MathHelper.lerp(partialTicks, rightBunPhysics.getPreBounceX(), rightBunPhysics.getBounceX());
				rightBBounceRotation = MathHelper.lerp(partialTicks, rightBunPhysics.getPreBounceRotation(), rightBunPhysics.getBounceRotation());
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
			float zBuOff = 0.0f - (buSize * 0.0625f);
			float zBtOff = 0.0625f - (btSize * 0.0625f);
			if (buSize >= 1f) zBuOff += 0.03125f;
			if (buSize >= 0.5f) zBuOff += 0.03125f;

			//matrixStack.translate(0, 0, zOff);
			//System.out.println(bounceRotation);

			float resistance = MathHelper.clamp(armorConfig.physicsResistance(), 0, 1);
			float resistance2 = MathHelper.clamp(armorConfig2.physicsResistance(), 0, 1);
			//Note: We only check if the breathing animation should be enabled if the chestplate's physics resistance
			// is less than or equal to 0.5 so that if we won't be rendering it we can avoid doing extra calculations
			boolean breathingAnimation = resistance <= 0.5F &&
					(!ent.isSubmergedInWater() || StatusEffectUtil.hasWaterBreathing(ent) ||
							ent.getWorld().getBlockState(ent.getBlockPos()).isOf(Blocks.BUBBLE_COLUMN));
			boolean bounceEnabled = plr.hasBreastPhysics() && (!isChestplateOccupied || plr.hasArmorBreastPhysics() && resistance < 1); //oh, you found this?
			boolean bounceEnabled2 = plr.hasBreastPhysics() && (!isLeggingsOccupied || plr.hasArmorBreastPhysics() && resistance2 < 1);

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
				renderBulgeWithTransforms(ent, model.leftLeg, armorStack2, matrixStack, vertexConsumerProvider, type, packedLightIn, combineTex, overlayRed, overlayGreen,
						overlayBlue, overlayAlpha, bounceEnabled2, bTotalX, bTotal, bBounceRotation, bulgeRotation, bulgeSize, bulgeOffsetY,
						bulgeOffsetZ, zBuOff, isLeggingsOccupied);
			}
			if (bunsSize >= 0.02f) {
				renderBunWithTransforms(ent, model.leftLeg, armorStack2, matrixStack, vertexConsumerProvider, type, packedLightIn, combineTex, overlayRed, overlayGreen,
						overlayBlue, overlayAlpha, bounceEnabled2, lBTotalX, lBTotal, leftBBounceRotation, bunsOffsetX, bunsOffsetY, bunsOffsetZ, zBtOff,
						bOutwardAngle, buns.isUnibun(), buns.getRot() * 100f, isLeggingsOccupied, true);
				renderBunWithTransforms(ent, model.rightLeg, armorStack2, matrixStack, vertexConsumerProvider, type, packedLightIn, combineTex, overlayRed, overlayGreen,
						overlayBlue, overlayAlpha, bounceEnabled2, rBTotalX, rBTotal, rightBBounceRotation, -bunsOffsetX, bunsOffsetY, bunsOffsetZ, zBtOff,
						-bOutwardAngle, buns.isUnibun(), buns.getRot() * 100f, isLeggingsOccupied, false);
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
		if (!entity.isInvisibleTo(MinecraftClient.getInstance().player)) {
			renderBox(left ? lBreast : rBreast, matrixStack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			if (entity.isPartVisible(PlayerModelPart.JACKET)) {
				matrixStack.translate(0, 0, -0.015f);
				matrixStack.scale(1.05f, 1.05f, 1.05f);
				renderBox(left ? lBreastWear : rBreastWear, matrixStack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			}
		}
		//TODO: Eventually we may want to expose a way via the API for mods to be able to override rendering
		// be it because they are not an armor item or the way they render their armor item is custom
		//Render Breast Armor
		if (!armorStack.isEmpty() && armorStack.getItem() instanceof ArmorItem armorItem) {
			Identifier armorTexture = getArmorResource(armorItem, false);
			float armorR = 1f;
			float armorG = 1f;
			float armorB = 1f;
			if (armorItem instanceof DyeableArmorItem dyeableItem) {
				int color = dyeableItem.getColor(armorStack);
				armorR = (float) (color >> 16 & 255) / 255.0F;
				armorG = (float) (color >> 8 & 255) / 255.0F;
				armorB = (float) (color & 255) / 255.0F;
			}
			float armorA = 1f;
			boolean glint = armorStack.hasGlint();
			matrixStack.push();
			matrixStack.translate(left ? 0.001f : -0.001f, 0.015f, -0.015f);
			matrixStack.scale(1.05f, 1, 1);
			BreastModelBox armor = armorTexture.getNamespace().equals("arclight") ? (left ? lBoobArmorArclight : rBoobArmorArclight) : (left ? lBoobArmor : rBoobArmor);
			RenderLayer armorType = RenderLayer.getEntityTranslucent(armorTexture);
			VertexConsumer armorVertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, armorType, false, glint);
			renderBox(armor, matrixStack, armorVertexConsumer, packedLightIn, OverlayTexture.DEFAULT_UV, armorR, armorG, armorB, armorA);
			matrixStack.pop();
		}
	}

	private void renderBulgeWithTransforms(AbstractClientPlayerEntity entity, ModelPart body, ItemStack armorStack, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider,
										   RenderLayer breastRenderType, int packedLightIn, int combineTex, float red, float green, float blue, float alpha,
										   boolean bounceEnabled, float totalX, float total, float bounceRotation,
										   float breastOffsetRotation, float breastSize, float breastOffsetY, float breastOffsetZ, float zOff,
										   boolean isChestplateOccupied) {
		matrixStack.push();
		//Surround with a try/catch to fix for essential mod.
		try {
			matrixStack.translate(body.pivotX * 0.0625f, body.pivotY * 0.0625f, body.pivotZ * 0.0625f);
			if (body.roll != 0.0F) {
				matrixStack.multiply(new Quaternion(0f, 0f, body.roll * 0.125f, false));
			}
			if (body.yaw != 0.0F) {
				matrixStack.multiply(new Quaternion(0f, body.yaw, 0f, false));
			}
			if (Math.abs(body.pitch) > Math.PI / 2) {
				matrixStack.multiply(new Quaternion(body.pitch, 0f, 0f, false));
			} else if (Math.abs(body.pitch) > (Math.PI / 16) * 7) {
				matrixStack.multiply(new Quaternion(body.pitch * 0.5f, 0f, 0f, false));
			} else if (bounceEnabled) {
				matrixStack.multiply(new Quaternion(body.pitch * 0.1f, 0f, 0f, false));
			}

			if (bounceEnabled) {
				matrixStack.translate(totalX / 32f, 0, 0);
				// matrixStack.translate(0, total / 32f, 0);
			}

			matrixStack.translate(-0.125f, -0.00625f + (breastOffsetY * 0.0625f), zOff - 0.075f + ((breastOffsetZ - breastOffsetRotation) * 0.0625f)); //shift down to correct position

			float totalRotation = breastOffsetRotation - 0.15f;

			if (bounceEnabled) {
				totalRotation -= total;
			}
			if (isChestplateOccupied) {
				matrixStack.translate(0, 0, 0.01f);
			}

			matrixStack.multiply(new Quaternion(-35f * totalRotation + 180f,  bounceEnabled ? bounceRotation * -8.75f : 0, 0, true));

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
		if (!entity.isInvisibleTo(MinecraftClient.getInstance().player)) {
			renderBox(bulgeModel, matrixStack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			if (entity.isPartVisible(PlayerModelPart.JACKET)) {
				matrixStack.scale(1.05f, 1.05f, 1.05f);
				matrixStack.translate(0, -0.005f, 0.005f);
				renderBox(bulgeWear, matrixStack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			}
		}
		//TODO: Eventually we may want to expose a way via the API for mods to be able to override rendering
		// be it because they are not an armor item or the way they render their armor item is custom
		//Render Breast Armor
		if (!armorStack.isEmpty() && armorStack.getItem() instanceof ArmorItem armorItem) {
			Identifier armorTexture = getArmorResource(armorItem, true);
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
			float armorA = 1f;
			boolean glint = armorStack.hasGlint();
			matrixStack.push();
			matrixStack.scale(1.05f, 1.05f, 1.05f);
			matrixStack.translate(0, -0.005f, 0.005f);
			BulgeModelBox armor = armorTexture.getNamespace().equals("arclight") ? bulgeModelArmorArclight : bulgeModelArmor;
			RenderLayer armorType = RenderLayer.getEntityTranslucent(armorTexture);
			VertexConsumer armorVertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, armorType, false, glint);
			renderBox(armor, matrixStack, armorVertexConsumer, packedLightIn, OverlayTexture.DEFAULT_UV, armorR, armorG, armorB, armorA);
			matrixStack.pop();
		}
	}

	private void renderBunWithTransforms(AbstractClientPlayerEntity entity, ModelPart body, ItemStack armorStack, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider,
										 RenderLayer breastRenderType, int packedLightIn, int combineTex, float red, float green, float blue, float alpha, boolean bounceEnabled, float totalX, float total,
										 float bounceRotation, float breastOffsetX, float breastOffsetY, float breastOffsetZ, float zOff, float outwardAngle, boolean uniboob,
										 float yRot, boolean isChestplateOccupied, boolean left) {
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
			if (Math.abs(body.pitch) > Math.PI / 2) {
				matrixStack.multiply(new Quaternion(body.pitch, 0f, 0f, false));
			} else if (body.pitch != 0f) {
				matrixStack.multiply(new Quaternion(body.pitch * 0.05f, 0f, 0f, false));
			}

			if (bounceEnabled) {
				matrixStack.translate(totalX / 32f, 0, 0);
				matrixStack.translate(0, total / 32f - 0.05, 0);
			}

			matrixStack.translate(breastOffsetX * 0.0625f + (left ? -0.11875 : 0.11875), -0.09625f + (breastOffsetY * 0.0625f), -zOff + 0.0625f * 4.5f + (breastOffsetZ * 0.0625f)); //shift down to correct position

			if (!uniboob) {
				matrixStack.translate(-0.0625f * 2 * (left ? 1 : -1), 0, 0);
			}
			if (bounceEnabled) {
				matrixStack.multiply(new Quaternion(0, bounceRotation, 0, true));
			}
			if (!uniboob) {
				matrixStack.translate(0.0625f * 2 * (left ? 1 : -1), 0, 0);
			}

			if (isChestplateOccupied) {
				matrixStack.translate(0, 0, 0.01f);
			}
			if (entity.isInSneakingPose()) {
				matrixStack.translate(0, 0.0625f, 0);
			}

			matrixStack.multiply(new Quaternion(yRot, outwardAngle, 0, true));
			matrixStack.multiply(new Quaternion(-45, 180, 0, true));

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
		if (!entity.isInvisibleTo(MinecraftClient.getInstance().player)) {
			renderBox(left ? lBun : rBun, matrixStack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			if (entity.isPartVisible(left ? PlayerModelPart.LEFT_PANTS_LEG : PlayerModelPart.RIGHT_PANTS_LEG)) {
				matrixStack.translate(left ? 0.001f : -0.001f, -0.015f, -0.015f);
				matrixStack.scale(1.05f, 1.05f, 1.05f);
				renderBox(left ? lBunWear : rBunWear, matrixStack, vertexConsumer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			}
		}
		//TODO: Eventually we may want to expose a way via the API for mods to be able to override rendering
		// be it because they are not an armor item or the way they render their armor item is custom
		//Render Breast Armor
		if (!armorStack.isEmpty() && armorStack.getItem() instanceof ArmorItem armorItem) {
			Identifier armorTexture = getArmorResource(armorItem, true);
			float armorR = 1f;
			float armorG = 1f;
			float armorB = 1f;
			if (armorItem instanceof DyeableArmorItem dyeableItem) {
				int color = dyeableItem.getColor(armorStack);
				armorR = (float) (color >> 16 & 255) / 255.0F;
				armorG = (float) (color >> 8 & 255) / 255.0F;
				armorB = (float) (color & 255) / 255.0F;
			}
			float armorA = 1f;
			boolean glint = armorStack.hasGlint();
			matrixStack.push();
			matrixStack.translate(left ? 0.001f : -0.001f, -0.015f, -0.015f);
			matrixStack.scale(1.05f, 1.1f, 1);
			BreastModelBox armor = armorTexture.getNamespace().equals("arclight") ? (left ? lBunArmorArclight : rBunArmorArclight) : (left ? lBunArmor : rBunArmor);
			RenderLayer armorType = RenderLayer.getEntityTranslucent(armorTexture);
			VertexConsumer armorVertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, armorType, false, glint);
			renderBox(armor, matrixStack, armorVertexConsumer, packedLightIn, OverlayTexture.DEFAULT_UV, armorR, armorG, armorB, armorA);
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
