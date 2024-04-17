package com.wildfire.main;

import com.wildfire.api.IWildfireSound;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public enum DeathSound implements IWildfireSound {
    NOTHING("None", null),
    TOUHOU("Pichuun", "th_death"),
    INGRESS("Shaper", "ingr_death"),
    DARWINIA("taskkill", "darwin_kill"),
    OMINOUS("Snowy?", "delta_ominous"),
    BADEXPLODE("Explosion", "delta_explode"),
    UNDERTALE("Heart", "under_heart"),
    OOF("Roblox", "oof_hurt");
    private final String name;
    private final SoundEvent snd;

    DeathSound(String name, String id) {
        this.name = name;
        if (id != null) {
            this.snd = new SoundEvent(new ResourceLocation(WildfireGender.MODID, id));
        } else {
            this.snd = null;
        }
    }

    public SoundEvent getSnd() {
        return snd;
    }

    public String getName() {
        return name;
    }

    public boolean isPitch() {
        return false;
    }
}