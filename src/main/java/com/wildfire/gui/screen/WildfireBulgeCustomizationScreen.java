/*
Wildfire's Female Gender Mod is a female gender mod created for Minecraft.
Copyright (C) 2022 WildfireRomeo

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
import com.wildfire.gui.WildfireButton;
import com.wildfire.gui.WildfireSlider;
import com.wildfire.main.Bulge;
import com.wildfire.main.GenderPlayer;
import com.wildfire.main.config.Configuration;
import it.unimi.dsi.fastutil.floats.FloatConsumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import java.util.UUID;

public class WildfireBulgeCustomizationScreen extends BaseWildfireScreen {

    private WildfireSlider bulgeSlider;

    public WildfireBulgeCustomizationScreen(Screen parent, UUID uuid) {
        super(new TranslatableText("wildfire_gender.appearance_settings.title"), parent, uuid);
    }

    @Override
    public void init() {
        int j = this.height / 2;

        GenderPlayer plr = getPlayer();
        Bulge bulge = plr.getBulge();
        FloatConsumer onSave = value -> {
            //Just save as we updated the actual value in value change
            GenderPlayer.saveGenderInfo(plr);
        };

        this.addDrawableChild(new WildfireButton(this.width / 2 + 178, j - 61, 9, 9, new TranslatableText("wildfire_gender.label.exit"),
              button -> MinecraftClient.getInstance().setScreen(parent)));

        //Customization
        this.addDrawableChild(this.bulgeSlider = new WildfireSlider(this.width / 2 + 30, j + 15, 158, 20, Configuration.BULGE_SIZE, bulge.getSize(),
                bulge::updateSize, value -> new TranslatableText("wildfire_gender.wardrobe.slider.bulge", Math.round((Math.round(value * 100f) / 100f) * 10)), onSave));

        super.init();
    }

    @Override
    public void render(MatrixStack m, int f1, int f2, float f3) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        GenderPlayer plr = getPlayer();
        super.renderBackground(m);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if(plr == null) return;

        try {
            RenderSystem.setShaderColor(1f,1.0F, 1.0F, 1.0F);
            int xP = this.width / 2 - 102;
            int yP = this.height / 2; // + 275;
            PlayerEntity ent = MinecraftClient.getInstance().world.getPlayerByUuid(this.playerUUID);
            if(ent != null) {
                WardrobeBrowserScreen.drawEntityOnScreen(xP, yP, 200, -20, -20, ent);
            } else {
                //player left, fallback
                minecraft.setScreen(new WildfirePlayerListScreen(minecraft));
            }
        } catch(Exception e) {
            //error, fallback
            minecraft.setScreen(new WildfirePlayerListScreen(minecraft));
        }

        boolean canHaveBulge = plr.getGender().canHaveBulge();
        bulgeSlider.visible = canHaveBulge;

        int x = this.width / 2;
        int y = this.height / 2;
        fill(m, x + 28, y - 64, x + 190, y + 79, 0x55000000);
        fill(m, x + 29, y - 63, x + 189, y - 50, 0x55000000);
        this.textRenderer.draw(m, getTitle(), x + 32, y - 60, 0xFFFFFF);
        super.render(m, f1, f2, f3);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state) {
        //Ensure all sliders are saved
        bulgeSlider.save();
        return super.mouseReleased(mouseX, mouseY, state);
    }
}
