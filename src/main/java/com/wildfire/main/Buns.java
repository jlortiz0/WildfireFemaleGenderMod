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

import com.wildfire.main.config.ConfigKey;
import com.wildfire.main.config.Configuration;

import java.util.function.Consumer;

public class Buns {

    private float xOffset = Configuration.BUNS_OFFSET_X.getDefault(), yOffset = Configuration.BUNS_OFFSET_Y.getDefault(), zOffset = Configuration.BUNS_OFFSET_Z.getDefault();
    private float gap = Configuration.BUNS_GAP.getDefault();
    private boolean unibuns = Configuration.BUNS_UNIBUN.getDefault();

    private <VALUE> boolean updateValue(ConfigKey<VALUE> key, VALUE value, Consumer<VALUE> setter) {
        if (key.validate(value)) {
            setter.accept(value);
            return true;
        }
        return false;
    }

    public float getXOffset() {
        return xOffset;
    }

    public boolean updateXOffset(float value) {
        return updateValue(Configuration.BUNS_OFFSET_X, value, v -> this.xOffset = v);
    }

    public float getYOffset() {
        return yOffset;
    }

    public boolean updateYOffset(float value) {
        return updateValue(Configuration.BUNS_OFFSET_Y, value, v -> this.yOffset = v);
    }

    public float getZOffset() {
        return zOffset;
    }

    public boolean updateZOffset(float value) {
        return updateValue(Configuration.BUNS_OFFSET_Z, value, v -> this.zOffset = v);
    }

    public float getGap() {
        return gap;
    }

    public boolean updateGap(float value) {
        return updateValue(Configuration.BUNS_GAP, value, v -> this.gap = v);
    }

    public boolean isUnibun() {
        return unibuns;
    }

    public boolean updateUnibun(boolean value) {
        return updateValue(Configuration.BUNS_UNIBUN, value, v -> this.unibuns = v);
    }
}
