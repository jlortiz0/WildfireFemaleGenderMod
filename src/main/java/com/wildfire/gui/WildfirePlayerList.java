package com.wildfire.gui;

import com.wildfire.gui.screen.WardrobeBrowserScreen;
import com.wildfire.gui.screen.WildfirePlayerListScreen;
import com.wildfire.main.WildfireGender;
import com.wildfire.main.entitydata.PlayerConfig;
import com.wildfire.render.ColorFontRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class WildfirePlayerList extends EntryListWidget<WildfirePlayerList.Entry> {
    private static final Identifier TXTR_SYNC = Identifier.of(WildfireGender.MODID, "textures/sync.png");
    private static final Identifier TXTR_CACHED = Identifier.of(WildfireGender.MODID, "textures/cached.png");
    private static final Identifier TXTR_UNKNOWN = Identifier.of(WildfireGender.MODID, "textures/unknown.png");

    private final int listWidth;

    private final WildfirePlayerListScreen parent;

    @Override
    protected void drawHeaderAndFooterSeparators(DrawContext context) {
    }

    public WildfirePlayerList(WildfirePlayerListScreen parent, MinecraftClient client, int listWidth, int top, int bottom) {
        super(client, parent.width-4, parent.height, top-6, 20);
        this.parent = parent;
        this.listWidth = listWidth;
        this.setRenderHeader(false, 0);
        this.refreshList();
    }

    @Override
    protected int getScrollbarX() {
        return parent.width / 2 + 55;
    }

    @Override
    public int getRowWidth()
    {
        return this.listWidth;
    }

    public void refreshList() {
        this.clearEntries();
        PlayerListEntry[] playersC = this.client.getNetworkHandler().getPlayerList().toArray(new PlayerListEntry[0]);

        for (PlayerListEntry loadedPlayer : playersC) {
            this.addEntry(new Entry(loadedPlayer));
        }
    }

    @Override
    protected void drawMenuListBackground(DrawContext ctx) {}

    public boolean isLoadingPlayers() {
        boolean loadingPlayers = false;
        for (Entry child : this.children()) {
            PlayerConfig aPlr = WildfireGender.getPlayerById(child.nInfo.getProfile().getId());
            if (aPlr == null) {
                loadingPlayers = true;
            }
        }
        return loadingPlayers;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    public class Entry extends EntryListWidget.Entry<WildfirePlayerList.Entry> {

        private final String name;
        public final PlayerListEntry nInfo;
        private final WildfireButton btnOpenGUI;

        private Entry(final PlayerListEntry nInfo) {
            this.nInfo = nInfo;
            this.name = nInfo.getProfile().getName();
            btnOpenGUI = new WildfireButton(0, 0, 112, 20, Text.empty(), button -> {
                PlayerConfig aPlr = WildfireGender.getPlayerById(nInfo.getProfile().getId());
                if(aPlr == null) return;

                try {
                    client.setScreen(new WardrobeBrowserScreen(parent, nInfo.getProfile().getId()));
                } catch(Exception ignored) {}
            });
            PlayerConfig aPlr = WildfireGender.getPlayerById(nInfo.getProfile().getId());
            if(aPlr != null) {
                btnOpenGUI.active = aPlr.syncStatus != PlayerConfig.SyncStatus.SYNCED;
            }
        }

        public PlayerListEntry getNetworkInfo() {
            return nInfo;
        }

        @Override
        public void render(DrawContext ctx, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks) {
            TextRenderer font = client.textRenderer;

            PlayerEntity playerentity = client.world.getPlayerByUuid(nInfo.getProfile().getId());
            PlayerConfig aPlr = WildfireGender.getPlayerById(nInfo.getProfile().getId());
            ctx.drawTexture(nInfo.getSkinTextures().texture(), left+2, top+2, 16, 16, 8, 8, 8, 8, 64, 64);
            if (playerentity != null && playerentity.isPartVisible(PlayerModelPart.HAT)) {
                ctx.drawTexture(nInfo.getSkinTextures().texture(), left+1, top+1, 18, 18, 40, 8, 8, 8,64, 64);
            }

            ctx.drawText(font, name, left + 23, top + 2, 0xFFFFFF, false);
            if(aPlr != null) {
                btnOpenGUI.active = aPlr.syncStatus != PlayerConfig.SyncStatus.SYNCED;

                ColorFontRenderer.drawWithColors(font, ctx, aPlr.getPronouns(), left + 23, top + 11, aPlr.getPronounColor().colors);
                if (aPlr.getSyncStatus() == PlayerConfig.SyncStatus.SYNCED) {
                    ctx.drawTexture(TXTR_SYNC, left + 98, top + 11, 0, 0, 12, 8, 12, 8);
                    if (mouseX > left + 98 - 2 && mouseY > top + 11 - 2 && mouseX < left + 98 + 12 + 2 && mouseY < top + 20) {
                        parent.setTooltip(Text.translatable("wildfire_gender.player_list.state.synced"));
                    }

                } else if (aPlr.getSyncStatus() == PlayerConfig.SyncStatus.UNKNOWN) {
                    ctx.drawTexture(TXTR_UNKNOWN, left + 98, top + 11, 0, 0, 12, 8, 12, 8);
                }
            } else {
                btnOpenGUI.active = false;
                ctx.drawText(font, Text.translatable("wildfire_gender.label.too_far").formatted(Formatting.RED), left + 23, top + 11, 0xFFFFFF, false);
            }
            this.btnOpenGUI.setX(left);
            this.btnOpenGUI.setY(top);
            this.btnOpenGUI.render(ctx, mouseX, mouseY, partialTicks);

            if(this.btnOpenGUI.isSelected()) {
                WildfirePlayerListScreen.HOVER_PLAYER = aPlr;
            }
        }


        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if(this.btnOpenGUI.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            return this.btnOpenGUI.mouseReleased(mouseX, mouseY, button);
        }
    }
}
