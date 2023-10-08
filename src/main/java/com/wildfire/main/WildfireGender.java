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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

import com.wildfire.api.IHurtSound;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

public class WildfireGender implements ClientModInitializer {
  	public static final String MODID = "wildfire_gender";
	public static final Map<UUID, GenderPlayer> CLOTHING_PLAYERS = new HashMap<>();

	public static final boolean isCurseforgeNerfed = false;
	public static final String GITHUB_LINK = "https://github.com/jlortiz0/WildfireGender";
	public static final SimpleRegistry<IHurtSound> hurtSounds = FabricRegistryBuilder.createSimple(IHurtSound.class, new Identifier(WildfireGender.MODID, "hurt_sound_registry")).buildAndRegister();

	@Override
  	public void onInitializeClient() {
		WildfireEventHandler.registerClientEvents();
    }

	@Nullable
	public static GenderPlayer getPlayerById(UUID id) {
		  return CLOTHING_PLAYERS.get(id);
	}

	public static GenderPlayer getOrAddPlayerById(UUID id) {
		return CLOTHING_PLAYERS.computeIfAbsent(id, GenderPlayer::new);
	}

  	
  	public static void loadGenderInfoAsync(UUID uuid, boolean markForSync) {
  		Thread thread = new Thread(() -> WildfireGender.loadGenderInfo(uuid, markForSync));
		thread.setName("WFGM_GetPlayer-" + uuid);
  		thread.start();
  	}

	public static GenderPlayer loadGenderInfo(UUID uuid, boolean markForSync) {
		return GenderPlayer.loadCachedPlayer(uuid, markForSync);
	}

	static {
		Registry.register(hurtSounds, (Identifier) null, new HurtSound("Disabled", null));
		Identifier id =  new Identifier(WildfireGender.MODID, "male_hurt1");
		Registry.register(hurtSounds, id, new HurtSound("Masculine 1", id));
		id = new Identifier(WildfireGender.MODID, "female_hurt1");
		Registry.register(hurtSounds, id, new HurtSound("Feminine 1", id));
	}
}