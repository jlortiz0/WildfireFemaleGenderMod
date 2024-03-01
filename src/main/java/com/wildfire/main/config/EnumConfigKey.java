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

package com.wildfire.main.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class EnumConfigKey<T extends Enum<T>> extends ConfigKey<T> {
    private final Class<T> cls;
    public EnumConfigKey(String key, Class<T> cls) {
        this(key, cls, cls.getEnumConstants()[0]);
    }
    public EnumConfigKey(String key, Class<T> cls, T defaultValue) {
        super(key, defaultValue);
        this.cls = cls;
    }

    protected T read(JsonElement element) {
        if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isNumber()) {
                int i = primitive.getAsInt();
                if (i >= 0 && i < cls.getEnumConstants().length) {
                    return cls.getEnumConstants()[i];
                }
            }
        }
        return defaultValue;
    }

    @Override
    public void save(JsonObject object, T value) {
        object.addProperty(key, value.ordinal());
    }

    @Override
    public boolean validate(T value) {
        return value != null && value.ordinal() < cls.getEnumConstants().length;
    }
}