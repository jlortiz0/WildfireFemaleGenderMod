/*
    Wildfire's Female Gender Mod is a female gender mod created for Minecraft.
    Copyright (C) 2023 WildfireRomeo

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.wildfire.gui.screen;

import com.wildfire.gui.GuiUtils;
import com.wildfire.gui.WildfireButton;
import com.wildfire.gui.WildfireSlider;
import com.wildfire.main.config.Configuration;
import com.wildfire.main.entitydata.Breasts;
import com.wildfire.main.entitydata.PlayerConfig;
import it.unimi.dsi.fastutil.floats.FloatConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.UUID;

@Environment(EnvType.CLIENT)
public class WildfireBreastCustomizationScreen extends BaseWildfireScreen {

    private WildfireSlider breastSlider, xOffsetBoobSlider, yOffsetBoobSlider, zOffsetBoobSlider, cleavageSlider;
    private WildfireButton btnDualPhysics, btnCustomization;
    private int currentTab = 0; // 0 = customization, 1 = presets

    public WildfireBreastCustomizationScreen(Screen parent, UUID uuid) {
        super(Text.translatable("wildfire_gender.appearance_settings.title"), parent, uuid);
    }

    @Override
    public void init() {
        int j = this.height / 2 - 11;

        PlayerConfig plr = Objects.requireNonNull(getPlayer(), "getPlayer()");
        Breasts breasts = plr.getBreasts();
        FloatConsumer onSave = value -> {
            //Just save as we updated the actual value in value change
            PlayerConfig.saveGenderInfo(plr);
        };

        this.addDrawableChild(new WildfireButton(this.width / 2 + 178, j - 60, 9, 9, Text.literal("X"),
              button -> MinecraftClient.getInstance().setScreen(parent)));

        this.addDrawableChild(this.breastSlider = new WildfireSlider(this.width / 2 + 30, j - 48, 158, 20, Configuration.BUST_SIZE, plr.getBustSize(),
              plr::updateBustSize, value -> Text.translatable("wildfire_gender.wardrobe.slider.breast_size", Math.round(value * 100)), onSave));

        //Customization
        this.addDrawableChild(this.xOffsetBoobSlider = new WildfireSlider(this.width / 2 + 30, j - 27, 158, 20, Configuration.BREASTS_OFFSET_X, breasts.getXOffset(),
              breasts::updateXOffset, value -> Text.translatable("wildfire_gender.wardrobe.slider.separation", Math.round((Math.round(value * 100f) / 100f) * 10)), onSave));
        this.addDrawableChild(this.yOffsetBoobSlider = new WildfireSlider(this.width / 2 + 30, j - 6, 158, 20, Configuration.BREASTS_OFFSET_Y, breasts.getYOffset(),
              breasts::updateYOffset, value -> Text.translatable("wildfire_gender.wardrobe.slider.height", Math.round((Math.round(value * 100f) / 100f) * 10)), onSave));
        this.addDrawableChild(this.zOffsetBoobSlider = new WildfireSlider(this.width / 2 + 30, j + 15, 158, 20, Configuration.BREASTS_OFFSET_Z, breasts.getZOffset(),
              breasts::updateZOffset, value -> Text.translatable("wildfire_gender.wardrobe.slider.depth", Math.round((Math.round(value * 100f) / 100f) * 10)), onSave));

        this.addDrawableChild(this.cleavageSlider = new WildfireSlider(this.width / 2 + 30, j + 36, 158, 20, Configuration.BREASTS_CLEAVAGE, breasts.getCleavage(),
              breasts::updateCleavage, value -> Text.translatable("wildfire_gender.wardrobe.slider.rotation", Math.round((Math.round(value * 100f) / 100f) * 100)), onSave));

        this.addDrawableChild(this.btnDualPhysics =new WildfireButton(this.width / 2 + 30, j + 57, 158, 20,
                Text.translatable("wildfire_gender.breast_customization.dual_physics", Text.translatable(breasts.isUniboob() ? "wildfire_gender.label.no" : "wildfire_gender.label.yes")), button -> {
            boolean isUniboob = !breasts.isUniboob();
            if (breasts.updateUniboob(isUniboob)) {
                button.setMessage(Text.translatable("wildfire_gender.breast_customization.dual_physics", Text.translatable(isUniboob ? "wildfire_gender.label.no" : "wildfire_gender.label.yes")));
                PlayerConfig.saveGenderInfo(plr);
            }
        }));

        this.currentTab = 0;

        super.init();
    }

    private void updatePresetTab() {
        PlayerConfig plr = getPlayer();
        if(plr == null) return;
        breastSlider.visible = currentTab == 0;
        xOffsetBoobSlider.visible = currentTab == 0;
        yOffsetBoobSlider.visible = currentTab == 0;
        zOffsetBoobSlider.visible = currentTab == 0;
        cleavageSlider.visible = currentTab == 0;
        btnDualPhysics.visible = currentTab == 0;
    }

    @Override
    public void renderBackground(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.renderBackground(ctx, mouseX, mouseY, delta);
        int x = this.width / 2;
        int y = this.height / 2;
        ctx.fill(x + 28, y - 54 - 21, x + 190, y + 68, 0x55000000);
        ctx.fill(x + 29, y - 53 - 21, x + 189, y - 60, 0x55000000);
        ctx.drawText(textRenderer, getTitle(), x + 32, y - 50 - 21, 0xFFFFFF, false);

        @SuppressWarnings("DataFlowIssue")
        PlayerEntity ent = client.world.getPlayerByUuid(this.playerUUID);
        if(ent != null) {
            int xP = this.width / 2 - 102;
            int yP = this.height / 2 + 275;
            ctx.enableScissor(this.width / 2 - 235, this.height / 2 - 150, this.width / 2 + 25, yP + 35);
            GuiUtils.drawEntityOnScreen(ctx, xP, yP, 200, -20, -20, ent);
            ctx.disableScissor();
        }
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        if(client == null || client.player == null || client.world == null) return;

        updatePresetTab();
        super.render(ctx, mouseX, mouseY, delta);

        int x = this.width / 2;
        int y = this.height / 2;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state) {
        //Ensure all sliders are saved
        breastSlider.save();
        xOffsetBoobSlider.save();
        yOffsetBoobSlider.save();
        zOffsetBoobSlider.save();
        cleavageSlider.save();
        return super.mouseReleased(mouseX, mouseY, state);
    }
}
