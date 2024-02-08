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
import com.wildfire.main.GenderPlayer;
import com.wildfire.main.WildfireGender;
import com.wildfire.main.WildfireSounds;
import com.wildfire.main.config.ClientConfiguration;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

public class WildfireCharacterSettingsScreen extends BaseWildfireScreen {

    private static final Component ENABLED = new TranslatableComponent("wildfire_gender.label.enabled").withStyle(ChatFormatting.GREEN);
    private static final Component DISABLED = new TranslatableComponent("wildfire_gender.label.disabled").withStyle(ChatFormatting.RED);

    private WildfireSlider bounceSlider, floppySlider;
    private ResourceLocation BACKGROUND;
    private int yPos = 0;
    private boolean bounceWarning;

    protected WildfireCharacterSettingsScreen(Screen parent, UUID uuid) {
        super(new TranslatableComponent("wildfire_gender.char_settings.title"), parent, uuid);
    }

    @Override
    public void init() {
        GenderPlayer aPlr = getPlayer();

        int x = this.width / 2;
        int y = this.height / 2;

        yPos = y - 47;
        int xPos = x - 156 / 2 - 1;

        //Add 'Close' button at beginning
        this.addRenderableWidget(new WildfireButton(this.width / 2 + 73, yPos - 11, 9, 9, new TranslatableComponent("wildfire_gender.label.exit"),
                button -> Minecraft.getInstance().setScreen(parent)));

        this.addRenderableWidget(new WildfireButton(xPos, yPos, 157, 20,
              new TranslatableComponent("wildfire_gender.char_settings.physics", aPlr.hasBreastPhysics() ? ENABLED : DISABLED), button -> {
            boolean enablePhysics = !aPlr.hasBreastPhysics();
            if (aPlr.updateBreastPhysics(enablePhysics)) {
                button.setMessage(new TranslatableComponent("wildfire_gender.char_settings.physics", enablePhysics ? ENABLED : DISABLED));
                GenderPlayer.saveGenderInfo(aPlr);
            }
        }, (button, matrices, mouseX, mouseY) -> this.renderTooltip(matrices, new TranslatableComponent("wildfire_gender.tooltip.breast_physics"), mouseX, mouseY)));



        this.addRenderableWidget(new WildfireButton(xPos, yPos + 20, 157, 20,
              new TranslatableComponent("wildfire_gender.char_settings.armor_physics", aPlr.hasArmorBreastPhysics() ? ENABLED : DISABLED), button -> {
            boolean enablePhysicsArmor = !aPlr.hasArmorBreastPhysics();
            if (aPlr.updateArmorBreastPhysics(enablePhysicsArmor)) {
                button.setMessage(new TranslatableComponent("wildfire_gender.char_settings.armor_physics", enablePhysicsArmor ? ENABLED : DISABLED));
                GenderPlayer.saveGenderInfo(aPlr);
            }
        }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, new TranslatableComponent("wildfire_gender.tooltip.armor_physics"), mouseX, mouseY)));

        this.addRenderableWidget(new WildfireButton(xPos, yPos + 40, 157, 20,
              new TranslatableComponent("wildfire_gender.char_settings.hide_in_armor", aPlr.showBreastsInArmor() ? DISABLED : ENABLED), button -> {
            boolean enableShowInArmor = !aPlr.showBreastsInArmor();
            if (aPlr.updateShowBreastsInArmor(enableShowInArmor)) {
                button.setMessage(new TranslatableComponent("wildfire_gender.char_settings.hide_in_armor", enableShowInArmor ? DISABLED : ENABLED));
                GenderPlayer.saveGenderInfo(aPlr);
            }
        }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, new TranslatableComponent("wildfire_gender.tooltip.hide_in_armor"), mouseX, mouseY)));

        this.addRenderableWidget(this.bounceSlider = new WildfireSlider(xPos, yPos + 60, 158, 22, ClientConfiguration.BOUNCE_MULTIPLIER, aPlr.getBounceMultiplierRaw(), value -> {
        }, value -> {
            float bounceText = 3 * value;
            float v = Math.round(bounceText * 10) / 10f;
            bounceWarning = v > 1;
            if (v == 3) {
                return new TranslatableComponent("wildfire_gender.slider.max_bounce");
            } else if (Math.round(bounceText * 100) / 100f == 0) {
                return new TranslatableComponent("wildfire_gender.slider.min_bounce");
            }
            return new TranslatableComponent("wildfire_gender.slider.bounce", v);
        }, value -> {
            if (aPlr.updateBounceMultiplier(value)) {
                GenderPlayer.saveGenderInfo(aPlr);
            }
        }));

        this.addRenderableWidget(this.floppySlider = new WildfireSlider(xPos, yPos + 80, 158, 22, ClientConfiguration.FLOPPY_MULTIPLIER, aPlr.getFloppiness(), value -> {
        }, value -> new TranslatableComponent("wildfire_gender.slider.floppy", Math.round(value * 100)), value -> {
            if (aPlr.updateFloppiness(value)) {
                GenderPlayer.saveGenderInfo(aPlr);
            }
        }));

        this.addRenderableWidget(new WildfireButton(xPos, yPos + 100, 157, 20,
              new TranslatableComponent("wildfire_gender.char_settings.hurt_sounds", WildfireSounds.get(aPlr.getHurtSounds()).getName()), button -> {
            minecraft.setScreen(new WildfireHurtSoundListScreen(minecraft, aPlr.uuid));
            button.setMessage(new TranslatableComponent("wildfire_gender.char_settings.hurt_sounds", WildfireSounds.get(aPlr.getHurtSounds()).getName()));
        }, (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, new TranslatableComponent("wildfire_gender.tooltip.hurt_sounds"), mouseX, mouseY)));

        this.BACKGROUND = new ResourceLocation(WildfireGender.MODID, "textures/gui/settings_bg.png");

        super.init();
    }

    @Override
    public void render(@Nonnull PoseStack m, int f1, int f2, float f3) {
        super.renderBackground(m);
        Player plrEntity = Minecraft.getInstance().level.getPlayerByUUID(this.playerUUID);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if(this.BACKGROUND != null) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, this.BACKGROUND);
        }
        int i = (this.width - 172) / 2;
        int j = (this.height - 124) / 2;
        blit(m, i, j, 0, 0, 172, 144);

        int x = this.width / 2;
        int y = this.height / 2;

        this.font.draw(m, title, x - 79, yPos - 10, 4473924);

        super.render(m, f1, f2, f3);

        if(plrEntity != null) {
            Screen.drawCenteredString(m, this.font, plrEntity.getDisplayName(), x, yPos - 30, 0xFFFFFF);
        }

        if(bounceWarning) {
            Screen.drawCenteredString(m, font, new TranslatableComponent("wildfire_gender.tooltip.bounce_warning").withStyle(ChatFormatting.ITALIC), x, y+90, 0xFF6666);
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