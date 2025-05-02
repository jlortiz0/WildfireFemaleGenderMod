package com.wildfire.render.mixin;

import com.wildfire.main.DeathSound;
import com.wildfire.main.GenderPlayer;
import com.wildfire.main.HurtSound;
import com.wildfire.main.WildfireGender;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    @Shadow public abstract SoundSource getSoundSource();

    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        GenderPlayer p = WildfireGender.getPlayerById(this.uuid);
        if (p == null)
            return super.getDeathSound();
        DeathSound snd = p.getDeathSound();
        if (snd != null && snd != DeathSound.NOTHING)
            this.level.playSound(null, this, snd.getSnd(), this.getSoundSource(), this.getSoundVolume(), 1.0f);
        return super.getDeathSound();
    }

    @Override
    protected void playHurtSound(DamageSource p_21160_) {
        GenderPlayer p = WildfireGender.getPlayerById(this.uuid);
        if (p == null) {
            super.playHurtSound(p_21160_);
            return;
        }
        HurtSound snd = p.getHurtSounds();
        if (snd == null || snd == HurtSound.NOTHING) {
            super.playHurtSound(p_21160_);
            return;
        }

        float pitch = (this.random.nextFloat() - this.random.nextFloat()) * snd.getPitch() + 1;
        this.level.playSound(null, this, snd.getSnd(), this.getSoundSource(), this.getSoundVolume(), pitch);
        super.playHurtSound(p_21160_);
    }
}
