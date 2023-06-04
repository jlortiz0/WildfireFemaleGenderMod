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
import net.minecraft.util.Formatting;

public class ColorConfigKey extends ConfigKey<Formatting> {

    //Do not modify

    public ColorConfigKey(String key, Formatting defaultValue) {
        super(key, defaultValue);
    }

    @Override
    protected Formatting read(JsonElement element) {
        if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isNumber()) {
                Formatting f = Formatting.byColorIndex(primitive.getAsInt());
                if (validate(f)) {
                    return f;
                }
            }
        }
        return defaultValue;
    }

    @Override
    public void save(JsonObject object, Formatting value) {
        if (validate(value)) {
            object.addProperty(key, value.getColorIndex());
        } else {
            object.addProperty(key, defaultValue.getColorIndex());
        }
    }

    @Override
    public boolean validate(Formatting value) {
        return value != null && value.isColor();
    }
}