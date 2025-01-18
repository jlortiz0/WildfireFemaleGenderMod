package com.wildfire.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.wildfire.gui.GuiUtils;
import com.wildfire.gui.WildfireButton;
import com.wildfire.gui.WildfirePlayerList;
import com.wildfire.main.WildfireGender;
import com.wildfire.main.entitydata.PlayerConfig;
import com.wildfire.render.ColorFontRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.Calendar;
import java.util.UUID;

public class WildfirePlayerListScreen extends Screen {
    private static final Identifier TXTR_BACKGROUND = Identifier.of(WildfireGender.MODID, "textures/gui/player_list.png");
    private static final Identifier TXTR_RIBBON = Identifier.of(WildfireGender.MODID, "textures/bc_ribbon.png");
    private static final UUID CREATOR_UUID = UUID.fromString("23b6feed-2dfe-4f2e-9429-863fd4adb946");
    private static final boolean isBreastCancerAwarenessMonth = Calendar.getInstance().get(Calendar.MONTH) == Calendar.OCTOBER;

    @Nullable
    private Text tooltip = null;

    public static PlayerConfig HOVER_PLAYER;

    WildfirePlayerList PLAYER_LIST;
    private final MinecraftClient client;
    public WildfirePlayerListScreen(MinecraftClient mc) {
        super(Text.translatable("wildfire_gender.player_list.title"));
        this.client = mc;
    }

    @Override
    public boolean shouldPause() { return false; }

    @Override
    public void init() {
        int x = this.width / 2;
        int y = this.height / 2 - 20;
		/*this.addButton(new SteinButton(this.width / 2 - 60, y + 75, 66, 15, new TranslationTextComponent("wildfire_gender.player_list.settings_button"), button -> {
			mc.displayGuiScreen(new WildfireSettingsScreen(SteinPlayerListScreen.this));
		}));*/

        PLAYER_LIST = new WildfirePlayerList(this, client, 118, (y - 61), (y + 71));
        this.addSelectableChild(this.PLAYER_LIST);

        this.addDrawableChild(new WildfireButton(this.width / 2 + 52, y - 74, 9, 9, Text.literal("X"), button -> client.setScreen(null)));

        super.init();
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float partialTicks) {
        HOVER_PLAYER = null;
        tooltip = null;
        PLAYER_LIST.refreshList();

        super.renderBackground(ctx, mouseX, mouseY, partialTicks);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.width - 132) / 2;
        int j = (this.height - 156) / 2 - 20;
        ctx.drawTexture(TXTR_BACKGROUND, i, j, 0, 0, 192, 174, 256, 256);

        int x = (this.width / 2);
        int y = (this.height / 2) - 20;

        super.render(ctx, mouseX, mouseY, partialTicks);

        double scale = client.getWindow().getScaleFactor();
        int left = x - 59;
        int bottom = y - 32;
        int width = 118;
        int height = 134;
        RenderSystem.enableScissor((int)(left  * scale), (int) (bottom * scale),
                (int)(width * scale), (int) (height * scale));

        PLAYER_LIST.render(ctx, mouseX, mouseY, partialTicks);
        RenderSystem.disableScissor();

        if(HOVER_PLAYER != null) {
            int dialogX = x + 75;
            int dialogY = y - 73;
            PlayerEntity pEntity = client.world.getPlayerByUuid(HOVER_PLAYER.uuid);
            if(pEntity != null) {
                ctx.drawText(this.textRenderer, pEntity.getDisplayName().copy().formatted(Formatting.UNDERLINE), dialogX, dialogY - 2, 0xFFFFFF, true);

                ColorFontRenderer.drawWithColors(this.textRenderer, ctx, Text.translatable("wildfire_gender.player_list.pronouns", HOVER_PLAYER.getPronouns()).getString(), dialogX, dialogY + 10, HOVER_PLAYER.getPronounColor().colors);
                ctx.drawText(this.textRenderer, Text.translatable("wildfire_gender.wardrobe.slider.breast_size", Math.round(HOVER_PLAYER.getBustSize() * 100)), dialogX, dialogY + 20, 0xBBBBBB, true);
                ctx.drawText(this.textRenderer, Text.translatable("wildfire_gender.char_settings.physics", Text.translatable(HOVER_PLAYER.hasBreastPhysics() ? "wildfire_gender.label.enabled" : "wildfire_gender.label.disabled")), dialogX, dialogY + 40, 0xBBBBBB, true);
                ctx.drawText(this.textRenderer, Text.translatable("wildfire_gender.player_list.bounce_multiplier", HOVER_PLAYER.getBounceMultiplier()), dialogX + 6, dialogY + 50, 0xBBBBBB, true);
                ctx.drawText(this.textRenderer, Text.translatable("wildfire_gender.player_list.breast_momentum", Math.round(HOVER_PLAYER.getFloppiness() * 100)), dialogX + 6, dialogY + 60, 0xBBBBBB, true);

                GuiUtils.drawEntityOnScreen(ctx, x - 110, y + 45, 45, (x - 300), (y - 26 - mouseY), pEntity);
            }
        }

        ctx.drawText(this.textRenderer, Text.translatable("wildfire_gender.player_list.title"), x - 60, y - 73, 4473924, false);

        if(client.player != null) {
            boolean withCreator = client.player.networkHandler.getPlayerList().stream()
                    .anyMatch((player) -> player.getProfile().getId().equals(CREATOR_UUID));

            int creatorY = y + 80;

            // move down so we don't overlap with the breast cancer awareness month banner
            if(isBreastCancerAwarenessMonth) creatorY += 40;

            if(withCreator) {
                GuiUtils.drawCenteredText(ctx, this.textRenderer, Text.translatable("wildfire_gender.label.with_creator"), this.width / 2, creatorY, 0xFF00FF);
            }

        }

        if(isBreastCancerAwarenessMonth) {
            ctx.fill(x - 159, y + 86, x + 159, y + 116, 0x55000000);
            ctx.drawTextWithShadow(textRenderer, Text.translatable("wildfire_gender.cancer_awareness.title").formatted(Formatting.BOLD, Formatting.ITALIC), this.width / 2 - 148, y + 97, 0xFFFFFF);
            ctx.drawTexture(TXTR_RIBBON, x + 130, y + 89, 0, 0, 26, 26, 20, 20, 20, 20);
        }

        if (tooltip != null) {
            ctx.drawTooltip(this.textRenderer, this.tooltip, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (this.width / 2);
        int y = (this.height / 2) - 20;

        if(isBreastCancerAwarenessMonth && mouseX > x - 159 && mouseY > y + 86 && mouseX < x + 159 && mouseY < y + 116) {
            this.client.setScreen(new ConfirmLinkScreen((bool) -> {
                if (bool) {
                    Util.getOperatingSystem().open("https://www.komen.org/how-to-help/donate/");
                }
                this.client.setScreen(this);
            }, "https://www.komen.org/how-to-help/donate/", true));
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void setTooltip(@Nullable Text tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state) {
        return super.mouseReleased(mouseX, mouseY, state);
    }
}
