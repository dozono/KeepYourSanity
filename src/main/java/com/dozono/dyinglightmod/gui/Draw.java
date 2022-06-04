package com.dozono.dyinglightmod.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;

import java.util.function.BiConsumer;

public class Draw {
    protected static void hLine(MatrixStack p_238465_1_, int p_238465_2_, int p_238465_3_, int p_238465_4_, int p_238465_5_) {
        if (p_238465_3_ < p_238465_2_) {
            int i = p_238465_2_;
            p_238465_2_ = p_238465_3_;
            p_238465_3_ = i;
        }

        fill(p_238465_1_, p_238465_2_, p_238465_4_, p_238465_3_ + 1, p_238465_4_ + 1, p_238465_5_);
    }

    protected static void vLine(MatrixStack p_238473_1_, int p_238473_2_, int p_238473_3_, int p_238473_4_, int p_238473_5_) {
        if (p_238473_4_ < p_238473_3_) {
            int i = p_238473_3_;
            p_238473_3_ = p_238473_4_;
            p_238473_4_ = i;
        }

        fill(p_238473_1_, p_238473_2_, p_238473_3_ + 1, p_238473_2_ + 1, p_238473_4_, p_238473_5_);
    }

    public static void fill(MatrixStack matrixStack, int x0, int y0, int x1, int y1, int color) {
        innerFill(matrixStack.last().pose(), x0, y0, x1, y1, color);
    }

    private static void innerFill(Matrix4f matrix4f, int x0, int y0, int x1, int y1, int colorCode) {
        if (x0 < x1) {
            int i = x0;
            x0 = x1;
            x1 = i;
        }

        if (y0 < y1) {
            int j = y0;
            y0 = y1;
            y1 = j;
        }

        float a = (float) (colorCode >> 24 & 255) / 255.0F;
        float r = (float) (colorCode >> 16 & 255) / 255.0F;
        float g = (float) (colorCode >> 8 & 255) / 255.0F;
        float b = (float) (colorCode & 255) / 255.0F;
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.vertex(matrix4f, (float) x0, (float) y1, 0.0F).color(r, g, b, a).endVertex();
        bufferbuilder.vertex(matrix4f, (float) x1, (float) y1, 0.0F).color(r, g, b, a).endVertex();
        bufferbuilder.vertex(matrix4f, (float) x1, (float) y0, 0.0F).color(r, g, b, a).endVertex();
        bufferbuilder.vertex(matrix4f, (float) x0, (float) y0, 0.0F).color(r, g, b, a).endVertex();
        bufferbuilder.end();
        WorldVertexBufferUploader.end(bufferbuilder);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    protected static void fillGradient(MatrixStack p_238468_1_, int p_238468_2_, int p_238468_3_, int p_238468_4_, int p_238468_5_, int p_238468_6_, int p_238468_7_) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        fillGradient(p_238468_1_.last().pose(), bufferbuilder, p_238468_2_, p_238468_3_, p_238468_4_, p_238468_5_, 0, p_238468_6_, p_238468_7_);
        tessellator.end();
        RenderSystem.shadeModel(7424);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
    }

    protected static void fillGradient(Matrix4f p_238462_0_, BufferBuilder p_238462_1_, int p_238462_2_, int p_238462_3_, int p_238462_4_, int p_238462_5_, int p_238462_6_, int p_238462_7_, int p_238462_8_) {
        float f = (float) (p_238462_7_ >> 24 & 255) / 255.0F;
        float f1 = (float) (p_238462_7_ >> 16 & 255) / 255.0F;
        float f2 = (float) (p_238462_7_ >> 8 & 255) / 255.0F;
        float f3 = (float) (p_238462_7_ & 255) / 255.0F;
        float f4 = (float) (p_238462_8_ >> 24 & 255) / 255.0F;
        float f5 = (float) (p_238462_8_ >> 16 & 255) / 255.0F;
        float f6 = (float) (p_238462_8_ >> 8 & 255) / 255.0F;
        float f7 = (float) (p_238462_8_ & 255) / 255.0F;
        p_238462_1_.vertex(p_238462_0_, (float) p_238462_4_, (float) p_238462_3_, (float) p_238462_6_).color(f1, f2, f3, f).endVertex();
        p_238462_1_.vertex(p_238462_0_, (float) p_238462_2_, (float) p_238462_3_, (float) p_238462_6_).color(f1, f2, f3, f).endVertex();
        p_238462_1_.vertex(p_238462_0_, (float) p_238462_2_, (float) p_238462_5_, (float) p_238462_6_).color(f5, f6, f7, f4).endVertex();
        p_238462_1_.vertex(p_238462_0_, (float) p_238462_4_, (float) p_238462_5_, (float) p_238462_6_).color(f5, f6, f7, f4).endVertex();
    }

    public static void drawCenteredString(MatrixStack p_238471_0_, FontRenderer p_238471_1_, String p_238471_2_, int p_238471_3_, int p_238471_4_, int p_238471_5_) {
        p_238471_1_.drawShadow(p_238471_0_, p_238471_2_, (float) (p_238471_3_ - p_238471_1_.width(p_238471_2_) / 2), (float) p_238471_4_, p_238471_5_);
    }

    public static void drawCenteredString(MatrixStack matrixStack, FontRenderer fontRenderer, ITextComponent text, int x, int y, int p_238472_5_) {
        IReorderingProcessor ireorderingprocessor = text.getVisualOrderText();
        fontRenderer.drawShadow(matrixStack, ireorderingprocessor, (float) (x - fontRenderer.width(ireorderingprocessor) / 2), (float) y, p_238472_5_);
    }

    public static void drawString(MatrixStack p_238476_0_, FontRenderer p_238476_1_, String p_238476_2_, int p_238476_3_, int p_238476_4_, int p_238476_5_) {
        p_238476_1_.drawShadow(p_238476_0_, p_238476_2_, (float) p_238476_3_, (float) p_238476_4_, p_238476_5_);
    }

    public static void drawString(MatrixStack p_238475_0_, FontRenderer p_238475_1_, ITextComponent p_238475_2_, int p_238475_3_, int p_238475_4_, int p_238475_5_) {
        p_238475_1_.drawShadow(p_238475_0_, p_238475_2_, (float) p_238475_3_, (float) p_238475_4_, p_238475_5_);
    }

    public static void blitOutlineBlack(int p_238459_1_, int p_238459_2_, BiConsumer<Integer, Integer> p_238459_3_) {
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        p_238459_3_.accept(p_238459_1_ + 1, p_238459_2_);
        p_238459_3_.accept(p_238459_1_ - 1, p_238459_2_);
        p_238459_3_.accept(p_238459_1_, p_238459_2_ + 1);
        p_238459_3_.accept(p_238459_1_, p_238459_2_ - 1);
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        p_238459_3_.accept(p_238459_1_, p_238459_2_);
    }

    public static void blit(MatrixStack matrixStack, int x, int y, int z, int w, int h, TextureAtlasSprite atlasSprite) {
        innerBlit(matrixStack.last().pose(), x, x + w, y, y + h, z, atlasSprite.getU0(), atlasSprite.getU1(), atlasSprite.getV0(), atlasSprite.getV1());
    }

    public static void blit(MatrixStack matrixStack, int x, int y, int u, int v, int w, int h) {
        blit(matrixStack, x, y, 0, (float) u, (float) v, w, h, 256, 256);
    }

    public static void blit(MatrixStack matrixStack, int x, int y, int z, float u, float v, int w, int h, int textureHeight, int textureWidth) {
        innerBlit(matrixStack, x, x + w, y, y + h, z, w, h, u, v, textureWidth, textureHeight);
    }

    public static void blit(MatrixStack matrixStack, int x, int y, int w, int h, float u, float v, int uWidth, int vHeight, int textureWidth, int textureHeight) {
        innerBlit(matrixStack, x, x + w, y, y + h, 0, uWidth, vHeight, u, v, textureWidth, textureHeight);
    }

    public static void blit(MatrixStack matrixStack, int x, int y, float u, float v, int w, int h, int textureWidth, int textureHeight) {
        blit(matrixStack, x, y, w, h, u, v, w, h, textureWidth, textureHeight);
    }

    private static void innerBlit(MatrixStack matrixStack, int x1, int x2, int y1, int y2, int z, int w, int h, float u, float v, int textureWidth, int textureHeight) {
        innerBlit(matrixStack.last().pose(), x1, x2, y1, y2, z, (u + 0.0F) / (float) textureWidth, (u + (float) w) / (float) textureWidth, (v + 0.0F) / (float) textureHeight, (v + (float) h) / (float) textureHeight);
    }

    private static void innerBlit(Matrix4f matrix4f, int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2) {
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.vertex(matrix4f, (float) x1, (float) y2, (float) z).uv(u1, v2).endVertex();
        bufferbuilder.vertex(matrix4f, (float) x2, (float) y2, (float) z).uv(u2, v2).endVertex();
        bufferbuilder.vertex(matrix4f, (float) x2, (float) y1, (float) z).uv(u2, v1).endVertex();
        bufferbuilder.vertex(matrix4f, (float) x1, (float) y1, (float) z).uv(u1, v1).endVertex();
        bufferbuilder.end();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.end(bufferbuilder);
    }
}
