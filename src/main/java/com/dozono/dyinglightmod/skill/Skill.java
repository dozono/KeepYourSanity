package com.dozono.dyinglightmod.skill;

import net.minecraft.entity.player.PlayerEntity;

public class Skill {
    public final SkillType type;
    private SkillContainer skillContainer;
    private final PlayerEntity player;

    private int level = 0;

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
    }

    public int getCost() {
        return this.type.getCost(level);
    }

    public void levelUp() {
        if (type.onLevelUp(player, this)) {
            this.level += 1;
            this.skillContainer.markDirty();
        }
    }
}
