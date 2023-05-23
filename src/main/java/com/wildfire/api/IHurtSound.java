package com.wildfire.api;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public interface IHurtSound {
    String getName();
    SoundEvent getSnd();
    Identifier getId();
}
