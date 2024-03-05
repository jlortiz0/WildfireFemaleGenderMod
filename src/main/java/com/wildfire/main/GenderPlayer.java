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

import com.google.gson.JsonObject;
import com.wildfire.main.config.ClientConfiguration;
import com.wildfire.main.config.ConfigKey;
import com.wildfire.physics.BreastPhysics;

import java.awt.*;
import java.util.UUID;
import java.util.function.Consumer;

public class GenderPlayer {

	public boolean needsSync;
	public final UUID uuid;
	private String pronouns;
	private int[] pronounColor = ClientConfiguration.PRONOUN_COLOR.getDefault();
    private float pBustSize = ClientConfiguration.BUST_SIZE.getDefault();

	private HurtSound hurtSounds = ClientConfiguration.HURT_SOUNDS.getDefault();
	private boolean replaceHurtSound = ClientConfiguration.REPLACE_HURT_SOUND.getDefault();

	//physics variables
	private boolean breastPhysics = ClientConfiguration.BREAST_PHYSICS.getDefault();
	private boolean armorBreastPhysics = ClientConfiguration.BREAST_PHYSICS_ARMOR.getDefault();
	private float bounceMultiplier = ClientConfiguration.BOUNCE_MULTIPLIER.getDefault();
	private float floppyMultiplier = ClientConfiguration.FLOPPY_MULTIPLIER.getDefault();

	public boolean lockSettings = false;

	public SyncStatus syncStatus = SyncStatus.UNKNOWN;
	private boolean showBreastsInArmor = ClientConfiguration.SHOW_IN_ARMOR.getDefault();

	private final ClientConfiguration cfg;
	private final BreastPhysics lBreastPhysics, rBreastPhysics;
	private final Breasts breasts;
	private boolean bilkable;

	public GenderPlayer(UUID uuid) {
		this(uuid, ClientConfiguration.PRONOUNS.getDefault());
	}

	public GenderPlayer(UUID uuid, String pronouns) {
		lBreastPhysics = new BreastPhysics(this);
		rBreastPhysics = new BreastPhysics(this);
		breasts = new Breasts();
		this.uuid = uuid;
		this.pronouns = pronouns;
		this.cfg = new ClientConfiguration("WildfireGender", this.uuid.toString());
		this.cfg.set(ClientConfiguration.USERNAME, this.uuid);
		this.cfg.setDefault(ClientConfiguration.PRONOUNS);
		this.cfg.setDefault(ClientConfiguration.PRONOUN_COLOR);
		this.cfg.setDefault(ClientConfiguration.BUST_SIZE);
		this.cfg.setDefault(ClientConfiguration.HURT_SOUNDS);

		this.cfg.setDefault(ClientConfiguration.BREASTS_OFFSET_X);
		this.cfg.setDefault(ClientConfiguration.BREASTS_OFFSET_Y);
		this.cfg.setDefault(ClientConfiguration.BREASTS_OFFSET_Z);
		this.cfg.setDefault(ClientConfiguration.BREASTS_UNIBOOB);
		this.cfg.setDefault(ClientConfiguration.BREASTS_CLEAVAGE);

		this.cfg.setDefault(ClientConfiguration.BREAST_PHYSICS);
		this.cfg.setDefault(ClientConfiguration.BREAST_PHYSICS_ARMOR);
		this.cfg.setDefault(ClientConfiguration.SHOW_IN_ARMOR);
		this.cfg.setDefault(ClientConfiguration.BOUNCE_MULTIPLIER);
		this.cfg.setDefault(ClientConfiguration.FLOPPY_MULTIPLIER);

		this.cfg.setDefault(ClientConfiguration.BILKABLE);
		this.cfg.finish();
	}

	public ClientConfiguration getConfig() {
		return cfg;
	}

	private <VALUE> boolean updateValue(ConfigKey<VALUE> key, VALUE value, Consumer<VALUE> setter) {
		if (key.validate(value)) {
			setter.accept(value);
			return true;
		}
		return false;
	}

	public String getPronouns() {
		return pronouns;
	}

	public boolean updatePronouns(String value) {
		return updateValue(ClientConfiguration.PRONOUNS, value, v -> {
			this.pronouns = v;
		});
	}

	public int[] getPronounColor() {
		return pronounColor;
	}

	public boolean updatePronounColor(int[] value) {
		return updateValue(ClientConfiguration.PRONOUN_COLOR, value, v -> {
			this.pronounColor = v;
		});
	}

	private static final int ticksPerColor = 13;

	public int getPronounColorOnTick(int tick) {
		if (pronounColor.length == 1) {
			return pronounColor[0];
		}
		int ind = (tick / ticksPerColor) % pronounColor.length;
		float per = (float) (tick % ticksPerColor) / ticksPerColor;
		float invPer = 1 - per;
		Color color1 = new Color(pronounColor[ind]);
		Color color2;
		if (ind + 1 == pronounColor.length) {
			color2 = new Color(pronounColor[0]);
		} else {
			color2 = new Color(pronounColor[ind + 1]);
		}
		Color output = new Color((color1.getRed() * invPer + color2.getRed() * per) / 255,
				(color1.getGreen() * invPer + color2.getGreen() * per) / 255, (color1.getBlue() * invPer + color2.getBlue() * per) / 255);

		return output.getRGB();
	}

	public float getBustSize() {
		return pBustSize;
	}

	public boolean updateBustSize(float value) {
		return updateValue(ClientConfiguration.BUST_SIZE, value, v -> this.pBustSize = v);
	}

	public HurtSound getHurtSounds() {
		return hurtSounds;
	}

	public boolean updateHurtSounds(HurtSound value) {
		return updateValue(ClientConfiguration.HURT_SOUNDS, value, v -> this.hurtSounds = v);
	}

	public boolean replaceHurtSounds() {
		return replaceHurtSound;
	}

	public boolean updateReplaceHurtSounds(boolean value) {
		return updateValue(ClientConfiguration.REPLACE_HURT_SOUND, value, v -> this.replaceHurtSound = v);
	}

	public boolean hasBreastPhysics() {
		return breastPhysics;
	}

	public boolean updateBreastPhysics(boolean value) {
		return updateValue(ClientConfiguration.BREAST_PHYSICS, value, v -> this.breastPhysics = v);
	}

	public boolean hasArmorBreastPhysics() {
		return armorBreastPhysics;
	}

	public boolean updateArmorBreastPhysics(boolean value) {
		return updateValue(ClientConfiguration.BREAST_PHYSICS_ARMOR, value, v -> this.armorBreastPhysics = v);
	}

	public boolean showBreastsInArmor() {
		return showBreastsInArmor;
	}

	public boolean updateShowBreastsInArmor(boolean value) {
		return updateValue(ClientConfiguration.SHOW_IN_ARMOR, value, v -> this.showBreastsInArmor = v);
	}

	public float getBounceMultiplier() {
		return Math.round((this.getBounceMultiplierRaw() * 3) * 100) / 100f;
	}

	public float getBounceMultiplierRaw() {
		return bounceMultiplier;
	}

	public boolean updateBounceMultiplier(float value) {
		return updateValue(ClientConfiguration.BOUNCE_MULTIPLIER, value, v -> this.bounceMultiplier = v);
	}

	public float getFloppiness() {
		return this.floppyMultiplier;
	}

	public boolean updateFloppiness(float value) {
		return updateValue(ClientConfiguration.FLOPPY_MULTIPLIER, value, v -> this.floppyMultiplier = v);
	}

	public boolean isBilkable() {
		return bilkable;
	}

	public boolean updateBilkable(boolean value) {
		return updateValue(ClientConfiguration.BILKABLE, value, v -> this.bilkable = v);
	}

	public SyncStatus getSyncStatus() {
		return this.syncStatus;
	}

	public static JsonObject toJsonObject(GenderPlayer plr) {
		JsonObject obj = new JsonObject();
		ClientConfiguration.USERNAME.save(obj, plr.uuid);
		ClientConfiguration.PRONOUNS.save(obj, plr.getPronouns());
		ClientConfiguration.PRONOUN_COLOR.save(obj, plr.getPronounColor());
		ClientConfiguration.BUST_SIZE.save(obj, plr.getBustSize());
		ClientConfiguration.HURT_SOUNDS.save(obj, plr.getHurtSounds());
		ClientConfiguration.REPLACE_HURT_SOUND.save(obj, plr.replaceHurtSounds());

		ClientConfiguration.BREAST_PHYSICS.save(obj, plr.hasBreastPhysics());
		ClientConfiguration.BREAST_PHYSICS_ARMOR.save(obj, plr.hasArmorBreastPhysics());
		ClientConfiguration.SHOW_IN_ARMOR.save(obj, plr.showBreastsInArmor());
		ClientConfiguration.BOUNCE_MULTIPLIER.save(obj, plr.getBounceMultiplierRaw());
		ClientConfiguration.FLOPPY_MULTIPLIER.save(obj, plr.getFloppiness());

		Breasts breasts = plr.getBreasts();
		ClientConfiguration.BREASTS_OFFSET_X.save(obj, breasts.getXOffset());
		ClientConfiguration.BREASTS_OFFSET_Y.save(obj, breasts.getYOffset());
		ClientConfiguration.BREASTS_OFFSET_Z.save(obj, breasts.getZOffset());
		ClientConfiguration.BREASTS_UNIBOOB.save(obj, breasts.isUniboob());
		ClientConfiguration.BREASTS_CLEAVAGE.save(obj, breasts.getCleavage());

		ClientConfiguration.BILKABLE.save(obj, plr.isBilkable());
		return obj;
	}

	public static GenderPlayer fromJsonObject(JsonObject obj) {
		GenderPlayer plr = new GenderPlayer(ClientConfiguration.USERNAME.read(obj));
		plr.updatePronouns(ClientConfiguration.PRONOUNS.read(obj));
		plr.updatePronounColor(ClientConfiguration.PRONOUN_COLOR.read(obj));
		plr.updateBustSize(ClientConfiguration.BUST_SIZE.read(obj));
		plr.updateHurtSounds(ClientConfiguration.HURT_SOUNDS.read(obj));
		plr.updateReplaceHurtSounds(ClientConfiguration.REPLACE_HURT_SOUND.read(obj));

		//physics
		plr.updateBreastPhysics(ClientConfiguration.BREAST_PHYSICS.read(obj));
		plr.updateArmorBreastPhysics(ClientConfiguration.BREAST_PHYSICS_ARMOR.read(obj));
		plr.updateShowBreastsInArmor(ClientConfiguration.SHOW_IN_ARMOR.read(obj));
		plr.updateBounceMultiplier(ClientConfiguration.BOUNCE_MULTIPLIER.read(obj));
		plr.updateFloppiness(ClientConfiguration.FLOPPY_MULTIPLIER.read(obj));

		Breasts breasts = plr.getBreasts();
		breasts.updateXOffset(ClientConfiguration.BREASTS_OFFSET_X.read(obj));
		breasts.updateYOffset(ClientConfiguration.BREASTS_OFFSET_Y.read(obj));
		breasts.updateZOffset(ClientConfiguration.BREASTS_OFFSET_Z.read(obj));
		breasts.updateUniboob(ClientConfiguration.BREASTS_UNIBOOB.read(obj));
		breasts.updateCleavage(ClientConfiguration.BREASTS_CLEAVAGE.read(obj));

		plr.updateBilkable(ClientConfiguration.BILKABLE.read(obj));
		return plr;
	}


	public static GenderPlayer loadCachedPlayer(UUID uuid, boolean markForSync) {
		GenderPlayer plr = WildfireGender.getPlayerById(uuid);
		if (plr != null) {
			plr.lockSettings = false;
			plr.syncStatus = SyncStatus.CACHED;
			ClientConfiguration config = plr.getConfig();
			plr.updatePronouns(config.get(ClientConfiguration.PRONOUNS));
			plr.updatePronounColor(config.get(ClientConfiguration.PRONOUN_COLOR));
			plr.updateBustSize(config.get(ClientConfiguration.BUST_SIZE));
			plr.updateHurtSounds(config.get(ClientConfiguration.HURT_SOUNDS));
			plr.updateReplaceHurtSounds(config.get(ClientConfiguration.REPLACE_HURT_SOUND));

			//physics
			plr.updateBreastPhysics(config.get(ClientConfiguration.BREAST_PHYSICS));
			plr.updateArmorBreastPhysics(config.get(ClientConfiguration.BREAST_PHYSICS_ARMOR));
			plr.updateShowBreastsInArmor(config.get(ClientConfiguration.SHOW_IN_ARMOR));
			plr.updateBounceMultiplier(config.get(ClientConfiguration.BOUNCE_MULTIPLIER));
			plr.updateFloppiness(config.get(ClientConfiguration.FLOPPY_MULTIPLIER));

			Breasts breasts = plr.getBreasts();
			breasts.updateXOffset(config.get(ClientConfiguration.BREASTS_OFFSET_X));
			breasts.updateYOffset(config.get(ClientConfiguration.BREASTS_OFFSET_Y));
			breasts.updateZOffset(config.get(ClientConfiguration.BREASTS_OFFSET_Z));
			breasts.updateUniboob(config.get(ClientConfiguration.BREASTS_UNIBOOB));
			breasts.updateCleavage(config.get(ClientConfiguration.BREASTS_CLEAVAGE));

			plr.updateBilkable(config.get(ClientConfiguration.BILKABLE));
			if (markForSync) {
				plr.needsSync = true;
			}
			return plr;
		}
		return null;
	}
	
	public static void saveGenderInfo(GenderPlayer plr) {
		ClientConfiguration config = plr.getConfig();
		config.set(ClientConfiguration.USERNAME, plr.uuid);
		config.set(ClientConfiguration.PRONOUNS, plr.getPronouns());
		config.set(ClientConfiguration.PRONOUN_COLOR, plr.getPronounColor());
		config.set(ClientConfiguration.BUST_SIZE, plr.getBustSize());
		config.set(ClientConfiguration.HURT_SOUNDS, plr.getHurtSounds());
		config.set(ClientConfiguration.REPLACE_HURT_SOUND, plr.replaceHurtSounds());

		//physics
		config.set(ClientConfiguration.BREAST_PHYSICS, plr.hasBreastPhysics());
		config.set(ClientConfiguration.BREAST_PHYSICS_ARMOR, plr.hasArmorBreastPhysics());
		config.set(ClientConfiguration.SHOW_IN_ARMOR, plr.showBreastsInArmor());
		config.set(ClientConfiguration.BOUNCE_MULTIPLIER, plr.getBounceMultiplierRaw());
		config.set(ClientConfiguration.FLOPPY_MULTIPLIER, plr.getFloppiness());

		config.set(ClientConfiguration.BREASTS_OFFSET_X, plr.getBreasts().getXOffset());
		config.set(ClientConfiguration.BREASTS_OFFSET_Y, plr.getBreasts().getYOffset());
		config.set(ClientConfiguration.BREASTS_OFFSET_Z, plr.getBreasts().getZOffset());
		config.set(ClientConfiguration.BREASTS_UNIBOOB, plr.getBreasts().isUniboob());
		config.set(ClientConfiguration.BREASTS_CLEAVAGE, plr.getBreasts().getCleavage());

		config.set(ClientConfiguration.BILKABLE, plr.isBilkable());
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

	public enum SyncStatus {
		CACHED, SYNCED, UNKNOWN
	}
}
