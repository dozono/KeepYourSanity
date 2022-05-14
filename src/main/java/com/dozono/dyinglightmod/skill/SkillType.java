package com.dozono.dyinglightmod.skill;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;

public abstract class SkillType extends ForgeRegistryEntry<SkillType> implements IForgeRegistryEntry<SkillType> {
    private final List<SkillType> dependencies;

    public SkillType(Builder builder) {
        this.dependencies = builder.deps;
    }

    public abstract void mount(PlayerEntity playerEntity, Skill skill);

    public static class Builder {
        private List<SkillType> deps = new ArrayList<>();

        public static Builder create() {
            return new Builder();
        }

        public Builder dependOn(SkillType skillType) {
            deps.add(skillType);
            return this;
        }
    }
}
