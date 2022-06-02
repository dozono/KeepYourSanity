package com.dozono.dyinglightmod.gui;

import com.dozono.dyinglightmod.skill.SkillType;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.Map;

public class SkillTabGui extends AbstractGui {
    private final Minecraft minecraft;
    private final SkillScreen screen;
    private final SkillTabType type;
    private final int index;
    private final SkillType skill;
    private final ITextComponent title;
    private final SkillEntryGui root;
    private IconSprite iconSprite;
    private final ResourceLocation background;
    private final Map<SkillType, SkillEntryGui> widgets = Maps.newLinkedHashMap();
    private double scrollX;
    private double scrollY;
    private int minX = Integer.MAX_VALUE;
    private int minY = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int maxY = Integer.MIN_VALUE;
    private float fade;
    private boolean centered;
    private int page;

    public SkillTabGui(Minecraft mc,
                       SkillScreen screen,
                       SkillTabType type,
                       int index,
                       SkillType skillType,
                       ITextComponent title,
                       IconSprite iconSprite,
                       ResourceLocation background) {
        this.minecraft = mc;
        this.screen = screen;
        this.type = type;
        this.index = index;
        this.skill = skillType;
        this.iconSprite = iconSprite;
        this.background = background;
        this.title = title;
        this.root = new SkillEntryGui(this, mc, skillType);
        this.addWidget(this.root, skillType);
    }

//    public SkillTypeTabGui(Minecraft mc, SkillScreen screen, AdvancementTabType type, int index, int page, SkillType adv, DisplayInfo info) {
//        this(mc, screen, type, index, adv, info);
//        this.page = page;
//    }

    @Nullable
    public static SkillTabGui create(Minecraft p_193936_0_, SkillScreen p_193936_1_, int p_193936_2_, SkillType skillType) {
        if (skillType.getDisplay() == null) {
            return null;
        }

        for (SkillTabType tabType : SkillTabType.values()) {
            if ((p_193936_2_ % SkillTabType.MAX_TABS) < tabType.getMax()) {
                return new SkillTabGui(p_193936_0_, p_193936_1_, tabType, p_193936_2_ % SkillTabType.MAX_TABS, p_193936_2_ / AdvancementTabType.MAX_TABS, skillType, skillType.getDisplay());
            }

            p_193936_2_ -= tabType.getMax();
        }

        return null;
    }

    public int getPage() {
        return page;
    }

    public SkillType getSkill() {
        return this.skill;
    }

    public ITextComponent getTitle() {
        return this.title;
    }

    public void drawTab(MatrixStack matrixStack, int x, int y, boolean selected) {
//        this.type.draw(matrixStack, this, x, y, selected, this.index);

        int j = selected ? this.textureY + this.height : this.textureY;
        Draw.blit(matrixStack, x + this.getX(p_238686_6_), p_238686_4_ + this.getY(p_238686_6_), i, j, this.width, this.height);
    }

    public void drawIcon(int p_191796_1_, int p_191796_2_, ItemRenderer p_191796_3_) {

//        this.type.drawIcon(p_191796_1_, p_191796_2_, this.index, p_191796_3_, this.icon);
    }

    public void drawContents(MatrixStack matrixStack) {
        if (!this.centered) {
            this.scrollX = (double) (117 - (this.maxX + this.minX) / 2);
            this.scrollY = (double) (56 - (this.maxY + this.minY) / 2);
            this.centered = true;
        }

        RenderSystem.pushMatrix();
        RenderSystem.enableDepthTest();
        RenderSystem.translatef(0.0F, 0.0F, 950.0F);
        RenderSystem.colorMask(false, false, false, false);
        fill(matrixStack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.translatef(0.0F, 0.0F, -950.0F);
        RenderSystem.depthFunc(518);
        fill(matrixStack, 234, 113, 0, 0, -16777216);
        RenderSystem.depthFunc(515);
        ResourceLocation resourcelocation = this.background;
        if (resourcelocation != null) {
            this.minecraft.getTextureManager().bind(resourcelocation);
        } else {
            this.minecraft.getTextureManager().bind(TextureManager.INTENTIONAL_MISSING_TEXTURE);
        }

        int i = MathHelper.floor(this.scrollX);
        int j = MathHelper.floor(this.scrollY);
        int x = i % 16;
        int l = j % 16;

        for (int y = -1; y <= 15; ++y) {
            for (int j1 = -1; j1 <= 8; ++j1) {
                Draw.blit(matrixStack, x + 16 * y, l + 16 * j1, 0.0F, 0.0F, 16, 16, 16, 16);
            }
        }

        this.root.drawConnectivity(matrixStack, i, j, true);
        this.root.drawConnectivity(matrixStack, i, j, false);
        this.root.draw(matrixStack, i, j);
        RenderSystem.depthFunc(518);
        RenderSystem.translatef(0.0F, 0.0F, -950.0F);
        RenderSystem.colorMask(false, false, false, false);
        Draw.fill(matrixStack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.translatef(0.0F, 0.0F, 950.0F);
        RenderSystem.depthFunc(515);
        RenderSystem.popMatrix();
    }

    public void drawTooltips(MatrixStack matrixStack, int p_238684_2_, int p_238684_3_, int p_238684_4_, int p_238684_5_) {
        RenderSystem.pushMatrix();
        RenderSystem.translatef(0.0F, 0.0F, 200.0F);
        Draw.fill(matrixStack, 0, 0, 234, 113, MathHelper.floor(this.fade * 255.0F) << 24);
        boolean flag = false;
        int x = MathHelper.floor(this.scrollX);
        int y = MathHelper.floor(this.scrollY);
        if (p_238684_2_ > 0 && p_238684_2_ < 234 && p_238684_3_ > 0 && p_238684_3_ < 113) {
            for (SkillEntryGui entryGui : this.widgets.values()) {
                if (entryGui.isMouseOver(x, y, p_238684_2_, p_238684_3_)) {
                    flag = true;
                    entryGui.drawHover(matrixStack, x, y, this.fade, p_238684_4_, p_238684_5_);
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


    public void scroll(double dx, double dy) {
        if (this.maxX - this.minX > 234) {
            this.scrollX = MathHelper.clamp(this.scrollX + dx, (double) (-(this.maxX - 234)), 0.0D);
        }

        if (this.maxY - this.minY > 113) {
            this.scrollY = MathHelper.clamp(this.scrollY + dy, (double) (-(this.maxY - 113)), 0.0D);
        }
    }

    public void addSkill(SkillType skillType) {
        SkillEntryGui SkillTypeEntryGui = new SkillEntryGui(this, this.minecraft, skillType);
        this.addWidget(SkillTypeEntryGui, skillType);
    }

    private void addWidget(SkillEntryGui entryGui, SkillType skillType) {
        this.widgets.put(skillType, entryGui);
        int i = entryGui.getX();
        int j = i + 28;
        int k = entryGui.getY();
        int l = k + 27;
        this.minX = Math.min(this.minX, i);
        this.maxX = Math.max(this.maxX, j);
        this.minY = Math.min(this.minY, k);
        this.maxY = Math.max(this.maxY, l);

        for (SkillEntryGui skillEntryGui : this.widgets.values()) {
            skillEntryGui.attachToParent();
        }
    }

    @Nullable
    public SkillEntryGui getWidget(SkillType skillType) {
        return this.widgets.get(skillType);
    }

    public SkillScreen getScreen() {
        return this.screen;
    }
}

