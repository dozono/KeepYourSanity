package com.dozono.dyinglightmod.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
enum SkillTabType {
    ABOVE(0, 0, 28, 32, 8),
    BELOW(84, 0, 28, 32, 8),
    LEFT(0, 64, 32, 28, 5),
    RIGHT(96, 64, 32, 28, 5);

    public static final int MAX_TABS = java.util.Arrays.stream(values()).mapToInt(e -> e.max).sum();
    private final int textureX;
    private final int textureY;
    private final int width;
    private final int height;
    private final int max;

    private SkillTabType(int p_i47386_3_, int p_i47386_4_, int p_i47386_5_, int p_i47386_6_, int p_i47386_7_) {
        this.textureX = p_i47386_3_;
        this.textureY = p_i47386_4_;
        this.width = p_i47386_5_;
        this.height = p_i47386_6_;
        this.max = p_i47386_7_;
    }

    public int getMax() {
        return this.max;
    }

    public void draw(MatrixStack matrixStack, AbstractGui gui, int p_238686_3_, int p_238686_4_, boolean selected, int p_238686_6_) {
        int i = this.textureX;
        if (p_238686_6_ > 0) {
            i += this.width;
        }

        if (p_238686_6_ == this.max - 1) {
            i += this.width;
        }

        int j = selected ? this.textureY + this.height : this.textureY;
        gui.blit(matrixStack, p_238686_3_ + this.getX(p_238686_6_), p_238686_4_ + this.getY(p_238686_6_), i, j, this.width, this.height);
    }

    public void drawIcon(int x, int y, int index, ItemRenderer render, ItemStack p_192652_5_) {
        int i = x + this.getX(index);
        int j = y + this.getY(index);
        switch (this) {
            case ABOVE:
                i += 6;
                j += 9;
                break;
            case BELOW:
                i += 6;
                j += 6;
                break;
            case LEFT:
                i += 10;
                j += 5;
                break;
            case RIGHT:
                i += 6;
                j += 5;
        }

        render.renderAndDecorateFakeItem(p_192652_5_, i, j);
    }

    public int getX(int index) {
        switch (this) {
            case ABOVE:
                return (this.width + 4) * index;
            case BELOW:
                return (this.width + 4) * index;
            case LEFT:
                return -this.width + 4;
            case RIGHT:
                return 248;
            default:
                throw new UnsupportedOperationException("Don't know what this tab type is!" + this);
        }
    }

    public int getY(int index) {
        switch (this) {
            case ABOVE:
                return -this.height + 4;
            case BELOW:
                return 136;
            case LEFT:
                return this.height * index;
            case RIGHT:
                return this.height * index;
            default:
                throw new UnsupportedOperationException("Don't know what this tab type is!" + this);
        }
    }

    public boolean isMouseOver(int p_198891_1_, int p_198891_2_, int p_198891_3_, double p_198891_4_, double p_198891_6_) {
        int i = p_198891_1_ + this.getX(p_198891_3_);
        int j = p_198891_2_ + this.getY(p_198891_3_);
        return p_198891_4_ > (double) i && p_198891_4_ < (double) (i + this.width) && p_198891_6_ > (double) j && p_198891_6_ < (double) (j + this.height);
    }
}
