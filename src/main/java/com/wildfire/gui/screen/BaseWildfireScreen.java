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

package com.wildfire.gui.screen;

import com.wildfire.main.GenderPlayer;
import com.wildfire.main.WildfireGender;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.UUID;

public abstract class BaseWildfireScreen extends Screen {

    protected final UUID playerUUID;
    protected final Screen parent;

    protected BaseWildfireScreen(Component title, Screen parent, UUID uuid) {
        super(title);
        this.parent = parent;
        this.playerUUID = uuid;
    }

    protected GenderPlayer getPlayer() {
        return WildfireGender.getPlayerById(this.playerUUID);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}