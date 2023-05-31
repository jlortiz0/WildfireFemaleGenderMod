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
import com.wildfire.api.IHurtSound;
import com.wildfire.main.HurtSound;
import com.wildfire.main.WildfireGender;
import com.wildfire.main.WildfireGenderServer;
import net.minecraft.util.Identifier;

public class HurtSoundConfigKey extends ConfigKey<Identifier> {

    //Do not modify

    public HurtSoundConfigKey(String key) {
        super(key, null);
    }

    @Override
    protected Identifier read(JsonElement element) {
        if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isString()) {
                return new Identifier(primitive.getAsString());
            }
        }
        return defaultValue;
    }

    @Override
    public void save(JsonObject object, Identifier value) {
        if (value != null) {
            object.addProperty(key, value.toString());
        }
    }
}