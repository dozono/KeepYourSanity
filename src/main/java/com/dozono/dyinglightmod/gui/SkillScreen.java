package com.dozono.dyinglightmod.gui;

import com.dozono.dyinglightmod.DyingLight;
import com.dozono.dyinglightmod.skill.SkillContainer;
import com.dozono.dyinglightmod.skill.SkillType;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.advancements.SkillType;
import net.minecraft.advancements.SkillProgress;
import net.minecraft.client.gui.advancements.SkillEntryGui;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.multiplayer.ClientSkillManager;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.network.play.client.CSeenSkillsPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class SkillScreen extends Screen implements ClientSkillManager.IListener {
    public static final ResourceLocation SKILL_ICON_LOCATION = new ResourceLocation(DyingLight.MODID, "textures/gui/skills/icons.png");

    private static final ResourceLocation WINDOW_LOCATION = new ResourceLocation("textures/gui/advancements/window.png");
    private static final ResourceLocation TABS_LOCATION = new ResourceLocation("textures/gui/advancements/tabs.png");
    private static final ITextComponent VERY_SAD_LABEL = new TranslationTextComponent("advancements.sad_label");
    private static final ITextComponent NO_ADVANCEMENTS_LABEL = new TranslationTextComponent("advancements.empty");
    private static final ITextComponent TITLE = new TranslationTextComponent("gui.advancements");
    private final SkillContainer container; // capability
    private final Map<SkillType, SkillTabGui> tabs = Maps.newLinkedHashMap();
    private SkillTabGui selectedTab;
    private boolean isScrolling;
    private static int tabPage, maxPages;

    public SkillScreen(SkillContainer container) {
        super(NarratorChatListener.NO_TITLE);
        this.container = container;
    }

    protected void init() {
        this.tabs.clear();
        this.selectedTab = null;
        this.container.setListener(this);
        if (this.selectedTab == null && !this.tabs.isEmpty()) {
            this.container.setSelectedTab(this.tabs.values().iterator().next().getSkill(), true);
        } else {
            this.container.setSelectedTab(this.selectedTab == null ? null : this.selectedTab.getSkill(), true);
        }
        if (this.tabs.size() > SkillTabType.MAX_TABS) {
            int guiLeft = (this.width - 252) / 2;
            int guiTop = (this.height - 140) / 2;
            addButton(new net.minecraft.client.gui.widget.button.Button(guiLeft, guiTop - 50, 20, 20, new net.minecraft.util.text.StringTextComponent("<"), b -> tabPage = Math.max(tabPage - 1, 0)));
            addButton(new net.minecraft.client.gui.widget.button.Button(guiLeft + 252 - 20, guiTop - 50, 20, 20, new net.minecraft.util.text.StringTextComponent(">"), b -> tabPage = Math.min(tabPage + 1, maxPages)));
            maxPages = this.tabs.size() / SkillTabType.MAX_TABS;
        }
    }

    public void removed() {
        this.container.setListener((ClientSkillManager.IListener) null);
        ClientPlayNetHandler clientplaynethandler = this.minecraft.getConnection();
        if (clientplaynethandler != null) {
            clientplaynethandler.send(CSeenSkillsPacket.closedScreen());
        }

    }

    public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
        if (p_231044_5_ == 0) {
            int i = (this.width - 252) / 2;
            int j = (this.height - 140) / 2;

            for (SkillTabGui advancementtabgui : this.tabs.values()) {
                if (advancementtabgui.getPage() == tabPage && advancementtabgui.isMouseOver(i, j, p_231044_1_, p_231044_3_)) {
                    this.container.setSelectedTab(advancementtabgui.getSkill(), true);
                    break;
                }
            }
        }

        return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
    }

    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
//        if (this.minecraft.options.keySkills.matches(p_231046_1_, p_231046_2_)) {
//            this.minecraft.setScreen((Screen) null);
//            this.minecraft.mouseHandler.grabMouse();
//            return true;
//        } else {
        return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
//        }
    }

    public void render(MatrixStack matrixStack, int x, int y, float partialTick) {
        int i = (this.width - 252) / 2;
        int j = (this.height - 140) / 2;
        this.renderBackground(matrixStack);
        if (maxPages != 0) {
            net.minecraft.util.text.ITextComponent page = new net.minecraft.util.text.StringTextComponent(String.format("%d / %d", tabPage + 1, maxPages + 1));
            int width = this.font.width(page);
            RenderSystem.disableLighting();
            this.font.drawShadow(matrixStack, page.getVisualOrderText(), i + (252 / 2) - (width / 2), j - 44, -1);
        }
        this.renderInside(matrixStack, x, y, i, j);
        this.renderWindow(matrixStack, i, j);
        this.renderTooltips(matrixStack, x, y, i, j);
    }

    public boolean mouseDragged(double x, double y, int btnIndex, double dx, double dy) {
        if (btnIndex != 0) {
            this.isScrolling = false;
            return false;
        } else {
            if (!this.isScrolling) {
                this.isScrolling = true;
            } else if (this.selectedTab != null) {
                this.selectedTab.scroll(dx, dy);
            }

            return true;
        }
    }

    private void renderInside(MatrixStack p_238696_1_, int p_238696_2_, int p_238696_3_, int p_238696_4_, int p_238696_5_) {
        SkillTabGui advancementtabgui = this.selectedTab;
        if (advancementtabgui == null) {
            fill(p_238696_1_, p_238696_4_ + 9, p_238696_5_ + 18, p_238696_4_ + 9 + 234, p_238696_5_ + 18 + 113, -16777216);
            int i = p_238696_4_ + 9 + 117;
            drawCenteredString(p_238696_1_, this.font, NO_ADVANCEMENTS_LABEL, i, p_238696_5_ + 18 + 56 - 9 / 2, -1);
            drawCenteredString(p_238696_1_, this.font, VERY_SAD_LABEL, i, p_238696_5_ + 18 + 113 - 9, -1);
        } else {
            RenderSystem.pushMatrix();
            RenderSystem.translatef((float) (p_238696_4_ + 9), (float) (p_238696_5_ + 18), 0.0F);
            advancementtabgui.drawContents(p_238696_1_);
            RenderSystem.popMatrix();
            RenderSystem.depthFunc(515);
            RenderSystem.disableDepthTest();
        }
    }

    public void renderWindow(MatrixStack matrixStack, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        this.minecraft.getTextureManager().bind(WINDOW_LOCATION);
        this.blit(matrixStack, x, y, 0, 0, 252, 140);
        if (this.tabs.size() > 1) {
            this.minecraft.getTextureManager().bind(TABS_LOCATION);

            for (SkillTabGui drawTab : this.tabs.values()) {
                if (drawTab.getPage() == tabPage)
                    drawTab.drawTab(matrixStack, x, y, drawTab == this.selectedTab);
            }

            RenderSystem.enableRescaleNormal();
            RenderSystem.defaultBlendFunc();

            for (SkillTabGui tabGui : this.tabs.values()) {
                if (tabGui.getPage() == tabPage)
                    tabGui.drawIcon(x, y, this.itemRenderer);
            }

            RenderSystem.disableBlend();
        }

        this.font.draw(matrixStack, TITLE, (float) (x + 8), (float) (y + 6), 4210752);
    }

    private void renderTooltips(MatrixStack p_238697_1_, int p_238697_2_, int p_238697_3_, int p_238697_4_, int p_238697_5_) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.selectedTab != null) {
            RenderSystem.pushMatrix();
            RenderSystem.enableDepthTest();
            RenderSystem.translatef((float) (p_238697_4_ + 9), (float) (p_238697_5_ + 18), 400.0F);
            this.selectedTab.drawTooltips(p_238697_1_, p_238697_2_ - p_238697_4_ - 9, p_238697_3_ - p_238697_5_ - 18, p_238697_4_, p_238697_5_);
            RenderSystem.disableDepthTest();
            RenderSystem.popMatrix();
        }

        if (this.tabs.size() > 1) {
            for (SkillTabGui tabGui : this.tabs.values()) {
                if (tabGui.getPage() == tabPage && tabGui.isMouseOver(p_238697_4_, p_238697_5_, (double) p_238697_2_, (double) p_238697_3_)) {
                    this.renderTooltip(p_238697_1_, tabGui.getTitle(), p_238697_2_, p_238697_3_);
                }
            }
        }

    }

    public void onAddSkillRoot(SkillType p_191931_1_) {
        SkillTabGui skillTabGui = SkillTabGui.create(this.minecraft, this, this.tabs.size(), p_191931_1_);
        if (skillTabGui != null) {
            this.tabs.put(p_191931_1_, skillTabGui);
        }
    }

    public void onRemoveSkillRoot(SkillType p_191928_1_) {
    }

    public void onAddSkillTask(SkillType skillType) {
        SkillTabGui advancementtabgui = this.getTab(skillType);
        if (advancementtabgui != null) {
            advancementtabgui.addSkill(skillType);
        }

    }

    public void onRemoveSkillTask(SkillType p_191929_1_) {
    }

//    public void onUpdateSkillProgress(SkillType p_191933_1_, SkillProgress p_191933_2_) {
//        SkillEntryGui advancemententrygui = this.getSkillWidget(p_191933_1_);
//        if (advancemententrygui != null) {
//            advancemententrygui.setProgress(p_191933_2_);
//        }
//
//    }

    public void onSelectedTabChanged(@Nullable SkillType p_193982_1_) {
        this.selectedTab = this.tabs.get(p_193982_1_);
    }

    public void onSkillsCleared() {
        this.tabs.clear();
        this.selectedTab = null;
    }

    @Nullable
    public SkillEntryGui getSkillWidget(SkillType p_191938_1_) {
        SkillTabGui advancementtabgui = this.getTab(p_191938_1_);
        return advancementtabgui == null ? null : advancementtabgui.getWidget(p_191938_1_);
    }

    @Nullable
    private SkillTabGui getTab(SkillType p_191935_1_) {
        while (p_191935_1_.getParent() != null) {
            p_191935_1_ = p_191935_1_.getParent();
        }

        return this.tabs.get(p_191935_1_);
    }
}
