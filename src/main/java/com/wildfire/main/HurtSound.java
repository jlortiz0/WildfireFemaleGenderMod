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
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class HurtSound implements IHurtSound {
	private final String name;
	private final Identifier id;
	private final SoundEvent snd;
	private final boolean fem;

	public HurtSound(String name, Identifier id, boolean fem) {
		this.name = name;
		this.fem = fem;
		this.id = id;
		if (id != null) {
			this.snd = SoundEvent.of(id, 32);
			Registry.register(Registries.SOUND_EVENT, id, snd);
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

	public boolean isFem() {
		return this.fem;
	}
}
