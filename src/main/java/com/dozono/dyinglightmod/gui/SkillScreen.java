package com.dozono.dyinglightmod.gui;

import com.dozono.dyinglightmod.DyingLight;
import com.dozono.dyinglightmod.skill.SkillContainer;
import com.dozono.dyinglightmod.skill.agility.SkillTypeDoubleJump;
import com.dozono.dyinglightmod.skill.combat.SkillTypePlunder;
import com.dozono.dyinglightmod.skill.survival.SkillTypeMandom;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SkillScreen extends Screen {
    public static final ResourceLocation SKILL_ICON_LOCATION = new ResourceLocation(DyingLight.MODID, "textures/gui/skill.png");
    private static final ResourceLocation WINDOW_LOCATION = new ResourceLocation("textures/gui/advancements/window.png");
    private static final ResourceLocation TABS_LOCATION = new ResourceLocation("textures/gui/advancements/tabs.png");
    private static final ITextComponent VERY_SAD_LABEL = new TranslationTextComponent("advancements.sad_label");
    private static final ITextComponent NO_ADVANCEMENTS_LABEL = new TranslationTextComponent("advancements.empty");
    private static final ITextComponent TITLE = new TranslationTextComponent("dying_light.tab.currentlevel");

    private final SkillContainer container; // capability
    private final SkillTabGui[] tabs;
    private SkillTabGui selectedTab;
    private boolean isScrolling;
    private static int tabPage, maxPages;

    public SkillScreen(SkillContainer container) {
        super(NarratorChatListener.NO_TITLE);
        this.container = container;
        tabs = new SkillTabGui[]{
                SkillTabGui.create(Minecraft.getInstance(), this, 0, SkillTypeMandom.INSTANCE,
                        new TranslationTextComponent("dyinglight.survival"),
//                        new IconSprite(0, 0, 32, 32, SKILL_ICON_LOCATION),
                        new ResourceLocation("textures/gui/advancements/backgrounds/husbandry.png")),
                SkillTabGui.create(Minecraft.getInstance(), this, 1, SkillTypePlunder.INSTANCE,
                        new TranslationTextComponent("dyinglight.combat"),
//                        new IconSprite(64, 32, 32, 32, SKILL_ICON_LOCATION),
                        new ResourceLocation("textures/gui/advancements/backgrounds/husbandry.png")),
                SkillTabGui.create(Minecraft.getInstance(), this, 2, SkillTypeDoubleJump.INSTANCE,
                        new TranslationTextComponent("dyinglight.agility"),
//                        new IconSprite(64, 0, 32, 32, SKILL_ICON_LOCATION),
                        new ResourceLocation("textures/gui/advancements/backgrounds/husbandry.png"))
        };
    }

    protected void init() {
        this.selectedTab = tabs[0];
        if (this.tabs.length > SkillTabType.MAX_TABS) {
            int guiLeft = (this.width - 252) / 2;
            int guiTop = (this.height - 140) / 2;
            addButton(new Button(guiLeft, guiTop - 50, 20, 20, new StringTextComponent("<"), b -> tabPage = Math.max(tabPage - 1, 0)));
            addButton(new Button(guiLeft + 252 - 20, guiTop - 50, 20, 20, new StringTextComponent(">"), b -> tabPage = Math.min(tabPage + 1, maxPages)));
            maxPages = this.tabs.length / SkillTabType.MAX_TABS;
        }
        for (SkillTabGui tab : tabs) {
            for (SkillEntryGui value : tab.getWidgets().values()) {
                this.addWidget(value);
            }
        }
    }


    public SkillTabGui getSelectedTab() {
        return selectedTab;
    }

    public void removed() {
//        this.container.setListener((ClientSkillManager.IListener) null);
//        ClientPlayNetHandler clientplaynethandler = this.minecraft.getConnection();
//        if (clientplaynethandler != null) {
//            clientplaynethandler.send(CSeenSkillsPacket.closedScreen());
//        }
    }

    public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
        if (p_231044_5_ == 0) {
            int i = (this.width - 252) / 2;
            int j = (this.height - 140) / 2;

            for (SkillTabGui skillTabGui : this.tabs) {
                if (skillTabGui.getPage() == tabPage && skillTabGui.isMouseOver(i, j, p_231044_1_, p_231044_3_)) {
                    this.selectedTab = skillTabGui;
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
            ITextComponent page = new StringTextComponent(String.format("%d / %d", tabPage + 1, maxPages + 1));
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
        SkillTabGui selectedTab = this.selectedTab;
        if (selectedTab == null) {
            Draw.fill(p_238696_1_, p_238696_4_ + 9, p_238696_5_ + 18, p_238696_4_ + 9 + 234, p_238696_5_ + 18 + 113, -16777216);
            int i = p_238696_4_ + 9 + 117;
            Draw.drawCenteredString(p_238696_1_, this.font, NO_ADVANCEMENTS_LABEL, i, p_238696_5_ + 18 + 56 - 9 / 2, -1);
            Draw.drawCenteredString(p_238696_1_, this.font, VERY_SAD_LABEL, i, p_238696_5_ + 18 + 113 - 9, -1);
        } else {
            RenderSystem.pushMatrix();
            RenderSystem.translatef((float) (p_238696_4_ + 9), (float) (p_238696_5_ + 18), 0.0F);
            selectedTab.renderContents(p_238696_1_);
            RenderSystem.popMatrix();
            RenderSystem.depthFunc(515);
            RenderSystem.disableDepthTest();
        }
    }

    private void renderWindow(MatrixStack matrixStack, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        this.minecraft.getTextureManager().bind(WINDOW_LOCATION);
        this.blit(matrixStack, x, y, 0, 0, 252, 140);

        this.minecraft.getTextureManager().bind(TABS_LOCATION);
        for (SkillTabGui drawTab : this.tabs) {
            if (drawTab.getPage() == tabPage)
                drawTab.renderTab(matrixStack, x, y, drawTab == this.selectedTab);
        }

        RenderSystem.enableRescaleNormal();
        RenderSystem.defaultBlendFunc();

        for (SkillTabGui tabGui : this.tabs) {
            if (tabGui.getPage() == tabPage)
                tabGui.renderIcon(matrixStack, x, y);
        }

        RenderSystem.disableBlend();

        this.font.draw(matrixStack, TITLE, (float) (x + 8), (float) (y + 6), 4210752);
        int width1 = minecraft.font.width(TITLE);
        this.font.draw(matrixStack, minecraft.player.totalExperience + "", (float) (x + 10 + width1), (float) (y + 6), 4210752);
    }

    private void renderTooltips(MatrixStack matrixStack, int x, int y, int xOffset, int yOffset) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.selectedTab != null) {
            RenderSystem.pushMatrix();
            RenderSystem.enableDepthTest();
            RenderSystem.translatef((float) (xOffset + 9), (float) (yOffset + 18), 400.0F);
            this.selectedTab.renderTooltips(matrixStack, x - xOffset - 9, y - yOffset - 18, xOffset, yOffset);
            RenderSystem.disableDepthTest();
            RenderSystem.popMatrix();
        }

        for (SkillTabGui tabGui : this.tabs) {
            if (tabGui.getPage() == tabPage && tabGui.isMouseOver(xOffset, yOffset, (double) x, (double) y)) {
                this.renderTooltip(matrixStack, tabGui.getTitle(), x, y);
            }
        }

    }

//    public void onUpdateSkillProgress(SkillType p_191933_1_, SkillProgress p_191933_2_) {
//        SkillEntryGui advancemententrygui = this.getSkillWidget(p_191933_1_);
//        if (advancemententrygui != null) {
//            advancemententrygui.setProgress(p_191933_2_);
//        }
//
//    }

//    public void set(@Nullable SkillType p_193982_1_) {
//        this.selectedTab = this.tabs.get(p_193982_1_);
//    }

//    @Nullable
//    public SkillEntryGui getSkillWidget(SkillType p_191938_1_) {
//        SkillTabGui tab = this.getTab(p_191938_1_);
//        return tab == null ? null : tab.getWidget(p_191938_1_);
//    }
}
