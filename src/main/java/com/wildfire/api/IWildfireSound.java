package com.wildfire.api;

import net.minecraft.sounds.SoundEvent;

public interface IWildfireSound {
    SoundEvent getSnd();
    String getName();
    float getPitch();
}
