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
package com.wildfire.mixins;
import com.mojang.authlib.GameProfile;
import com.wildfire.api.IGenderArmor;
import com.wildfire.api.WildfireAPI;
import com.wildfire.main.GenderPlayer;
import com.wildfire.main.WildfireGender;
import com.wildfire.main.WildfireHelper;
import com.wildfire.render.armor.EmptyGenderArmor;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.TrinketInventory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nl.enjarai.showmeyourskin.config.ArmorConfig;
import nl.enjarai.showmeyourskin.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(value = PlayerEntity.class, priority = 900)
public abstract class PlayerEntityMixin extends LivingEntity {

    public float wfg_femaleBreast;
    public float wfg_preBounce;

    float bounceVel = 0;
    float targetBounce = 0;
    float preY = 0;

    boolean justSneaking = false;
    boolean alreadySleeping = false;

    public PlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(EntityType.PLAYER, world);
    }

    @Inject(at = @At("TAIL"), method = "tick")
    public void onTick(CallbackInfo info) {
        tickWildfire();
    }

    public void tickWildfire() {
        if(!this.world.isClient()) return;
        GenderPlayer aPlr = WildfireGender.getPlayerById(this.getUuid());
        if(aPlr == null) return;
        PlayerEntity plr = (PlayerEntity) (Object) this;

        ItemStack armorStack = plr.getEquippedStack(EquipmentSlot.CHEST);

        IGenderArmor armorConfig = WildfireHelper.getArmorConfig(armorStack);

        //Cosmetic armor
        if(FabricLoader.getInstance().isModLoaded("trinkets")) {
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
        if (armorConfig != EmptyGenderArmor.INSTANCE && FabricLoader.getInstance().isModLoaded("showmeyourskin")) {
            ArmorConfig conf = ModConfig.INSTANCE.getApplicable(plr.getUuid());
            if (conf.getTransparency(EquipmentSlot.CHEST) == 0) {
                armorConfig = EmptyGenderArmor.INSTANCE;
            }
        }

        ItemStack armorStack2 = plr.getEquippedStack(EquipmentSlot.LEGS);

        IGenderArmor armorConfig2 = WildfireHelper.getArmorConfig(armorStack2);

        //Cosmetic armor
        if(FabricLoader.getInstance().isModLoaded("trinkets")) {
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
        if (armorConfig2 != EmptyGenderArmor.INSTANCE && FabricLoader.getInstance().isModLoaded("showmeyourskin")) {
            ArmorConfig conf = ModConfig.INSTANCE.getApplicable(plr.getUuid());
            if (conf.getTransparency(EquipmentSlot.LEGS) == 0) {
                armorConfig2 = EmptyGenderArmor.INSTANCE;
            }
        }

        aPlr.getLeftBreastPhysics().update(plr, armorConfig);
        aPlr.getRightBreastPhysics().update(plr, armorConfig);
        aPlr.getBulgePhysics().update(plr, armorConfig2);
        aPlr.getLeftBunPhysics().update(plr, armorConfig2);
        aPlr.getRightBunPhysics().update(plr, armorConfig2);


    }

}