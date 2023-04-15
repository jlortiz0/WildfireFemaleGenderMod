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

package com.wildfire.main.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.wildfire.main.GenderPlayer.Pronouns;
import com.wildfire.main.HurtSoundBank;

public class HurtSoundConfigKey extends ConfigKey<HurtSoundBank> {

    //Do not modify
    private static final HurtSoundBank[] SOUNDS = HurtSoundBank.values();

    public HurtSoundConfigKey(String key) {
        super(key, HurtSoundBank.NONE);
    }

    @Override
    protected HurtSoundBank read(JsonElement element) {
        if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isNumber()) {
                int ordinal = primitive.getAsInt();
                if (ordinal >= 0 && ordinal < SOUNDS.length) {
                    return SOUNDS[ordinal];
                }
            }
        }
        return defaultValue;
    }

    @Override
    public void save(JsonObject object, HurtSoundBank value) {
        object.addProperty(key, value.ordinal());
    }
}