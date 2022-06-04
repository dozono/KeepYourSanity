package com.dozono.dyinglightmod.skill;

import net.minecraft.advancements.DisplayInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;

public abstract class SkillType extends ForgeRegistryEntry<SkillType> implements IForgeRegistryEntry<SkillType> {
    private final List<SkillType> dependencies;
    private final SkillType parent;
    /**
     * The index will affect the texture u, v of this skill
     */
    private final int index;

    public SkillType(Builder builder) {
        this.dependencies = builder.deps;
        this.parent = builder.parent;
        this.index = builder.index;
    }

    public List<SkillType> getDependencies() {
        return dependencies;
    }

    public SkillType getParent() {
        return parent;
    }

    public int getIndex() {
        return index;
    }

    public abstract void mount(PlayerEntity playerEntity, Skill skill);

    public abstract void onLevelUp(PlayerEntity player, Skill skill);

    public static class Builder {
        private List<SkillType> deps = new ArrayList<>();
        private SkillType parent;
        private int index;

        public static Builder create() {
            return new Builder();
        }

        public Builder setIndex(int index) {
            this.index = index;
            return this;
        }

        public Builder setParent(SkillType parent) {
            this.parent = parent;
            return this;
        }

        public Builder dependOn(SkillType skillType) {
            deps.add(skillType);
            return this;
        }
    }
}
