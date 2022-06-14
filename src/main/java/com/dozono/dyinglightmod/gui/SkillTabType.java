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

    SkillTabType(int textureX, int textureY, int width, int height, int max) {
        this.textureX = textureX;
        this.textureY = textureY;
        this.width = width;
        this.height = height;
        this.max = max;
    }

    public int getMax() {
        return this.max;
    }

    public void draw(MatrixStack matrixStack, int x, int y, boolean selected, int index) {
        int u = this.textureX;
        if (index > 0) {
            u += this.width;
        }

        if (index == this.max - 1) {
            u += this.width;
        }

        int v = selected ? this.textureY + this.height : this.textureY;
        Draw.blit(matrixStack, x + this.getX(index), y + this.getY(index), u, v, this.width, this.height);
    }

    public void drawIcon(MatrixStack matrixStack, int x, int y, int index, IconSprite sprite) {
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

        Draw.blit(matrixStack, i, j, 16, 16, sprite.u, sprite.v, sprite.w, sprite.h, 256, 256);
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
