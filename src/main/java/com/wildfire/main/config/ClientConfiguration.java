package com.wildfire.main.config;

import com.wildfire.main.DeathSound;
import com.wildfire.main.HurtSound;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ClientConfiguration extends Configuration {

    public static final UUIDConfigKey USERNAME = new UUIDConfigKey("username", UUID.nameUUIDFromBytes("UNKNOWN".getBytes(StandardCharsets.UTF_8)));
    public static final StringConfigKey PRONOUNS = new StringConfigKey("gender", "please ask");
    public static final ColorConfigKey PRONOUN_COLOR = new ColorConfigKey("gender_color", 0xFFFFFF);
    public static final FloatConfigKey BUST_SIZE = new FloatConfigKey("bust_size", 0, 0, 1.5F);
    public static final EnumConfigKey<HurtSound> HURT_SOUNDS = new EnumConfigKey<>("hurt_sounds", HurtSound.class);
    public static final EnumConfigKey<DeathSound> DEATH_SOUND = new EnumConfigKey<>("death_sound", DeathSound.class);
    public static final BooleanConfigKey REPLACE_HURT_SOUND = new BooleanConfigKey("replace_hurt_sound", false);

    public static final FloatConfigKey BREASTS_OFFSET_X = new FloatConfigKey("breasts_xOffset", 0.0F, -1, 1);
    public static final FloatConfigKey BREASTS_OFFSET_Y = new FloatConfigKey("breasts_yOffset", 0.0F, -1, 1);
    public static final FloatConfigKey BREASTS_OFFSET_Z = new FloatConfigKey("breasts_zOffset", 0.0F, -1, 0);
    public static final BooleanConfigKey BREASTS_UNIBOOB = new BooleanConfigKey("breasts_uniboob", true);
    public static final FloatConfigKey BREASTS_CLEAVAGE = new FloatConfigKey("breasts_cleavage", 0.05F, 0, 0.1F);

    public static final BooleanConfigKey BREAST_PHYSICS = new BooleanConfigKey("breast_physics", true);
    public static final BooleanConfigKey BREAST_PHYSICS_ARMOR = new BooleanConfigKey("breast_physics_armor", false);
    public static final BooleanConfigKey SHOW_IN_ARMOR = new BooleanConfigKey("show_in_armor", true);
    public static final FloatConfigKey BOUNCE_MULTIPLIER = new FloatConfigKey("bounce_multiplier", 0.34F, 0, 1);
    public static final FloatConfigKey FLOPPY_MULTIPLIER = new FloatConfigKey("floppy_multiplier", 0.95F, 0, 1);

    public static final BooleanConfigKey BILKABLE = new BooleanConfigKey("canon_cock", false);


    public ClientConfiguration(String saveLoc, String cfgName) {
        super(saveLoc, cfgName);
    }
}
