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

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class WildfireSounds {
	private static HashMap<ResourceLocation, Integer> hsMap = new HashMap<>(12);
	private static HurtSound[] hsArr;

	static {
		HurtSound NOTHING = new HurtSound("None", null);
		HurtSound FEMALE_HURT = new HurtSound("Female", new ResourceLocation(WildfireGender.MODID, "female_hurt"));
		HurtSound FEMALE_HURT2 = new HurtSound("Terraria (F)", new ResourceLocation(WildfireGender.MODID, "female_hurt2"));
		HurtSound FEMALE_HURT3 = new HurtSound("Femscout", new ResourceLocation(WildfireGender.MODID, "female_hurt3"));
		HurtSound FEMALE_HURT4 = new HurtSound("Femsniper", new ResourceLocation(WildfireGender.MODID, "female_hurt4"));
		HurtSound MALE_HURT1 = new HurtSound("Male", new ResourceLocation(WildfireGender.MODID, "male_hurt"));
		HurtSound MALE_HURT2 = new HurtSound("Terraria (M)", new ResourceLocation(WildfireGender.MODID, "male_hurt2"));
		HurtSound MALE_HURT3 = new HurtSound("Soldier", new ResourceLocation(WildfireGender.MODID, "male_hurt3"));
		HurtSound MALE_HURT4 = new HurtSound("Scout", new ResourceLocation(WildfireGender.MODID, "male_hurt4"));
		HurtSound INKLING = new HurtSound("Inkling", new ResourceLocation(WildfireGender.MODID, "squid_hurt"));
		HurtSound ROBOT = new HurtSound("SWORDSMACHINE", new ResourceLocation(WildfireGender.MODID, "robo_hurt"));
		HurtSound OOF = new HurtSound("Roblox", new ResourceLocation(WildfireGender.MODID, "oof_hurt"));

		hsArr = new HurtSound[]{NOTHING, FEMALE_HURT, FEMALE_HURT2, FEMALE_HURT3, FEMALE_HURT4, MALE_HURT1, MALE_HURT2, MALE_HURT3, MALE_HURT4, INKLING, ROBOT, OOF};
		for (int i = 0; i < hsArr.length; i++) {
			hsMap.put(hsArr[i].getId(), i);
		}
	}

	public static HurtSound get(ResourceLocation loc) {
		return hsArr[hsMap.get(loc)];
	}

	public static HurtSound get(String loc) {
		return get(new ResourceLocation(WildfireGender.MODID, loc));
	}

	public static HurtSound getNext(ResourceLocation loc) {
		int i = hsMap.get(loc) + 1;
		if (i >= hsArr.length) {
			return hsArr[0];
		}
		return hsArr[i];
	}

	public static HurtSound getNext(String loc) {
		return getNext(new ResourceLocation(WildfireGender.MODID, loc));
	}
}
