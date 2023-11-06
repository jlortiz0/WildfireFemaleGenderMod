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
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;


public class WildfireGenderClient implements ClientModInitializer {
    public static final SimpleRegistry<IHurtSound> hurtSounds = FabricRegistryBuilder.createSimple(IHurtSound.class, new Identifier(WildfireGender.MODID, "hurt_sound_registry")).buildAndRegister();

    @Override
    public void onInitializeClient() {
        WildfireEventHandler.registerClientEvents();
    }

    static {
        Registry.register(hurtSounds, (Identifier) null, new HurtSound("Disabled", null, false));
        Identifier id =  new Identifier(WildfireGender.MODID, "male_hurt1");
        Registry.register(hurtSounds, id, new HurtSound("Masculine 1", id, false));
        id = new Identifier(WildfireGender.MODID, "female_hurt1");
        Registry.register(hurtSounds, id, new HurtSound("Feminine 1", id, true));
    }
}