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

import com.google.gson.JsonObject;
import com.wildfire.main.config.ConfigKey;
import com.wildfire.main.config.Configuration;
import com.wildfire.physics.BreastPhysics;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.UUID;
import java.util.function.Consumer;

public class GenderPlayer {

	public boolean needsSync;
	public final UUID uuid;
	private Pronouns pronouns;
	private float pBustSize = Configuration.BUST_SIZE.getDefault();

	private boolean hurtSounds = Configuration.HURT_SOUNDS.getDefault();

	//physics variables
	private boolean breastPhysics = Configuration.BREAST_PHYSICS.getDefault();
	private boolean armorBreastPhysics = Configuration.BREAST_PHYSICS_ARMOR.getDefault();
	private float bounceMultiplier = Configuration.BOUNCE_MULTIPLIER.getDefault();
	private float floppyMultiplier = Configuration.FLOPPY_MULTIPLIER.getDefault();

	public boolean lockSettings = false;

	public SyncStatus syncStatus = SyncStatus.UNKNOWN;
	private boolean showBreastsInArmor = Configuration.SHOW_IN_ARMOR.getDefault();

	private final Configuration cfg;
	private final BreastPhysics lBreastPhysics, rBreastPhysics;
	private final Breasts breasts;
	private final Bulge bulge;

	public GenderPlayer(UUID uuid) {
		this(uuid, Configuration.GENDER.getDefault());
	}

	public GenderPlayer(UUID uuid, Pronouns pronouns) {
		lBreastPhysics = new BreastPhysics(this);
		rBreastPhysics = new BreastPhysics(this);
		breasts = new Breasts();
		bulge = new Bulge();
		this.uuid = uuid;
		this.pronouns = pronouns;
		this.cfg = new Configuration("WildfireGender", this.uuid.toString());
		this.cfg.set(Configuration.USERNAME, this.uuid);
		this.cfg.setDefault(Configuration.GENDER);
		this.cfg.setDefault(Configuration.BUST_SIZE);
		this.cfg.setDefault(Configuration.HURT_SOUNDS);

		this.cfg.setDefault(Configuration.BREASTS_OFFSET_X);
		this.cfg.setDefault(Configuration.BREASTS_OFFSET_Y);
		this.cfg.setDefault(Configuration.BREASTS_OFFSET_Z);
		this.cfg.setDefault(Configuration.BREASTS_UNIBOOB);
		this.cfg.setDefault(Configuration.BREASTS_CLEAVAGE);

		this.cfg.setDefault(Configuration.BREAST_PHYSICS);
		this.cfg.setDefault(Configuration.BREAST_PHYSICS_ARMOR);
		this.cfg.setDefault(Configuration.SHOW_IN_ARMOR);
		this.cfg.setDefault(Configuration.BOUNCE_MULTIPLIER);
		this.cfg.setDefault(Configuration.FLOPPY_MULTIPLIER);
                
		this.cfg.setDefault(Configuration.BULGE_SIZE);
		this.cfg.setDefault(Configuration.BULGE_OFFSET_X);
		this.cfg.setDefault(Configuration.BULGE_OFFSET_Y);
		this.cfg.setDefault(Configuration.BULGE_OFFSET_Z);
		this.cfg.finish();
	}

	public Configuration getConfig() {
		return cfg;
	}

	private <VALUE> boolean updateValue(ConfigKey<VALUE> key, VALUE value, Consumer<VALUE> setter) {
		if (key.validate(value)) {
			setter.accept(value);
			return true;
		}
		return false;
	}

	public Pronouns getGender() {
		return pronouns;
	}

	public boolean updateGender(Pronouns value) {
		return updateValue(Configuration.GENDER, value, v -> this.pronouns = v);
	}

	public float getBustSize() {
		return pBustSize;
	}

	public boolean updateBustSize(float value) {
		return updateValue(Configuration.BUST_SIZE, value, v -> this.pBustSize = v);
	}

	public boolean hasHurtSounds() {
		return hurtSounds;
	}

	public boolean updateHurtSounds(boolean value) {
		return updateValue(Configuration.HURT_SOUNDS, value, v -> this.hurtSounds = v);
	}

	public boolean hasBreastPhysics() {
		return breastPhysics;
	}

	public boolean updateBreastPhysics(boolean value) {
		return updateValue(Configuration.BREAST_PHYSICS, value, v -> this.breastPhysics = v);
	}

	public boolean hasArmorBreastPhysics() {
		return armorBreastPhysics;
	}

	public boolean updateArmorBreastPhysics(boolean value) {
		return updateValue(Configuration.BREAST_PHYSICS_ARMOR, value, v -> this.armorBreastPhysics = v);
	}

	public boolean showBreastsInArmor() {
		return showBreastsInArmor;
	}

	public boolean updateShowBreastsInArmor(boolean value) {
		return updateValue(Configuration.SHOW_IN_ARMOR, value, v -> this.showBreastsInArmor = v);
	}

	public float getBounceMultiplier() {
		return Math.round((this.getBounceMultiplierRaw() * 3) * 100) / 100f;
	}

	public float getBounceMultiplierRaw() {
		return bounceMultiplier;
	}

	public boolean updateBounceMultiplier(float value) {
		return updateValue(Configuration.BOUNCE_MULTIPLIER, value, v -> this.bounceMultiplier = v);
	}

	public float getFloppiness() {
		return this.floppyMultiplier;
	}

	public boolean updateFloppiness(float value) {
		return updateValue(Configuration.FLOPPY_MULTIPLIER, value, v -> this.floppyMultiplier = v);
	}

	public SyncStatus getSyncStatus() {
		return this.syncStatus;
	}

	public static JsonObject toJsonObject(GenderPlayer plr) {
		JsonObject obj = new JsonObject();
		Configuration.USERNAME.save(obj, plr.uuid);
		Configuration.GENDER.save(obj, plr.getGender());
		Configuration.BUST_SIZE.save(obj, plr.getBustSize());
		Configuration.HURT_SOUNDS.save(obj, plr.hasHurtSounds());

		Configuration.BREAST_PHYSICS.save(obj, plr.hasBreastPhysics());
		Configuration.BREAST_PHYSICS_ARMOR.save(obj, plr.hasArmorBreastPhysics());
		Configuration.SHOW_IN_ARMOR.save(obj, plr.showBreastsInArmor());
		Configuration.BOUNCE_MULTIPLIER.save(obj, plr.getBounceMultiplierRaw());
		Configuration.FLOPPY_MULTIPLIER.save(obj, plr.getFloppiness());

		Breasts breasts = plr.getBreasts();
		Configuration.BREASTS_OFFSET_X.save(obj, breasts.getXOffset());
		Configuration.BREASTS_OFFSET_Y.save(obj, breasts.getYOffset());
		Configuration.BREASTS_OFFSET_Z.save(obj, breasts.getZOffset());
		Configuration.BREASTS_UNIBOOB.save(obj, breasts.isUniboob());
		Configuration.BREASTS_CLEAVAGE.save(obj, breasts.getCleavage());
                
		Bulge bulge = plr.getBulge();
		Configuration.BULGE_SIZE.save(obj, bulge.getSize());
		Configuration.BULGE_OFFSET_X.save(obj, bulge.getXOffset());
		Configuration.BULGE_OFFSET_Y.save(obj, bulge.getYOffset());
		Configuration.BULGE_OFFSET_Z.save(obj, bulge.getZOffset());
		return obj;
	}

	public static GenderPlayer fromJsonObject(JsonObject obj) {
		GenderPlayer plr = new GenderPlayer(Configuration.USERNAME.read(obj));
		plr.updateGender(Configuration.GENDER.read(obj));
		plr.updateBustSize(Configuration.BUST_SIZE.read(obj));
		plr.updateHurtSounds(Configuration.HURT_SOUNDS.read(obj));

		//physics
		plr.updateBreastPhysics(Configuration.BREAST_PHYSICS.read(obj));
		plr.updateArmorBreastPhysics(Configuration.BREAST_PHYSICS_ARMOR.read(obj));
		plr.updateShowBreastsInArmor(Configuration.SHOW_IN_ARMOR.read(obj));
		plr.updateBounceMultiplier(Configuration.BOUNCE_MULTIPLIER.read(obj));
		plr.updateFloppiness(Configuration.FLOPPY_MULTIPLIER.read(obj));

		Breasts breasts = plr.getBreasts();
		breasts.updateXOffset(Configuration.BREASTS_OFFSET_X.read(obj));
		breasts.updateYOffset(Configuration.BREASTS_OFFSET_Y.read(obj));
		breasts.updateZOffset(Configuration.BREASTS_OFFSET_Z.read(obj));
		breasts.updateUniboob(Configuration.BREASTS_UNIBOOB.read(obj));
		breasts.updateCleavage(Configuration.BREASTS_CLEAVAGE.read(obj));

		Bulge bulge = plr.getBulge();
		bulge.updateSize(Configuration.BULGE_SIZE.read(obj));
		bulge.updateXOffset(Configuration.BULGE_OFFSET_X.read(obj));
		bulge.updateYOffset(Configuration.BULGE_OFFSET_Y.read(obj));
		bulge.updateZOffset(Configuration.BULGE_OFFSET_Z.read(obj));
		return plr;
	}


	public static GenderPlayer loadCachedPlayer(UUID uuid, boolean markForSync) {
		GenderPlayer plr = WildfireGender.getPlayerById(uuid);
		if (plr != null) {
			plr.lockSettings = false;
			plr.syncStatus = SyncStatus.CACHED;
			Configuration config = plr.getConfig();
			plr.updateGender(config.get(Configuration.GENDER));
			plr.updateBustSize(config.get(Configuration.BUST_SIZE));
			plr.updateHurtSounds(config.get(Configuration.HURT_SOUNDS));

			//physics
			plr.updateBreastPhysics(config.get(Configuration.BREAST_PHYSICS));
			plr.updateArmorBreastPhysics(config.get(Configuration.BREAST_PHYSICS_ARMOR));
			plr.updateShowBreastsInArmor(config.get(Configuration.SHOW_IN_ARMOR));
			plr.updateBounceMultiplier(config.get(Configuration.BOUNCE_MULTIPLIER));
			plr.updateFloppiness(config.get(Configuration.FLOPPY_MULTIPLIER));

			Breasts breasts = plr.getBreasts();
			breasts.updateXOffset(config.get(Configuration.BREASTS_OFFSET_X));
			breasts.updateYOffset(config.get(Configuration.BREASTS_OFFSET_Y));
			breasts.updateZOffset(config.get(Configuration.BREASTS_OFFSET_Z));
			breasts.updateUniboob(config.get(Configuration.BREASTS_UNIBOOB));
			breasts.updateCleavage(config.get(Configuration.BREASTS_CLEAVAGE));
                        
			Bulge bulge = plr.getBulge();
			bulge.updateSize(config.get(Configuration.BULGE_SIZE));
			bulge.updateXOffset(config.get(Configuration.BULGE_OFFSET_X));
			bulge.updateYOffset(config.get(Configuration.BULGE_OFFSET_Y));
			bulge.updateZOffset(config.get(Configuration.BULGE_OFFSET_Z));
			if (markForSync) {
				plr.needsSync = true;
			}
			return plr;
		}
		return null;
	}
	
	public static void saveGenderInfo(GenderPlayer plr) {
		Configuration config = plr.getConfig();
		config.set(Configuration.USERNAME, plr.uuid);
		config.set(Configuration.GENDER, plr.getGender());
		config.set(Configuration.BUST_SIZE, plr.getBustSize());
		config.set(Configuration.HURT_SOUNDS, plr.hasHurtSounds());

		//physics
		config.set(Configuration.BREAST_PHYSICS, plr.hasBreastPhysics());
		config.set(Configuration.BREAST_PHYSICS_ARMOR, plr.hasArmorBreastPhysics());
		config.set(Configuration.SHOW_IN_ARMOR, plr.showBreastsInArmor());
		config.set(Configuration.BOUNCE_MULTIPLIER, plr.getBounceMultiplierRaw());
		config.set(Configuration.FLOPPY_MULTIPLIER, plr.getFloppiness());

		config.set(Configuration.BREASTS_OFFSET_X, plr.getBreasts().getXOffset());
		config.set(Configuration.BREASTS_OFFSET_Y, plr.getBreasts().getYOffset());
		config.set(Configuration.BREASTS_OFFSET_Z, plr.getBreasts().getZOffset());
		config.set(Configuration.BREASTS_UNIBOOB, plr.getBreasts().isUniboob());
		config.set(Configuration.BREASTS_CLEAVAGE, plr.getBreasts().getCleavage());

		config.set(Configuration.BULGE_SIZE, plr.getBulge().getSize());
		config.set(Configuration.BULGE_OFFSET_X, plr.getBulge().getXOffset());
		config.set(Configuration.BULGE_OFFSET_Y, plr.getBulge().getYOffset());
		config.set(Configuration.BULGE_OFFSET_Z, plr.getBulge().getZOffset());
                
		config.save();
		plr.needsSync = true;
	}

	public Breasts getBreasts() {
		return breasts;
	}

	public BreastPhysics getLeftBreastPhysics() {
		return lBreastPhysics;
	}
	public BreastPhysics getRightBreastPhysics() {
		return rBreastPhysics;
	}
        
        public Bulge getBulge() {
            return bulge;
        }

	public enum SyncStatus {
		CACHED, SYNCED, UNKNOWN
	}

	public enum Pronouns {
		THEY_THEM(new LiteralText("they/them").formatted(Formatting.GREEN)),
		HE_HIM(new LiteralText("he/him").formatted(Formatting.BLUE)),
		SHE_HER(new LiteralText("she/her").formatted(Formatting.LIGHT_PURPLE)),
		HE_THEY(new LiteralText("he/they").formatted(Formatting.DARK_BLUE)),
		SHE_THEY(new LiteralText("she/they").formatted(Formatting.DARK_PURPLE)),
		THEY_HE(new LiteralText("they/he").formatted(Formatting.GREEN)),
		THEY_SHE(new LiteralText("they/she").formatted(Formatting.GREEN)),
		ANY(new LiteralText("any/all").formatted(Formatting.GOLD)),
		ASK(new LiteralText("please ask").formatted(Formatting.WHITE));

		private final Text name;

		Pronouns(Text name) {
			this.name = name;
		}

		public Text getDisplayName() {
			return name;
		}

		public boolean canHaveBreasts() {
			return true;
		}
		public boolean canHaveBulge() {
                    return true;
                }
	}
}
