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

package com.wildfire.main;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.lwjgl.system.CallbackI;

import java.util.Random;

public enum HurtSoundBank {
	NONE("Disabled", null),
	MASCULINE1("Masculine 1", "male_hurt1"),
	FEMININE1("Feminine 1", "female_hurt1"),
	MASCULINE2("Masculine 2", "male_hurt2"),
	FEMININE2("Feminine 2", "female_hurt2"),
	MASCULINE3("Masculine 3", "male_hurt3"),
	ROBOT("Robot", "robo_hurt");
	private final String name;
	private final Identifier id;
	private final SoundEvent snd;
	HurtSoundBank(String name, String prefix) {
		this.name = name;
		if (prefix == null) {
			this.id = null;
			this.snd = null;
		} else {
			this.id = new Identifier(WildfireGender.MODID, prefix);
			this.snd = new SoundEvent(this.id);
		}
	}

	public SoundEvent getSnd() {
		return snd;
	}

	private void register() {
		if (snd != null) {
			Registry.register(Registry.SOUND_EVENT, id, snd);
		}
	}
	public String getName() {
		return name;
	}
	public static void registerAll() {
		for (HurtSoundBank h : HurtSoundBank.values()) {
			h.register();
		}
	}
}
