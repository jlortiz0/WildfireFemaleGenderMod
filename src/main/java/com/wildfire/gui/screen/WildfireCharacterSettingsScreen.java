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

package com.wildfire.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wildfire.gui.WildfireButton;
import com.wildfire.gui.WildfireSlider;
import com.wildfire.gui.WildfireToggleButton;
import com.wildfire.main.GenderPlayer;
import com.wildfire.main.HurtSound;
import com.wildfire.main.WildfireGender;
import com.wildfire.main.config.ClientConfiguration;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

public class WildfireCharacterSettingsScreen extends BaseWildfireScreen {

    private static final Component ENABLED = Component.translatable("wildfire_gender.label.enabled").withStyle(ChatFormatting.GREEN);
    private static final Component DISABLED = Component.translatable("wildfire_gender.label.disabled").withStyle(ChatFormatting.RED);

    private WildfireSlider bounceSlider, floppySlider;
    private static final ResourceLocation BACKGROUND = new ResourceLocation(WildfireGender.MODID, "textures/gui/settings_bg.png");
    private int yPos = 0;
    private boolean bounceWarning;

    protected WildfireCharacterSettingsScreen(Screen parent, UUID uuid) {
        super(Component.translatable("wildfire_gender.char_settings.title"), parent, uuid);
    }

    @Override
    public void init() {
        GenderPlayer aPlr = getPlayer();

        int rxPos = this.width / 2;
        int y = this.height / 2;

        yPos = y - 66;
        int lxPos = rxPos - 240 / 2 - 1;

        //Add 'Close' button at beginning
        this.addRenderableWidget(new WildfireButton(rxPos + 112, yPos - 12, 9, 9, Component.translatable("wildfire_gender.label.exit"),
                button -> Minecraft.getInstance().setScreen(parent)));

        this.addRenderableWidget(new WildfireToggleButton(lxPos, yPos, 119, 20,
                Component.translatable("wildfire_gender.char_settings.physics"), button -> {
            boolean enablePhysics = !aPlr.hasBreastPhysics();
            if (aPlr.updateBreastPhysics(enablePhysics)) {
                GenderPlayer.saveGenderInfo(aPlr);
            }
        }, aPlr::hasBreastPhysics));

        final WildfireButton armorButton, hitSoundButton;
        this.addRenderableWidget(armorButton = new WildfireToggleButton(rxPos, yPos, 119, 20,
                Component.translatable("wildfire_gender.char_settings.armor_physics"), button -> {
            boolean enablePhysicsArmor = !aPlr.hasArmorBreastPhysics();
            if (aPlr.updateArmorBreastPhysics(enablePhysicsArmor)) {
                GenderPlayer.saveGenderInfo(aPlr);
            }
        }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, Component.translatable("wildfire_gender.tooltip.armor_physics"), mouseX, mouseY), aPlr::hasArmorBreastPhysics));
        armorButton.active = aPlr.showBreastsInArmor();

        this.addRenderableWidget(new WildfireToggleButton(lxPos, yPos + 21, 119, 19,
              Component.translatable("wildfire_gender.char_settings.hide_in_armor"), button -> {
            boolean enableShowInArmor = !aPlr.showBreastsInArmor();
            if (aPlr.updateShowBreastsInArmor(enableShowInArmor)) {
                GenderPlayer.saveGenderInfo(aPlr);
                armorButton.active = aPlr.showBreastsInArmor();
            }
        }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, Component.translatable("wildfire_gender.tooltip.hide_in_armor"), mouseX, mouseY), aPlr::showBreastsInArmor));

        this.addRenderableWidget(this.bounceSlider = new WildfireSlider(lxPos, yPos + 40, 240, 22, ClientConfiguration.BOUNCE_MULTIPLIER, aPlr.getBounceMultiplierRaw(), value -> {
        }, value -> {
            float bounceText = 3 * value;
            float v = Math.round(bounceText * 10) / 10f;
            bounceWarning = v > 1;
            if (v == 3) {
                return Component.translatable("wildfire_gender.slider.max_bounce");
            } else if (Math.round(bounceText * 100) / 100f == 0) {
                return Component.translatable("wildfire_gender.slider.min_bounce");
            }
            return Component.translatable("wildfire_gender.slider.bounce", v);
        }, value -> {
            if (aPlr.updateBounceMultiplier(value)) {
                GenderPlayer.saveGenderInfo(aPlr);
            }
        }));

        this.addRenderableWidget(this.floppySlider = new WildfireSlider(lxPos, yPos + 61, 240, 22, ClientConfiguration.FLOPPY_MULTIPLIER, aPlr.getFloppiness(), value -> {
        }, value -> Component.translatable("wildfire_gender.slider.floppy", Math.round(value * 100)), value -> {
            if (aPlr.updateFloppiness(value)) {
                GenderPlayer.saveGenderInfo(aPlr);
            }
        }));

        this.addRenderableWidget(hitSoundButton = new WildfireToggleButton(rxPos, yPos + 83, 119, 20,
                Component.translatable("wildfire_gender.char_settings.replace_hurt_sounds"), button -> {
            boolean replaceHurtSounds = !aPlr.replaceHurtSounds();
            if (aPlr.updateReplaceHurtSounds(replaceHurtSounds)) {
                GenderPlayer.saveGenderInfo(aPlr);
            }
        }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, Component.translatable("wildfire_gender.tooltip.replace_hurt_sounds"), mouseX, mouseY), () -> !aPlr.replaceHurtSounds()));
        hitSoundButton.active = aPlr.getHurtSounds() != HurtSound.NOTHING;

        this.addRenderableWidget(new WildfireButton(lxPos, yPos + 83, 119, 20,
              Component.translatable("wildfire_gender.char_settings.hurt_sounds", aPlr.getHurtSounds().getName()), button -> {
            minecraft.setScreen(new WildfireSoundListScreen<>(minecraft, aPlr.uuid, this, GenderPlayer::getHurtSounds, GenderPlayer::updateHurtSounds));
            button.setMessage(Component.translatable("wildfire_gender.char_settings.hurt_sounds", aPlr.getHurtSounds().getName()));
            hitSoundButton.active = aPlr.getHurtSounds() != HurtSound.NOTHING;
        }));

        this.addRenderableWidget(new WildfireToggleButton(rxPos, yPos + 104, 119, 19,
                Component.translatable("wildfire_gender.char_settings.bilkable"), button -> {
            boolean bilkable = !aPlr.isBilkable();
            if (aPlr.updateBilkable(bilkable)) {
                GenderPlayer.saveGenderInfo(aPlr);
            }
        }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, Component.translatable("wildfire_gender.tooltip.bilkable"), mouseX, mouseY), aPlr::isBilkable));

        super.init();
    }

    @Override
    public void render(@Nonnull PoseStack m, int f1, int f2, float f3) {
        super.renderBackground(m);
        Player plrEntity = Minecraft.getInstance().level.getPlayerByUUID(this.playerUUID);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        int i = (this.width - 256) / 2;
        int j = (this.height - 164) / 2;
        blit(m, i, j, 0, 0, 256, 184);

        int x = this.width / 2;
        int y = this.height / 2;

        font.draw(m, title, x - font.width(title) / 2, yPos - 11, 4473924);

        super.render(m, f1, f2, f3);

        if(plrEntity != null) {
            Screen.drawCenteredString(m, this.font, plrEntity.getDisplayName(), x, yPos - 30, 0xFFFFFF);
        }

        if(bounceWarning) {
            Screen.drawCenteredString(m, font, Component.translatable("wildfire_gender.tooltip.bounce_warning").withStyle(ChatFormatting.ITALIC), x, y+100, 0xFF6666);
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state) {
        //Ensure all sliders are saved
        bounceSlider.save();
        floppySlider.save();
        return super.mouseReleased(mouseX, mouseY, state);
    }
}