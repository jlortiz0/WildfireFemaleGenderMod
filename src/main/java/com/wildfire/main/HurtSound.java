package com.wildfire.main;

import com.wildfire.api.IWildfireSound;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

@SuppressWarnings("unused")
public enum HurtSound implements IWildfireSound {
    NOTHING("None", null),
    FEMALE_HURT("Female", "female_hurt"),
    FEMALE_HURT2("Ter (F)", "female_hurt2", 0.2f),
    FEMALE_HURT3("Femscout", "female_hurt3"),
    FEMALE_HURT4("Femsniper", "female_hurt4"),
    MALE_HURT1("Male", "male_hurt", 0.2f),
    MALE_HURT2("Ter (M)", "male_hurt2", 0.2f),
    MALE_HURT3("Soldier", "male_hurt3"),
    MALE_HURT4("Scout", "male_hurt4"),
    INKLING("Inkling", "squid_hurt", 0.15f),
    ROBOT("SWORDS", "robo_hurt", 0.15f);
    private final String name;
    private final SoundEvent snd;
    private final float pitch;

    HurtSound(String name, String id, float pitch) {
        this.name = name;
        this.pitch = pitch;
        if (id != null) {
            this.snd = new SoundEvent(new ResourceLocation(WildfireGender.MODID, id));
        } else {
            this.snd = null;
        }
    }

    HurtSound(String name, String id) {
        this(name, id, 0.05f);
    }

    public SoundEvent getSnd() {
        return snd;
    }

    public String getName() {
        return name;
    }

    public float getPitch() {
        return this.pitch;
    }

    public static void register() {}
}