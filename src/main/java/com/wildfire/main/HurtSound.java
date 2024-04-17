package com.wildfire.main;

import com.wildfire.api.IWildfireSound;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

@SuppressWarnings("unused")
public enum HurtSound implements IWildfireSound {
    NOTHING("None", null),
    FEMALE_HURT("Female", "female_hurt"),
    FEMALE_HURT2("Ter (F)", "female_hurt2", 1),
    FEMALE_HURT3("Femscout", "female_hurt3"),
    FEMALE_HURT4("Femsniper", "female_hurt4"),
    MALE_HURT1("Male", "male_hurt", 1),
    MALE_HURT2("Ter (M)", "male_hurt2", 1),
    MALE_HURT3("Soldier", "male_hurt3"),
    MALE_HURT4("Scout", "male_hurt4"),
    INKLING("Inkling", "squid_hurt", 0.75f),
    ROBOT("SWORDS", "robo_hurt", 0.75f);
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
        this(name, id, 0.25f);
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
}