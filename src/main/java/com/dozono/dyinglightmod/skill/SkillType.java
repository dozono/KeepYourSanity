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
    private final DisplayInfo displayInfo;
    private final int index;

    public SkillType(Builder builder) {
        this.dependencies = builder.deps;
        this.parent = builder.parent;
        this.displayInfo = builder.displayInfo;
    }

    public List<SkillType> getDependencies() {
        return dependencies;
    }

    public SkillType getParent() {
        return parent;
    }

    public DisplayInfo getDisplay() {
        return displayInfo;
    }

    public int getIndex() {
        return index;
    }

    public abstract void mount(PlayerEntity playerEntity, Skill skill);

    public abstract void onLevelUp(PlayerEntity player, Skill skill);

    public static class Builder {
        private List<SkillType> deps = new ArrayList<>();
        private SkillType parent;
        private DisplayInfo displayInfo;

        public static Builder create() {
            return new Builder();
        }

        public Builder setDisplayInfo(DisplayInfo displayInfo) {
            this.displayInfo = displayInfo;
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
