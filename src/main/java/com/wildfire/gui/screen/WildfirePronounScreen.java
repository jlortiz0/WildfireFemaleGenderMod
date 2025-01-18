package com.wildfire.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.wildfire.gui.GuiUtils;
import com.wildfire.gui.WildfireButton;
import com.wildfire.gui.WildfireColorButton;
import com.wildfire.main.PronounColor;
import com.wildfire.main.entitydata.PlayerConfig;
import com.wildfire.render.ColorFontRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EditBoxWidget;
import net.minecraft.text.Text;

import java.util.UUID;

public class WildfirePronounScreen extends BaseWildfireScreen {

    private EditBoxWidget textFieldWidget;
    private final PlayerConfig aPlr;

    public WildfirePronounScreen(Screen parent, UUID plr) {
        super(Text.translatable("wildfire_gender.label.pronouns"), parent, plr);
        aPlr = getPlayer();
    }

    @Override
    public void init() {
        int x = this.width / 2;
        int y = this.height / 2 - 30;

        textFieldWidget = new EditBoxWidget(this.textRenderer, this.width / 2 - 100, y - 10, 200, 20, Text.translatable("wildfire_gender.label.pronouns"), Text.empty());
        textFieldWidget.setMaxLength(16);
        textFieldWidget.setFocused(true);
        textFieldWidget.setText(aPlr.getPronouns());
        this.addDrawableChild(this.textFieldWidget);
        this.setInitialFocus(this.textFieldWidget);

        this.addDrawableChild(new WildfireButton(this.width / 2 - 100, y + 110, 200, 20, Text.translatable("wildfire_gender.label.cancel"),
                button -> MinecraftClient.getInstance().setScreen(parent)));
        this.addDrawableChild(new WildfireButton(this.width / 2 - 100, y + 84, 200, 20, Text.translatable("wildfire_gender.label.okay"),
                button -> {
                    if (aPlr.updatePronouns(textFieldWidget.getText())) {
                        PlayerConfig.saveGenderInfo(aPlr);
                    }
                    MinecraftClient.getInstance().setScreen(parent);
                }));

        // ChatFormatting row
        for (int i = 0; i < 15; i++) {
            this.addDrawableChild(new WildfireColorButton(x - 155 + i * 21, y + 32, 18, this::setColor, this::drawFlagTooltip, PronounColor.values()[i]));
        }
        // Flags row
        for (int i = 15; i < PronounColor.values().length; i++) {
            this.addDrawableChild(new WildfireColorButton(x - 506 + i * 23, y + 53, 22, this::setColor, this::drawFlagTooltip, PronounColor.values()[i]));
        }

        super.init();
    }

    public void setColor(ButtonWidget btn) {
        if (!(btn instanceof WildfireColorButton button)) return;
        PronounColor colors = button.getColor();
        if (aPlr.updatePronounColor(colors)) {
            PlayerConfig.saveGenderInfo(aPlr);
        }
    }

    @Override
    public void render(DrawContext ctx, int f1, int f2, float f3) {
        super.renderBackground(ctx, f1, f2, f3);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int x = (this.width / 2);
        int y = (this.height / 2);

        ctx.fill(x - 166, y, x + 167, y + 48, 0x55000000);
        super.render(ctx, f1, f2, f3);

        String msg = Text.translatable("wildfire_gender.label.pronouns").getString();
        ColorFontRenderer.drawWithColors(this.textRenderer, ctx, msg, (this.width - this.textRenderer.getWidth(msg)) / 2, 20, aPlr.getPronounColor().colors);
    }

    public void drawFlagTooltip(DrawContext ctx, String text) {
        GuiUtils.drawCenteredText(ctx, this.textRenderer, Text.literal(text), this.width / 2, this.height / 2 - 60, -1);
    }
}
