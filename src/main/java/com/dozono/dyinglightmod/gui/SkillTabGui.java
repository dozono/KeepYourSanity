package com.dozono.dyinglightmod.gui;

import com.dozono.dyinglightmod.skill.SkillType;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.Map;

import static com.dozono.dyinglightmod.gui.SkillScreen.SKILL_ICON_LOCATION;

public class SkillTabGui {
    private final Minecraft minecraft;
    private final SkillScreen screen;
    private final SkillTabType type;
    private final int index;
    private final ITextComponent title;
    private final int iconU;
    private final int iconV;
    private final ResourceLocation background;

    private final SkillEntryGui root;
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
                       int tabIndex,
                       SkillType skillType,
                       ITextComponent title,
                       ResourceLocation background) {
        this.minecraft = mc;
        this.screen = screen;
        this.type = type;
        this.index = tabIndex;
        this.root = SkillEntryHelper.createSkillGuiTree(mc, this, skillType, widgets);
        int[] iconUV = SkillEntryHelper.getIconUV(skillType);
        this.iconU = iconUV[0];
        this.iconV = iconUV[1];
        this.background = background;
        this.title = title;
        this.updateRegion();
    }


    public static SkillTabGui create(Minecraft minecraft, SkillScreen screen, int index, SkillType skillType,
                                     ITextComponent title, ResourceLocation backgroundTexture) {
        for (SkillTabType tabType : SkillTabType.values()) {
            if ((index % SkillTabType.MAX_TABS) < tabType.getMax()) {
                return new SkillTabGui(minecraft, screen, tabType, index, skillType, title, backgroundTexture);
            }

            index -= tabType.getMax();
        }

        throw new RuntimeException("WTF");
    }

    // render function

    /**
     * Render the tab texture
     */
    public void renderTab(MatrixStack matrixStack, int x, int y, boolean selected) {
        this.type.draw(matrixStack, x, y, selected, this.index);
        int i = screen.width;
        int j = screen.height;
    }

    /**
     * Render the icon on tab
     */
    public void renderIcon(MatrixStack matrixStack, int x, int y) {
        this.minecraft.getTextureManager().bind(SKILL_ICON_LOCATION);
        this.type.drawIcon(matrixStack, x, y, this.index, this.iconU, iconV);
    }

    /**
     * Render the tab inside body
     */
    public void renderContents(MatrixStack matrixStack) {
        if (!this.centered) {
            this.scrollX = 117 - (this.maxX + this.minX) / 2;
            this.scrollY = 56 - (this.maxY + this.minY) / 2;
            this.centered = true;
        }

        RenderSystem.pushMatrix();
        RenderSystem.enableDepthTest();
        RenderSystem.translatef(0.0F, 0.0F, 950.0F);
        RenderSystem.colorMask(false, false, false, false);
        Draw.fill(matrixStack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.translatef(0.0F, 0.0F, -950.0F);
        RenderSystem.depthFunc(518);
        Draw.fill(matrixStack, 234, 113, 0, 0, -16777216);
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

        this.root.renderConnectivity(matrixStack, i, j, true);
        this.root.renderConnectivity(matrixStack, i, j, false);
        this.root.render(matrixStack, i, j);
        RenderSystem.depthFunc(518);
        RenderSystem.translatef(0.0F, 0.0F, -950.0F);
        RenderSystem.colorMask(false, false, false, false);
        Draw.fill(matrixStack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.translatef(0.0F, 0.0F, 950.0F);
        RenderSystem.depthFunc(515);
        RenderSystem.popMatrix();
    }

    /**
     * Render the tooltip on tab
     */
    public void renderTooltips(MatrixStack matrixStack, int xRelative, int yRelative, int xOffset, int yOffset) {
        RenderSystem.pushMatrix();
        RenderSystem.translatef(0.0F, 0.0F, 200.0F);
        Draw.fill(matrixStack, 0, 0, 234, 113, MathHelper.floor(this.fade * 255.0F) << 24);
        boolean flag = false;
        int x = MathHelper.floor(this.scrollX);
        int y = MathHelper.floor(this.scrollY);
        if (xRelative > 0 && xRelative < 234 && yRelative > 0 && yRelative < 113) {
            for (SkillEntryGui entryGui : this.widgets.values()) {
                if (entryGui.isMouseOver(x, y, xRelative, yRelative)) {
                    flag = true;
                    entryGui.renderHover(matrixStack, x, y, this.fade, xOffset, yOffset);
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

    // gui controller

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

    public double getScrollX() {
        return scrollX;
    }

    public double getScrollY() {
        return scrollY;
    }

    // skill related

    // update this bound region
    private void updateRegion() {
        for (SkillEntryGui entryGui : this.widgets.values()) {
            int x0 = entryGui.getX();
            int x1 = x0 + 32;
            int y0 = entryGui.getY();
            int y1 = y0 + 32;
            this.minX = Math.min(this.minX, x0);
            this.maxX = Math.max(this.maxX, x1);
            this.minY = Math.min(this.minY, y0);
            this.maxY = Math.max(this.maxY, y1);
        }
    }

    // getters

    public int getPage() {
        return page;
    }

    public ITextComponent getTitle() {
        return this.title;
    }

    @Nullable
    public SkillEntryGui getWidget(SkillType skillType) {
        return this.widgets.get(skillType);
    }

    public Map<SkillType, SkillEntryGui> getWidgets() {
        return widgets;
    }

    public SkillScreen getScreen() {
        return this.screen;
    }
}

