package com.dozono.dyinglightmod.gui;

import com.dozono.dyinglightmod.skill.SkillType;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.advancements.SkillType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.advancements.SkillTypeState;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.CharacterManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.dozono.dyinglightmod.gui.SkillScreen.SKILL_ICON_LOCATION;

@OnlyIn(Dist.CLIENT)
public class SkillEntryGui extends AbstractGui {
    private static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/advancements/widgets.png");
    public static final int ICON_DIMENSION = 32;
    public static final int ICON_TEXTURE_DIMENSION = 256;

    private static final int[] TEST_SPLIT_OFFSETS = new int[]{0, 10, -10, 25, -25};
    private final SkillTabGui tab;
    private final SkillType skillType;
    private final IReorderingProcessor title;
    private final int width;
    private final List<IReorderingProcessor> description;
    private final Minecraft minecraft;
    private SkillEntryGui parent;
    private final List<SkillEntryGui> children = Lists.newArrayList();
    //    private SkillTypeProgress progress;
    private final int x;
    private final int y;

    public SkillEntryGui(SkillTabGui tab, Minecraft minecraft, SkillType skillType) {
        this.tab = tab;
        this.skillType = skillType;
        this.minecraft = minecraft;
        this.title = LanguageMap.getInstance().getVisualOrder(minecraft.font.substrByWidth(info.getTitle(), 163));
//        this.x = MathHelper.floor(info.getX() * 28.0F);
//        this.y = MathHelper.floor(info.getY() * 27.0F);
//        int i = skillType.getMaxCriteraRequired();
//        int j = String.valueOf(i).length();
//        int k = i > 1 ? minecraft.font.width("  ") + minecraft.font.width("0") * j * 2 + minecraft.font.width("/") : 0;
        int l = 29 + minecraft.font.width(this.title) + k;
        this.description = LanguageMap.getInstance().getVisualOrder(this.findOptimalLines(TextComponentUtils.mergeStyles(info.getDescription().copy(), Style.EMPTY.withColor(info.getFrame().getChatColor())), l));

        for (IReorderingProcessor ireorderingprocessor : this.description) {
            l = Math.max(l, minecraft.font.width(ireorderingprocessor));
        }

        this.width = l + 3 + 5;
    }

    private static float getMaxWidth(CharacterManager p_238693_0_, List<ITextProperties> p_238693_1_) {
        return (float) p_238693_1_.stream().mapToDouble(p_238693_0_::stringWidth).max().orElse(0.0D);
    }

    private List<ITextProperties> findOptimalLines(ITextComponent p_238694_1_, int p_238694_2_) {
        CharacterManager charactermanager = this.minecraft.font.getSplitter();
        List<ITextProperties> list = null;
        float f = Float.MAX_VALUE;

        for (int i : TEST_SPLIT_OFFSETS) {
            List<ITextProperties> list1 = charactermanager.splitLines(p_238694_1_, p_238694_2_ - i, Style.EMPTY);
            float f1 = Math.abs(getMaxWidth(charactermanager, list1) - (float) p_238694_2_);
            if (f1 <= 10.0F) {
                return list1;
            }

            if (f1 < f) {
                f = f1;
                list = list1;
            }
        }

        return list;
    }

    @Nullable
    private SkillEntryGui getFirstVisibleParent(SkillType skillType) {
        skillType = skillType.getParent();
        return skillType != null ? this.tab.getWidget(skillType) : null;
    }

    public void drawConnectivity(MatrixStack matrixStack, int x, int y, boolean p_238692_4_) {
        if (this.parent != null) {
            int i = x + this.parent.x + 13;
            int j = x + this.parent.x + 26 + 4;
            int k = y + this.parent.y + 13;
            int l = x + this.x + 13;
            int i1 = y + this.y + 13;
            int j1 = p_238692_4_ ? -16777216 : -1;
            if (p_238692_4_) {
                this.hLine(matrixStack, j, i, k - 1, j1);
                this.hLine(matrixStack, j + 1, i, k, j1);
                this.hLine(matrixStack, j, i, k + 1, j1);
                this.hLine(matrixStack, l, j - 1, i1 - 1, j1);
                this.hLine(matrixStack, l, j - 1, i1, j1);
                this.hLine(matrixStack, l, j - 1, i1 + 1, j1);
                this.vLine(matrixStack, j - 1, i1, k, j1);
                this.vLine(matrixStack, j + 1, i1, k, j1);
            } else {
                this.hLine(matrixStack, j, i, k, j1);
                this.hLine(matrixStack, l, j, i1, j1);
                this.vLine(matrixStack, j, i1, k, j1);
            }
        }

        for (SkillEntryGui child : this.children) {
            child.drawConnectivity(matrixStack, x, y, p_238692_4_);
        }
    }

    public void draw(MatrixStack matrixStack, int x, int y) {
//            float f = this.progress == null ? 0.0F : this.progress.getPercent();
//        SkillTypeState state;
//        if (f >= 1.0F) {
//            state = SkillTypeState.OBTAINED;
//        } else {
//            state = SkillTypeState.UNOBTAINED;
//        }

//        this.minecraft.getTextureManager().bind(WIDGETS_LOCATION);
//        this.blit(p_238688_1_, p_238688_2_ + this.x + 3, p_238688_3_ + this.y, this.display.getFrame().getTexture(), 128 + state.getIndex() * 26, 26, 26);
        this.minecraft.getTextureManager().bind(SKILL_ICON_LOCATION);
        // TODO render the

        int u = 0;
        int v = 0;
        int uOffset = this.skillType.getIndex() * ICON_DIMENSION;
        if (uOffset + ICON_DIMENSION >= ICON_TEXTURE_DIMENSION) {
            int vIndex = (uOffset + ICON_DIMENSION) / ICON_TEXTURE_DIMENSION;
            v = vIndex * ICON_DIMENSION;
            u = (uOffset + ICON_DIMENSION) % ICON_TEXTURE_DIMENSION;
        } else {
            u = uOffset;
        }
//        1 -> 32
//        2 -> 64
//        8 -> 288 ~ 1  0
        Draw.blit(matrixStack, x + this.x + 3, y + this.y, u, v, ICON_DIMENSION, ICON_DIMENSION);

        for (SkillEntryGui entryGui : this.children) {
            entryGui.draw(matrixStack, x, y);
        }
    }

//    public void setProgress(SkillTypeProgress p_191824_1_) {
//        this.progress = p_191824_1_;
//    }

    public void addChild(SkillEntryGui p_191822_1_) {
        this.children.add(p_191822_1_);
    }

    public void drawHover(MatrixStack matrixStack, int x, int y, float transparency, int p_238689_5_, int p_238689_6_) {
        boolean flag = p_238689_5_ + x + this.x + this.width + 26 >= this.tab.getScreen().width;
//        String s = this.progress == null ? null : this.progress.getProgressText();
//        int i = s == null ? 0 : this.minecraft.font.width(s);
        boolean flag1 = 113 - y - this.y - 26 <= 6 + this.description.size() * 9;
//        float f = this.progress == null ? 0.0F : this.progress.getPercent();
//        int j = MathHelper.floor(f * (float) this.width);
        SkillTypeState advancementstate;
        SkillTypeState advancementstate1;
        SkillTypeState advancementstate2;
        if (f >= 1.0F) {
            j = this.width / 2;
            advancementstate = SkillTypeState.OBTAINED;
            advancementstate1 = SkillTypeState.OBTAINED;
            advancementstate2 = SkillTypeState.OBTAINED;
        } else if (j < 2) {
            j = this.width / 2;
            advancementstate = SkillTypeState.UNOBTAINED;
            advancementstate1 = SkillTypeState.UNOBTAINED;
            advancementstate2 = SkillTypeState.UNOBTAINED;
        } else if (j > this.width - 2) {
            j = this.width / 2;
            advancementstate = SkillTypeState.OBTAINED;
            advancementstate1 = SkillTypeState.OBTAINED;
            advancementstate2 = SkillTypeState.UNOBTAINED;
        } else {
            advancementstate = SkillTypeState.OBTAINED;
            advancementstate1 = SkillTypeState.UNOBTAINED;
            advancementstate2 = SkillTypeState.UNOBTAINED;
        }

        int k = this.width - j;
        this.minecraft.getTextureManager().bind(WIDGETS_LOCATION);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        int l = y + this.y;
        int x;
        if (flag) {
            x = x + this.x - this.width + 26 + 6;
        } else {
            x = x + this.x;
        }

        int height = 32 + this.description.size() * 9;
        if (!this.description.isEmpty()) {
            if (flag1) {
                this.render9Sprite(matrixStack, x, l + 26 - height, this.width, height, 10, 200, 26, 0, 52);
            } else {
                this.render9Sprite(matrixStack, x, l, this.width, height, 10, 200, 26, 0, 52);
            }
        }

        this.blit(matrixStack, x, l, 0, advancementstate.getIndex() * 26, j, 26);
        this.blit(matrixStack, x + j, l, 200 - k, advancementstate1.getIndex() * 26, k, 26);
        this.blit(matrixStack, x + this.x + 3, y + this.y, this.display.getFrame().getTexture(), 128 + advancementstate2.getIndex() * 26, 26, 26);
        if (flag) {
            this.minecraft.font.drawShadow(matrixStack, this.title, (float) (x + 5), (float) (y + this.y + 9), -1);
            if (s != null) {
                this.minecraft.font.drawShadow(matrixStack, s, (float) (x + this.x - i), (float) (y + this.y + 9), -1);
            }
        } else {
            this.minecraft.font.drawShadow(matrixStack, this.title, (float) (x + this.x + 32), (float) (y + this.y + 9), -1);
            if (s != null) {
                this.minecraft.font.drawShadow(matrixStack, s, (float) (x + this.x + this.width - i - 5), (float) (y + this.y + 9), -1);
            }
        }

        if (flag1) {
            for (int k1 = 0; k1 < this.description.size(); ++k1) {
                this.minecraft.font.draw(matrixStack, this.description.get(k1), (float) (x + 5), (float) (l + 26 - height + 7 + k1 * 9), -5592406);
            }
        } else {
            for (int l1 = 0; l1 < this.description.size(); ++l1) {
                this.minecraft.font.draw(matrixStack, this.description.get(l1), (float) (x + 5), (float) (y + this.y + 9 + 17 + l1 * 9), -5592406);
            }
        }

        this.minecraft.getItemRenderer().renderAndDecorateFakeItem(this.display.getIcon(), x + this.x + 8, y + this.y + 5);
    }

    protected void render9Sprite(MatrixStack matrixStack, int x, int y, int w, int h, int p_238691_6_, int p_238691_7_, int p_238691_8_, int p_238691_9_, int p_238691_10_) {
        this.blit(matrixStack, x, y, p_238691_9_, p_238691_10_, p_238691_6_, p_238691_6_);
        this.renderRepeating(matrixStack, x + p_238691_6_, y, w - p_238691_6_ - p_238691_6_, p_238691_6_, p_238691_9_ + p_238691_6_, p_238691_10_, p_238691_7_ - p_238691_6_ - p_238691_6_, p_238691_8_);
        this.blit(matrixStack, x + w - p_238691_6_, y, p_238691_9_ + p_238691_7_ - p_238691_6_, p_238691_10_, p_238691_6_, p_238691_6_);
        this.blit(matrixStack, x, y + h - p_238691_6_, p_238691_9_, p_238691_10_ + p_238691_8_ - p_238691_6_, p_238691_6_, p_238691_6_);
        this.renderRepeating(matrixStack, x + p_238691_6_, y + h - p_238691_6_, w - p_238691_6_ - p_238691_6_, p_238691_6_, p_238691_9_ + p_238691_6_, p_238691_10_ + p_238691_8_ - p_238691_6_, p_238691_7_ - p_238691_6_ - p_238691_6_, p_238691_8_);
        this.blit(matrixStack, x + w - p_238691_6_, y + h - p_238691_6_, p_238691_9_ + p_238691_7_ - p_238691_6_, p_238691_10_ + p_238691_8_ - p_238691_6_, p_238691_6_, p_238691_6_);
        this.renderRepeating(matrixStack, x, y + p_238691_6_, p_238691_6_, h - p_238691_6_ - p_238691_6_, p_238691_9_, p_238691_10_ + p_238691_6_, p_238691_7_, p_238691_8_ - p_238691_6_ - p_238691_6_);
        this.renderRepeating(matrixStack, x + p_238691_6_, y + p_238691_6_, w - p_238691_6_ - p_238691_6_, h - p_238691_6_ - p_238691_6_, p_238691_9_ + p_238691_6_, p_238691_10_ + p_238691_6_, p_238691_7_ - p_238691_6_ - p_238691_6_, p_238691_8_ - p_238691_6_ - p_238691_6_);
        this.renderRepeating(matrixStack, x + w - p_238691_6_, y + p_238691_6_, p_238691_6_, h - p_238691_6_ - p_238691_6_, p_238691_9_ + p_238691_7_ - p_238691_6_, p_238691_10_ + p_238691_6_, p_238691_7_, p_238691_8_ - p_238691_6_ - p_238691_6_);
    }

    protected void renderRepeating(MatrixStack p_238690_1_, int p_238690_2_, int p_238690_3_, int p_238690_4_, int p_238690_5_, int p_238690_6_, int p_238690_7_, int p_238690_8_, int p_238690_9_) {
        for (int i = 0; i < p_238690_4_; i += p_238690_8_) {
            int j = p_238690_2_ + i;
            int k = Math.min(p_238690_8_, p_238690_4_ - i);

            for (int l = 0; l < p_238690_5_; l += p_238690_9_) {
                int i1 = p_238690_3_ + l;
                int j1 = Math.min(p_238690_9_, p_238690_5_ - l);
                this.blit(p_238690_1_, j, i1, p_238690_6_, p_238690_7_, k, j1);
            }
        }

    }

    public boolean isMouseOver(int p_191816_1_, int p_191816_2_, int p_191816_3_, int p_191816_4_) {
        if (!this.display.isHidden() || this.progress != null && this.progress.isDone()) {
            int i = p_191816_1_ + this.x;
            int j = i + 26;
            int k = p_191816_2_ + this.y;
            int l = k + 26;
            return p_191816_3_ >= i && p_191816_3_ <= j && p_191816_4_ >= k && p_191816_4_ <= l;
        } else {
            return false;
        }
    }

    public void attachToParent() {
        if (this.parent == null && this.skillType.getParent() != null) {
            this.parent = this.getFirstVisibleParent(this.skillType);
            if (this.parent != null) {
                this.parent.addChild(this);
            }
        }

    }

    public int getY() {
        return this.y;
    }

    public int getX() {
        return this.x;
    }
}
