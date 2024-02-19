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

package com.wildfire.main;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wildfire.api.IGenderArmor;
import com.wildfire.render.GenderLayer;
import com.wildfire.render.armor.EmptyGenderArmor;
import com.wildfire.render.armor.MoveBoxGenderArmor;
import com.wildfire.render.armor.SimpleGenderArmor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class WildfireHelper {

    public static final Capability<IGenderArmor> GENDER_ARMOR_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Map<ResourceLocation, IGenderArmor> GENDER_ARMOR_MAP = new HashMap<>();

    public static float randFloat(float min, float max) {
        return (float) ThreadLocalRandom.current().nextDouble(min, (double) max + 1);
    }

    public static IGenderArmor getArmorConfig(ItemStack stack, ResourceLocation rl) {
        if (stack.isEmpty()) {
            return EmptyGenderArmor.INSTANCE;
        }
        return stack.getCapability(WildfireHelper.GENDER_ARMOR_CAPABILITY).orElseGet(() -> {
            //While these defaults could be attached to the item stack via the AttachCapabilitiesEvent there is not
            // really a great reason to do so as we would then need to ensure we handle all the lazy optionals properly,
            // so we just include them as part of this fallback
            if (stack.getItem() instanceof ArmorItem armorItem && armorItem.getSlot() == EquipmentSlot.CHEST) {
                //Start by checking if it is a vanilla chestplate as we have custom configurations for those we check against
                // the armor material instead of the item instance in case any mods define custom armor items using vanilla
                // materials as then we can make a better guess at what we want the default implementation to be
                ArmorMaterial material = armorItem.getMaterial();
                if (material == ArmorMaterials.LEATHER) {
                    return SimpleGenderArmor.LEATHER;
                } else if (material == ArmorMaterials.CHAIN) {
                    return SimpleGenderArmor.CHAIN_MAIL;
                } else if (material == ArmorMaterials.GOLD) {
                    return SimpleGenderArmor.GOLD;
                } else if (material == ArmorMaterials.IRON) {
                    return SimpleGenderArmor.IRON;
                } else if (material == ArmorMaterials.DIAMOND) {
                    return SimpleGenderArmor.DIAMOND;
                } else if (material == ArmorMaterials.NETHERITE) {
                    return SimpleGenderArmor.NETHERITE;
                }
                return GENDER_ARMOR_MAP.getOrDefault(rl, SimpleGenderArmor.FALLBACK);
                //Otherwise just fallback to our default armor implementation
            }
            //If it is not an armor item default as if "nothing is being worn that covers the breast area"
            // this might not be fully accurate and may need some tweaks but in general is likely relatively
            // close to the truth of if it should render or not. This covers cases such as the elytra and
            // other wearables
            return EmptyGenderArmor.INSTANCE;
        });
    }

    public static void loadGenderArmorFile() {
        JsonObject obj = GsonHelper.parse(new InputStreamReader(WildfireHelper.class.getResourceAsStream("/armors.json")));
        for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
            String key = entry.getKey();
            JsonObject entryObj = entry.getValue().getAsJsonObject();
            float physics = entryObj.has("physics") ? entryObj.get("physics").getAsFloat() : 0.5f;
            int tW = entryObj.has("w") ? entryObj.get("w").getAsInt() : 64;
            int tH = entryObj.has("h") ? entryObj.get("h").getAsInt() : 64;
            int leftSub = entryObj.has("left") ? entryObj.get("left").getAsInt() : 4;
            int u = entryObj.has("u") ? entryObj.get("u").getAsInt() : 20;
            int v = entryObj.has("v") ? entryObj.get("v").getAsInt() : 17;
            GENDER_ARMOR_MAP.put(new ResourceLocation(key), new MoveBoxGenderArmor(physics, tW, tH, u, v, leftSub));
        }
    }
}
