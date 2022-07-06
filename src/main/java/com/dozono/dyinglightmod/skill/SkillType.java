package com.dozono.dyinglightmod.skill;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

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

    public Skill createSkill(PlayerEntity player) {
        return new Skill(this, player);
    }

    public void mount(PlayerEntity playerEntity, Skill skill) {
    }

    protected int getCost(int level) {
        if (level == 0) {
            return 50;
        }
        if (level == 1) {
            return 100;
        }
        if (level == 2) {
            return 200;
        }
        return -1;
    }

    public boolean onLevelUp(PlayerEntity player, Skill skill) {
        int totalExperience = player.totalExperience;
        for (SkillType parent : this.parents) {
            Optional<Skill> parentSkill = parent.getSkill(player);
            if (!parentSkill.isPresent() || parentSkill.get().getLevel() == 0) {
                return false;
            }
        }
        int cost = this.getCost(skill.getLevel());
        if (cost < 0) {
            return false;
        }
        if (totalExperience > cost) {
            player.giveExperiencePoints(-cost);
            return true;
        }
        return false;
    }

    public void readNBT(CompoundNBT compoundNBT, Skill skill) {
    }

    public void writeNBT(CompoundNBT compoundNBT, Skill skill) {
    }

    public Optional<Skill> getSkill(ICapabilityProvider provider) {
        return provider.getCapability(CapabilitySkillContainer).map((c) -> c.getSkill(this))
                .flatMap((v) -> v);
    }

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
