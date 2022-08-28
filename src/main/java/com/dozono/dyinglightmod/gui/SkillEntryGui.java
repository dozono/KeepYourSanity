package com.dozono.dyinglightmod.gui;

import com.dozono.dyinglightmod.DyingLight;
import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillLevelUpMessage;
import com.dozono.dyinglightmod.skill.SkillType;
import com.dozono.dyinglightmod.skill.survival.SkillTypeMandom;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import java.util.List;
import java.util.Optional;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.dozono.dyinglightmod.gui.SkillScreen.SKILL_ICON_LOCATION;

@OnlyIn(Dist.CLIENT)
public class SkillEntryGui implements IGuiEventListener {
    public static final int ICON_DIMENSION = 32;
    public static final int ICON_TEXTURE_DIMENSION = 256;

    private final SkillTabGui tab;
    private final SkillType skillType;
    private final IReorderingProcessor title;
    private final int width;
    private final List<IReorderingProcessor> description;
    private final Minecraft minecraft;
    private SkillEntryGui parent;
    private final List<SkillEntryGui> children;
    //    private SkillTypeProgress progress;
    private int x = 0;
    private int y = 0;
    private int u = 0;
    private int v = 0;
    Skill skill;

    public SkillEntryGui(Minecraft minecraft, SkillTabGui tab, SkillType skillType, int x, int y, int u, int v, List<SkillEntryGui> children) {
        if (minecraft.player != null) {
            minecraft.player.getCapability(DyingLight.CapabilitySkillContainer).ifPresent(c -> {
                c.getSkill(skillType).ifPresent(s -> {
                    skill = s;
                });
            });
        }
        this.tab = tab;
        this.skillType = skillType;
        this.minecraft = minecraft;
        this.children = children;
        TranslationTextComponent title = new TranslationTextComponent("dyinglight.skill." + skillType.getRegistryName().getPath() + ".title");
        this.title = LanguageMap.getInstance().getVisualOrder(minecraft.font.substrByWidth(title, 163));

        this.x = x;
        this.y = y;
        this.u = u;
        this.v = v;

        int l = 29 + minecraft.font.width(this.title);
        TranslationTextComponent description = new TranslationTextComponent("dyinglight.skill." + skillType.getRegistryName().getPath() + ".description");
        this.description = LanguageMap.getInstance().getVisualOrder(TextComponentUtil.splitOptimalLines(description, l));

        for (IReorderingProcessor ireorderingprocessor : this.description) {
            l = Math.max(l, minecraft.font.width(ireorderingprocessor));
        }

        this.width = l + 3 + 5;

        for (SkillEntryGui child : children) {
            child.parent = this;
        }
    }


    public void renderConnectivity(MatrixStack matrixStack, int x, int y, boolean p_238692_4_) {
        if (this.parent != null) {
            int i = x + this.parent.x + 13;
            int j = x + this.parent.x + 26 + 4;
            int k = y + this.parent.y + 13;
            int l = x + this.x + 13;
            int i1 = y + this.y + 13;
            int j1 = p_238692_4_ ? -16777216 : -1;
            if (p_238692_4_) {
                Draw.hLine(matrixStack, j, i, k - 1, j1);
                Draw.hLine(matrixStack, j + 1, i, k, j1);
                Draw.hLine(matrixStack, j, i, k + 1, j1);
                Draw.hLine(matrixStack, l, j - 1, i1 - 1, j1);
                Draw.hLine(matrixStack, l, j - 1, i1, j1);
                Draw.hLine(matrixStack, l, j - 1, i1 + 1, j1);
                Draw.vLine(matrixStack, j - 1, i1, k, j1);
                Draw.vLine(matrixStack, j + 1, i1, k, j1);
            } else {
                Draw.hLine(matrixStack, j, i, k, j1);
                Draw.hLine(matrixStack, l, j, i1, j1);
                Draw.vLine(matrixStack, j, i1, k, j1);
            }
        }

        for (SkillEntryGui child : this.children) {
//            child.renderConnectivity(matrixStack, x, y, p_238692_4_);
        }
    }

    /**
     * Render the skill icon
     */
    public void render(MatrixStack matrixStack, int x, int y) {
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
        Draw.blit(matrixStack, x + this.x + 3, y + this.y, 16, 16, u, v, 32, 32, 256, 256);

//        this.minecraft.font.drawShadow(matrixStack, this.skill.getLevel() + "", (float) (this.x + x + 15), (float) (this.y + y - 2), -1);

        for (SkillEntryGui entryGui : this.children) {
            entryGui.render(matrixStack, x, y);
        }
    }


    public void renderHover(MatrixStack matrixStack, int x, int y, float transparency, int xOff, int yOff) {
        boolean flag = xOff + x + this.x + this.width + 26 >= this.tab.getScreen().width;
//        String s = this.progress == null ? null : this.progress.getProgressText();
//        int i = s == null ? 0 : this.minecraft.font.width(s);
        boolean flag1 = 113 - y - this.y - 26 <= 6 + this.description.size() * 9;
//        float f = this.progress == null ? 0.0F : this.progress.getPercent();
//        int j = MathHelper.floor(f * (float) this.width);
//        SkillTypeState advancementstate;
//        SkillTypeState advancementstate1;
//        SkillTypeState advancementstate2;
//        if (f >= 1.0F) {
//            j = this.width / 2;
//            advancementstate = SkillTypeState.OBTAINED;
//            advancementstate1 = SkillTypeState.OBTAINED;
//            advancementstate2 = SkillTypeState.OBTAINED;
//        } else if (j < 2) {
//            j = this.width / 2;
//            advancementstate = SkillTypeState.UNOBTAINED;
//            advancementstate1 = SkillTypeState.UNOBTAINED;
//            advancementstate2 = SkillTypeState.UNOBTAINED;
//        } else if (j > this.width - 2) {
//            j = this.width / 2;
//            advancementstate = SkillTypeState.OBTAINED;
//            advancementstate1 = SkillTypeState.OBTAINED;
//            advancementstate2 = SkillTypeState.UNOBTAINED;
//        } else {
//            advancementstate = SkillTypeState.OBTAINED;
//            advancementstate1 = SkillTypeState.UNOBTAINED;
//            advancementstate2 = SkillTypeState.UNOBTAINED;
//        }

        int k = this.width / 2;
        this.minecraft.getTextureManager().bind(new ResourceLocation("textures/gui/advancements/widgets.png"));
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        int l = y + this.y;
        int sx = 0;
        if (flag) {
            sx = x + this.x - this.width + 26 + 6;
        } else {
            sx = x + this.x;
        }
        sx -= 1;
        l -= 4;
        int height = 32 + this.description.size() * 9 + 9 /* for cost */;
        if (!this.description.isEmpty()) {
            if (flag1) {
                this.render9Sprite(matrixStack, sx, l + 26 - height, this.width, height, 10, 200, 26, 0, 52);
            } else {
                this.render9Sprite(matrixStack, sx, l, this.width, height, 10, 200, 26, 0, 52);
            }
        }

        Draw.blit(matrixStack, sx, l, 0, 1 * 26, k, 26);
        Draw.blit(matrixStack, sx + k, l, 200 - k, 1 * 26, k, 26);
//        Draw.blit(matrixStack, x + this.x + 3, y + this.y, this.display.getFrame().getTexture(), 128 + 1 * 26, 26, 26);
        if (flag) {
            this.minecraft.font.drawShadow(matrixStack, this.title, (float) (sx + 5), (float) (l + 9), -1);
//            if (s != null) {
//                this.minecraft.font.drawShadow(matrixStack, s, (float) (x + this.x - i), (float) (y + this.y + 9), -1);
//            }
        } else {
            this.minecraft.font.drawShadow(matrixStack, this.title, (float) (sx + 25), (float) (l + 9), -1);
//            if (s != null) {
//                this.minecraft.font.drawShadow(matrixStack, s, (float) (x + this.x + this.width - i - 5), (float) (y + this.y + 9), -1);
//            }
        }

        if (flag1) {
            for (int k1 = 0; k1 < this.description.size(); ++k1) {
                this.minecraft.font.draw(matrixStack, this.description.get(k1), (float) (sx + 5), (float) (l + 26 - height + 7 + k1 * 9), -5592406);
            }
            this.minecraft.font.draw(matrixStack, this.skill.getCost() + "", (float) (sx + 5), (float) (l + 26 - height + 7 + this.description.size() * 9), -5592406);
        } else {
            for (int l1 = 0; l1 < this.description.size(); ++l1) {
                this.minecraft.font.draw(matrixStack, this.description.get(l1), (float) (sx + 5), (float) (l + 9 + 17 + l1 * 9), -5592406);
            }
            this.minecraft.font.draw(matrixStack, this.skill.getCost() + "", (float) (sx + 5), (float) (l + 9 + 17 + this.description.size() * 9), -5592406);
        }

        this.minecraft.getTextureManager().bind(SKILL_ICON_LOCATION);
        Draw.blit(matrixStack, x + this.x + 3, y + this.y, 16, 16, u, v, 32, 32, 256, 256);
        this.minecraft.font.drawShadow(matrixStack, this.skill.getLevel() + "", (float) (this.x + x + 15), (float) (this.y + y - 2), -1);
    }

    protected void render9Sprite(MatrixStack matrixStack, int x, int y, int w, int h, int p_238691_6_, int p_238691_7_, int p_238691_8_, int p_238691_9_, int p_238691_10_) {
        Draw.blit(matrixStack, x, y, p_238691_9_, p_238691_10_, p_238691_6_, p_238691_6_);
        this.renderRepeating(matrixStack, x + p_238691_6_, y, w - p_238691_6_ - p_238691_6_, p_238691_6_, p_238691_9_ + p_238691_6_, p_238691_10_, p_238691_7_ - p_238691_6_ - p_238691_6_, p_238691_8_);
        Draw.blit(matrixStack, x + w - p_238691_6_, y, p_238691_9_ + p_238691_7_ - p_238691_6_, p_238691_10_, p_238691_6_, p_238691_6_);
        Draw.blit(matrixStack, x, y + h - p_238691_6_, p_238691_9_, p_238691_10_ + p_238691_8_ - p_238691_6_, p_238691_6_, p_238691_6_);
        this.renderRepeating(matrixStack, x + p_238691_6_, y + h - p_238691_6_, w - p_238691_6_ - p_238691_6_, p_238691_6_, p_238691_9_ + p_238691_6_, p_238691_10_ + p_238691_8_ - p_238691_6_, p_238691_7_ - p_238691_6_ - p_238691_6_, p_238691_8_);
        Draw.blit(matrixStack, x + w - p_238691_6_, y + h - p_238691_6_, p_238691_9_ + p_238691_7_ - p_238691_6_, p_238691_10_ + p_238691_8_ - p_238691_6_, p_238691_6_, p_238691_6_);
        this.renderRepeating(matrixStack, x, y + p_238691_6_, p_238691_6_, h - p_238691_6_ - p_238691_6_, p_238691_9_, p_238691_10_ + p_238691_6_, p_238691_7_, p_238691_8_ - p_238691_6_ - p_238691_6_);
        this.renderRepeating(matrixStack, x + p_238691_6_, y + p_238691_6_, w - p_238691_6_ - p_238691_6_, h - p_238691_6_ - p_238691_6_, p_238691_9_ + p_238691_6_, p_238691_10_ + p_238691_6_, p_238691_7_ - p_238691_6_ - p_238691_6_, p_238691_8_ - p_238691_6_ - p_238691_6_);
        this.renderRepeating(matrixStack, x + w - p_238691_6_, y + p_238691_6_, p_238691_6_, h - p_238691_6_ - p_238691_6_, p_238691_9_ + p_238691_7_ - p_238691_6_, p_238691_10_ + p_238691_6_, p_238691_7_, p_238691_8_ - p_238691_6_ - p_238691_6_);
    }

    protected void renderIconBackground(MatrixStack matrixStack, int w, int h){

    }

    protected void renderRepeating(MatrixStack p_238690_1_, int p_238690_2_, int p_238690_3_, int p_238690_4_, int p_238690_5_, int p_238690_6_, int p_238690_7_, int p_238690_8_, int p_238690_9_) {
        for (int i = 0; i < p_238690_4_; i += p_238690_8_) {
            int j = p_238690_2_ + i;
            int k = Math.min(p_238690_8_, p_238690_4_ - i);

            for (int l = 0; l < p_238690_5_; l += p_238690_9_) {
                int i1 = p_238690_3_ + l;
                int j1 = Math.min(p_238690_9_, p_238690_5_ - l);
                Draw.blit(p_238690_1_, j, i1, p_238690_6_, p_238690_7_, k, j1);
            }
        }
    }

    // controller

//    public void setProgress(SkillTypeProgress p_191824_1_) {
//        this.progress = p_191824_1_;
//    }
//
//    @Nullable
//    private SkillEntryGui getFirstVisibleParent(SkillType skillType) {
//        skillType = skillType.getParents();
//        return skillType != null ? this.tab.getWidget(skillType) : null;
//    }


    @Override
    public boolean mouseClicked(double x, double y, int p_231044_5_) {
        ClientPlayerEntity player = this.minecraft.player;
        if (player == null) {
            return false;
        }
        Optional<Skill> s = this.skillType.getSkill(player);
        if (this.tab != this.tab.getScreen().getSelectedTab()) {
            return false;
        }

        if (s.isPresent()) {
            Skill sk = s.get();

            int xOffset = (this.tab.getScreen().width - 252) / 2;
            int yOffset = (this.tab.getScreen().height - 140) / 2;
            boolean mouseOver = this.isMouseOver(MathHelper.floor(this.tab.getScrollX()), MathHelper.floor(this.tab.getScrollY()),
                    (int) x - xOffset - 9, (int) y - yOffset - 18);
            if (mouseOver) {
                DyingLight.CHANNEL.sendToServer(new SkillLevelUpMessage(sk.type.getRegistryName()));
                sk.levelUp();
                return true;
            }
        }

        return false;
    }

    public boolean isMouseOver(int x, int y, int xRelative, int yReletive) {
        int i = x + this.x;
        int j = i + 26;
        int k = y + this.y;
        int l = k + 26;
        return xRelative >= i && xRelative <= j && yReletive >= k && yReletive <= l;

    }

    // helper

    public int getY() {
        return this.y;
    }

    public int getX() {
        return this.x;
    }
}
