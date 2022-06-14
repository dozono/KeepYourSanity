package com.dozono.dyinglightmod.skill;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;

public abstract class SkillType extends ForgeRegistryEntry<SkillType> implements IForgeRegistryEntry<SkillType> {
    private final List<SkillType> children;
    private final List<SkillType> parents;

    public SkillType(Builder builder) {
        this.parents = builder.parents;
        this.children = new ArrayList<>();
        for (SkillType parent : builder.parents) {
            parent.children.add(this);
        }
    }

    public List<SkillType> getChildren() {
        return children;
    }

    public List<SkillType> getParents() {
        return parents;
    }

    public Skill createSkill() {
        return new Skill(this);
    }

    public void mount(PlayerEntity playerEntity, Skill skill) {}

    public void onLevelUp(PlayerEntity player, Skill skill) {}

    public static class Builder {
        private final List<SkillType> parents = new ArrayList<>();

        public static Builder create() {
            return new Builder();
        }

        public Builder addParent(SkillType parent) {
            this.parents.add(parent);
            return this;
        }
    }
}
