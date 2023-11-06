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

package com.wildfire.main.networking;

import com.wildfire.main.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.UUID;

public abstract class PacketGenderInfo {
    protected final UUID uuid;
    private final String pronouns;
    private final int[] pronounColor;
    private final float bust_size;
    private final float buns_size;

    //physics variables
    private final boolean breast_physics;
    private final boolean breast_physics_armor;
    private final boolean show_in_armor;
    private final float bounceMultiplier;
    private final float floppyMultiplier;

    private final float xOffset, yOffset, zOffset;
    private final boolean uniboob;
    private final float cleavage;

    private final Identifier hurtSounds;
    private final float bulgeX;
    private final float bulgeY;
    private final float bulgeZ;
    private final float bulgeSize;

    private final float bunsX;
    private final float bunsY;
    private final float bunsZ;
    private final boolean unibun;
    private final float bunGap;
    private final float bunRot;

    protected PacketGenderInfo(GenderPlayer plr) {
        this.uuid = plr.uuid;
        this.pronouns = plr.getPronouns();
        this.pronounColor = plr.getPronounColor();
        this.bust_size = plr.getBustSize();
        this.buns_size = plr.getBunsSize();
        this.hurtSounds = plr.getHurtSounds();

        //physics variables
        this.breast_physics = plr.hasBreastPhysics();
        this.breast_physics_armor = plr.hasArmorBreastPhysics();
        this.show_in_armor = plr.showBreastsInArmor();
        this.bounceMultiplier = plr.getBounceMultiplierRaw();
        this.floppyMultiplier = plr.getFloppiness();

        Breasts breasts = plr.getBreasts();
        this.xOffset = breasts.getXOffset();
        this.yOffset = breasts.getYOffset();
        this.zOffset = breasts.getZOffset();

        this.uniboob = breasts.isUniboob();
        this.cleavage = breasts.getCleavage();

        Bulge bulge = plr.getBulge();
        this.bulgeX = bulge.getRotation();
        this.bulgeY = bulge.getYOffset();
        this.bulgeZ = bulge.getZOffset();
        this.bulgeSize = bulge.getSize();

        Buns buns = plr.getBuns();
        this.bunsX = buns.getXOffset();
        this.bunsY = buns.getYOffset();
        this.bunsZ = buns.getZOffset();

        this.unibun = buns.isUnibun();
        this.bunGap = buns.getGap();
        this.bunRot = buns.getRot();
    }

    protected PacketGenderInfo(PacketByteBuf buffer) {
        this.uuid = buffer.readUuid();
        this.pronouns = buffer.readString();
        this.pronounColor = new int[Math.min(buffer.readInt(), 8)];
        for (int i = 0; i < this.pronounColor.length; i++) {
            this.pronounColor[i] = buffer.readInt();
        }
        this.bust_size = buffer.readFloat();
        this.buns_size = buffer.readFloat();
        String s = buffer.readString();
        if (s.isEmpty()) {
            this.hurtSounds = null;
        } else {
            this.hurtSounds = new Identifier(s);
        }

        //physics variables
        this.breast_physics = buffer.readBoolean();
        this.breast_physics_armor = buffer.readBoolean();
        this.show_in_armor = buffer.readBoolean();
        this.bounceMultiplier = buffer.readFloat();
        this.floppyMultiplier = buffer.readFloat();

        this.xOffset = buffer.readFloat();
        this.yOffset = buffer.readFloat();
        this.zOffset = buffer.readFloat();
        this.uniboob = buffer.readBoolean();
        this.cleavage = buffer.readFloat();

        this.bulgeX = buffer.readFloat();
        this.bulgeY = buffer.readFloat();
        this.bulgeZ = buffer.readFloat();
        this.bulgeSize = buffer.readFloat();

        this.bunsX = buffer.readFloat();
        this.bunsY = buffer.readFloat();
        this.bunsZ = buffer.readFloat();
        this.unibun = buffer.readBoolean();
        this.bunGap = buffer.readFloat();
        this.bunRot = buffer.readFloat();
    }

    public void encode(PacketByteBuf buffer) {
        buffer.writeUuid(this.uuid);
        buffer.writeString(this.pronouns);
        buffer.writeInt(this.pronounColor.length);
        for (int f : this.pronounColor) {
            buffer.writeInt(f);
        }
        buffer.writeFloat(this.bust_size);
        buffer.writeFloat(this.buns_size);
        if (this.hurtSounds == null) {
            buffer.writeString("");
        } else {
            buffer.writeString(this.hurtSounds.toString());
        }

        buffer.writeBoolean(this.breast_physics);
        buffer.writeBoolean(this.breast_physics_armor);
        buffer.writeBoolean(this.show_in_armor);
        buffer.writeFloat(this.bounceMultiplier);
        buffer.writeFloat(this.floppyMultiplier);

        buffer.writeFloat(this.xOffset);
        buffer.writeFloat(this.yOffset);
        buffer.writeFloat(this.zOffset);
        buffer.writeBoolean(this.uniboob);
        buffer.writeFloat(this.cleavage);

        buffer.writeFloat(this.bulgeX);
        buffer.writeFloat(this.bulgeY);
        buffer.writeFloat(this.bulgeZ);
        buffer.writeFloat(this.bulgeSize);

        buffer.writeFloat(this.bunsX);
        buffer.writeFloat(this.bunsY);
        buffer.writeFloat(this.bunsZ);
        buffer.writeBoolean(this.unibun);
        buffer.writeFloat(this.bunGap);
        buffer.writeFloat(this.bunRot);
    }

    protected void updatePlayerFromPacket(GenderPlayer plr) {
        plr.updatePronouns(pronouns);
        plr.updatePronounColor(pronounColor);
        plr.updateBustSize(bust_size);
        plr.updateBunsSize(buns_size);
        plr.updateHurtSounds(hurtSounds);

        //physics
        plr.updateBreastPhysics(breast_physics);
        plr.updateArmorBreastPhysics(breast_physics_armor);
        plr.updateShowBreastsInArmor(show_in_armor);
        plr.updateBounceMultiplier(bounceMultiplier);
        plr.updateFloppiness(floppyMultiplier);
        //System.out.println(plr.username + " - " + plr.gender);

        Breasts breasts = plr.getBreasts();
        breasts.updateXOffset(xOffset);
        breasts.updateYOffset(yOffset);
        breasts.updateZOffset(zOffset);
        breasts.updateUniboob(uniboob);
        breasts.updateCleavage(cleavage);

        Bulge bulge = plr.getBulge();
        bulge.updateXOffset(bulgeX);
        bulge.updateYOffset(bulgeY);
        bulge.updateZOffset(bulgeZ);
        bulge.updateSize(bulgeSize);

        Buns buns = plr.getBuns();
        buns.updateXOffset(bunsX);
        buns.updateYOffset(bunsY);
        buns.updateZOffset(bunsZ);
        buns.updateUnibun(unibun);
        buns.updateGap(bunGap);
        buns.updateRot(bunRot);
    }
}
