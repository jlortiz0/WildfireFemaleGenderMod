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

package com.wildfire.main;

import com.wildfire.api.IGenderArmor;
import com.wildfire.api.WildfireAPI;
import com.wildfire.main.config.FloatConfigKey;
import com.wildfire.render.armor.EmptyGenderArmor;
import com.wildfire.render.armor.SimpleGenderArmor;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public final class WildfireHelper {
    private WildfireHelper() {
        throw new UnsupportedOperationException();
    }

    public static int randInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    public static float randFloat(float min, float max) {
        return (float) ThreadLocalRandom.current().nextDouble(min, (double) max + 1);
    }

    public static IGenderArmor getArmorConfig(ItemStack stack) {
        if (stack.isEmpty()) {
            return EmptyGenderArmor.INSTANCE;
        }

        if (WildfireAPI.getGenderArmors().get(stack.getItem()) != null) {
            return WildfireAPI.getGenderArmors().get(stack.getItem());
        } else {
            //TODO: Fabric Alternative to Capabilities? Maybe someone can help with this?
            if (stack.getItem() instanceof ArmorItem armorItem && armorItem.getSlotType() == EquipmentSlot.CHEST) {
                //Start by checking if it is a vanilla chestplate as we have custom configurations for those we check against
                // the armor material instead of the item instance in case any mods define custom armor items using vanilla
                // materials as then we can make a better guess at what we want the default implementation to be
                RegistryEntry<ArmorMaterial> material = armorItem.getMaterial();
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
                //Otherwise just fallback to our default armor implementation
                return SimpleGenderArmor.FALLBACK;
            }
            //If it is not an armor item default as if "nothing is being worn that covers the breast area"
            // this might not be fully accurate and may need some tweaks but in general is likely relatively
            // close to the truth of if it should render or not. This covers cases such as the elytra and
            // other wearables
            return EmptyGenderArmor.INSTANCE;
        }
    }

    /**
     * Utility method returning an {@link Optional} containing the requested value from the provided {@link NbtCompound}
     */
    public static <T> Optional<T> readNbt(NbtCompound compound, String key, Function<String, T> reader) {
        if(!compound.contains(key)) {
            return Optional.empty();
        }
        return Optional.of(reader.apply(key));
    }

    /**
     * Variant of {@link #readNbt}, clamping a {@code float} value to the allowed range by the provided config key.
     */
    public static Optional<Float> readNbt(NbtCompound compound, String key, FloatConfigKey configKey) {
        return readNbt(compound, key, compound::getFloat)
                .map(v -> MathHelper.clamp(v, configKey.getMinInclusive(), configKey.getMaxInclusive()));
    }
}
