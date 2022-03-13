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

import com.wildfire.api.IHurtSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class HurtSound implements IHurtSound {
	private final String name;
	private final Identifier id;
	private final SoundEvent snd;
	public HurtSound(String name, Identifier id) {
		this.name = name;
		this.id = id;
		if (id != null) {
			this.snd = new SoundEvent(id);
			Registry.register(Registry.SOUND_EVENT, id, snd);
		} else {
			this.snd = null;
		}
	}

	public SoundEvent getSnd() {
		return snd;
	}

	public String getName() {
		return name;
	}

	public Identifier getId() {
		return id;
	}
}