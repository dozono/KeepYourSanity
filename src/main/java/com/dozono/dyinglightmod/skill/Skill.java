package com.dozono.dyinglightmod.skill;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

public class Skill {
    public final SkillType type;
    private final SkillContainer skillContainer;
    private final PlayerEntity player;

    private int level = 0;

    @OnlyIn(Dist.CLIENT)
    private boolean clientDirty = false;

    public Skill(SkillType type, SkillContainer skillContainer, PlayerEntity player) {
        this.type = type;
        this.skillContainer = skillContainer;
        this.player = player;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        this.skillContainer.markDirty();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::markClientDirty);
    }

    @OnlyIn(Dist.CLIENT)
    private void markClientDirty() {
        this.clientDirty = true;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isClientDirty() {
        return clientDirty;
    }

    public int getCost() {
        return this.type.getCost(level);
    }

    public void levelUp() {
        if (type.onLevelUp(player, this, this.level + 1)) {
            this.level += 1;
            this.skillContainer.sync();
        }
    }
}
