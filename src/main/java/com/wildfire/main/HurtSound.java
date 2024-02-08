package com.wildfire.main;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class HurtSound {
    private final String name;
    private final ResourceLocation id;
    private final SoundEvent snd;
    private final boolean fem;

    public HurtSound(String name, ResourceLocation id, boolean fem) {
        this.name = name;
        this.fem = fem;
        this.id = id;
        if (id != null) {
            this.snd = new SoundEvent(id);
        } else {
            this.snd = null;
        }
    }

    public HurtSound(String name, ResourceLocation id) {
        this(name, id, false);
    }

    public SoundEvent getSnd() {
        return snd;
    }

    public String getName() {
        return name;
    }

    public ResourceLocation getId() {
        return id;
    }

    public boolean isFem() {
        return this.fem;
    }
}