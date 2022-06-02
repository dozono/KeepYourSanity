package com.dozono.dyinglightmod.gui;

import com.dozono.dyinglightmod.skill.SkillType;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.advancements.AdvancementEntryGui;
import net.minecraft.client.gui.advancements.AdvancementsScreen;
import net.minecraft.util.text.ITextComponent;
import com.mojang.blaze3d.systems.RenderSystem;

import javax.annotation.Nullable;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SkillGui extends AbstractGui {
    private final Minecraft minecraft;
    private final SkillEntryGui root;
    //    private final AdvancementsScreen screen;
    private final SkillTabType type;
    private final int index;
    //    private final Advancement advancement;
    private final DisplayInfo display;
    //    private final ItemStack icon;
    private final ITextComponent title;
    //    private final AdvancementEntryGui root;
//    private final Map<Advancement, AdvancementEntryGui> widgets = Maps.newLinkedHashMap();
    private double scrollX;
    private double scrollY;
    private int minX = Integer.MAX_VALUE;
    private int minY = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int maxY = Integer.MIN_VALUE;
    private float fade;
    private boolean centered;
    private int page;

    @Nullable
    public static SkillTabGui create(Minecraft mc, AdvancementsScreen p_193936_1_, int p_193936_2_, Advancement p_193936_3_) {
        if (p_193936_3_.getDisplay() == null) {
            return null;
        } else {
            for (SkillTabType skillTabType : SkillTabType.values()) {
                if ((p_193936_2_ % SkillTabType.MAX_TABS) < skillTabType.getMax()) {
                    return new SkillTabGui(mc, p_193936_1_, skillTabType, p_193936_2_ % SkillTabType.MAX_TABS, p_193936_2_ / SkillTabType.MAX_TABS, p_193936_3_, p_193936_3_.getDisplay());
                }

                p_193936_2_ -= skillTabType.getMax();
            }

            return null;
        }
    }

    public SkillGui(Minecraft mc, int index, DisplayInfo displayInfo) {
        this.minecraft = mc;
        this.index = index;
        this.display = displayInfo;
        this.title = displayInfo.getTitle();
        this.addWidget(this.root, p_i47589_5_);
    }

    public int getPage() {
        return page;
    }

    public Advancement getAdvancement() {
        return this.advancement;
    }

    public ITextComponent getTitle() {
        return this.title;
    }

    public void drawTab(MatrixStack p_238683_1_, int p_238683_2_, int p_238683_3_, boolean p_238683_4_) {
        this.type.draw(p_238683_1_, this, p_238683_2_, p_238683_3_, p_238683_4_, this.index);
    }

    public void drawIcon(int x, int y, ItemRenderer p_191796_3_) {
        this.type.drawIcon(x, y, this.index, p_191796_3_, this.icon);
    }

    public void drawContents(MatrixStack p_238682_1_) {
        if (!this.centered) {
            this.scrollX = (double) (117 - (this.maxX + this.minX) / 2);
            this.scrollY = (double) (56 - (this.maxY + this.minY) / 2);
            this.centered = true;
        }

        RenderSystem.pushMatrix();
        RenderSystem.enableDepthTest();
        RenderSystem.translatef(0.0F, 0.0F, 950.0F);
        RenderSystem.colorMask(false, false, false, false);
        fill(p_238682_1_, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.translatef(0.0F, 0.0F, -950.0F);
        RenderSystem.depthFunc(518);
        fill(p_238682_1_, 234, 113, 0, 0, -16777216);
        RenderSystem.depthFunc(515);
        ResourceLocation resourcelocation = this.display.getBackground();
        if (resourcelocation != null) {
            this.minecraft.getTextureManager().bind(resourcelocation);
        } else {
            this.minecraft.getTextureManager().bind(TextureManager.INTENTIONAL_MISSING_TEXTURE);
        }

        int i = MathHelper.floor(this.scrollX);
        int j = MathHelper.floor(this.scrollY);
        int k = i % 16;
        int l = j % 16;

        for (int i1 = -1; i1 <= 15; ++i1) {
            for (int j1 = -1; j1 <= 8; ++j1) {
                blit(p_238682_1_, k + 16 * i1, l + 16 * j1, 0.0F, 0.0F, 16, 16, 16, 16);
            }
        }

        this.root.drawConnectivity(p_238682_1_, i, j, true);
        this.root.drawConnectivity(p_238682_1_, i, j, false);
        this.root.draw(p_238682_1_, i, j);
        RenderSystem.depthFunc(518);
        RenderSystem.translatef(0.0F, 0.0F, -950.0F);
        RenderSystem.colorMask(false, false, false, false);
        fill(p_238682_1_, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.translatef(0.0F, 0.0F, 950.0F);
        RenderSystem.depthFunc(515);
        RenderSystem.popMatrix();
    }

    public void drawTooltips(MatrixStack p_238684_1_, int p_238684_2_, int p_238684_3_, int p_238684_4_, int p_238684_5_) {
        RenderSystem.pushMatrix();
        RenderSystem.translatef(0.0F, 0.0F, 200.0F);
        fill(p_238684_1_, 0, 0, 234, 113, MathHelper.floor(this.fade * 255.0F) << 24);
        boolean flag = false;
        int i = MathHelper.floor(this.scrollX);
        int j = MathHelper.floor(this.scrollY);
        if (p_238684_2_ > 0 && p_238684_2_ < 234 && p_238684_3_ > 0 && p_238684_3_ < 113) {
            for (AdvancementEntryGui advancemententrygui : this.widgets.values()) {
                if (advancemententrygui.isMouseOver(i, j, p_238684_2_, p_238684_3_)) {
                    flag = true;
                    advancemententrygui.drawHover(p_238684_1_, i, j, this.fade, p_238684_4_, p_238684_5_);
                    break;
                }
            }
        }

        RenderSystem.popMatrix();
        if (flag) {
            this.fade = MathHelper.clamp(this.fade + 0.02F, 0.0F, 0.3F);
        } else {
            this.fade = MathHelper.clamp(this.fade - 0.04F, 0.0F, 1.0F);
        }

    }

    public boolean isMouseOver(int p_195627_1_, int p_195627_2_, double p_195627_3_, double p_195627_5_) {
        return this.type.isMouseOver(p_195627_1_, p_195627_2_, this.index, p_195627_3_, p_195627_5_);
    }


    public void scroll(double p_195626_1_, double p_195626_3_) {
        if (this.maxX - this.minX > 234) {
            this.scrollX = MathHelper.clamp(this.scrollX + p_195626_1_, (double) (-(this.maxX - 234)), 0.0D);
        }

        if (this.maxY - this.minY > 113) {
            this.scrollY = MathHelper.clamp(this.scrollY + p_195626_3_, (double) (-(this.maxY - 113)), 0.0D);
        }

    }

    public void addAdvancement(Advancement p_191800_1_) {
        if (p_191800_1_.getDisplay() != null) {
            AdvancementEntryGui advancemententrygui = new AdvancementEntryGui(this, this.minecraft, p_191800_1_, p_191800_1_.getDisplay());
            this.addWidget(advancemententrygui, p_191800_1_);
        }
    }

    private void addWidget(SkillEntryGui p_193937_1_, SkillType skill) {
        this.widgets.put(skill, p_193937_1_);
        int i = p_193937_1_.getX();
        int j = i + 28;
        int k = p_193937_1_.getY();
        int l = k + 27;
        this.minX = Math.min(this.minX, i);
        this.maxX = Math.max(this.maxX, j);
        this.minY = Math.min(this.minY, k);
        this.maxY = Math.max(this.maxY, l);

        for (AdvancementEntryGui advancemententrygui : this.widgets.values()) {
            advancemententrygui.attachToParent();
        }

    }

    @Nullable
    public AdvancementEntryGui getWidget(Advancement p_191794_1_) {
        return this.widgets.get(p_191794_1_);
    }

    public AdvancementsScreen getScreen() {
        return this.screen;
    }
}

