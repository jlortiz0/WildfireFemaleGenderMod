package com.wildfire.main;

import com.wildfire.api.IWildfireSound;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public enum HurtSound implements IWildfireSound {
    NOTHING("None", null),
    FEMALE_HURT("Female", "female_hurt"),
    FEMALE_HURT2("Terraria (F)", "female_hurt2", true),
    FEMALE_HURT3("Femscout", "female_hurt3"),
    FEMALE_HURT4("Femsniper", "female_hurt4"),
    MALE_HURT1("Male", "male_hurt", true),
    MALE_HURT2("Terraria (M)", "male_hurt2", true),
    MALE_HURT3("Soldier", "male_hurt3"),
    MALE_HURT4("Scout", "male_hurt4"),
    INKLING("Inkling", "squid_hurt", true),
    ROBOT("SWORDSMACHINE", "robo_hurt", true);
    private final String name;
    private final SoundEvent snd;
    private final boolean pitch;

    HurtSound(String name, String id, boolean pitch) {
        this.name = name;
        this.pitch = pitch;
        if (id != null) {
            this.snd = new SoundEvent(new ResourceLocation(WildfireGender.MODID, id));
        } else {
            this.snd = null;
        }
    }

    HurtSound(String name, String id) {
        this(name, id, false);
    }

    public SoundEvent getSnd() {
        return snd;
    }

    public String getName() {
        return name;
    }

    public boolean isPitch() {
        return this.pitch;
    }
}