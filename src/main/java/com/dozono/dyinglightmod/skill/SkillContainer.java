package com.dozono.dyinglightmod.skill;

import com.dozono.dyinglightmod.DyingLight;
import com.dozono.dyinglightmod.skill.agility.SkillTypeDoubleJump;
import com.dozono.dyinglightmod.skill.agility.SkillTypeRunner;
import com.dozono.dyinglightmod.skill.agility.SkillTypeSwimmer;
import com.dozono.dyinglightmod.skill.agility.SkillTypeAquaMan;
import com.dozono.dyinglightmod.skill.combat.SkillTypeDeathDenied;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dozono.dyinglightmod.DyingLight.CapabilitySkillContainer;

public class SkillContainer implements ICapabilitySerializable<CompoundNBT> {
    private final List<Skill> skills = new ArrayList<>();
    private final PlayerEntity playerEntity;

    public SkillContainer(PlayerEntity player) {
        playerEntity = player;

        Collection<SkillType> allSkills = DyingLight.SKILL_REGISTRY.get().getValues();
        List<SkillType> roots = allSkills.stream().filter(s -> s.getParents().size() == 0).collect(Collectors.toList());

        for (SkillType type : allSkills) {
            Skill e = type.createSkill(player);
            skills.add(e);
            type.mount(playerEntity, e);
        }
    }

    public <T extends Skill> Optional<T> getSkill(SkillType type) {
        for (Skill skill : skills) {
            if (skill.type == type) {
                return Optional.of((T) skill);
            }
        }
        return Optional.empty();
    }

    @Nonnull
    public List<Skill> getRoots() {
        return skills;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilitySkillContainer) {
            return (LazyOptional<T>) LazyOptional.of(() -> this);
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        for (Skill skill : this.skills) {
            CompoundNBT seperateNBT = new CompoundNBT();
            skill.type.writeNBT(seperateNBT, skill);
            nbt.put(skill.type.getRegistryName().toString(), seperateNBT);

        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        for (Skill skill : this.skills) {
            if (nbt.contains(skill.type.getRegistryName().toString())) {
                CompoundNBT inbt = nbt.getCompound(skill.type.getRegistryName().toString());
                skill.type.readNBT(inbt, skill);
            }
        }
    }
}
