package com.dozono.dyinglightmod.skill;

import com.dozono.dyinglightmod.skill.combat.SkillTypeDisguise;
import com.dozono.dyinglightmod.skill.survival.*;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SkillContainer {
    private final List<Skill> skills = new ArrayList<>();
    private final PlayerEntity playerEntity;

    public SkillContainer(PlayerEntity entity) {
        playerEntity = entity;
        addSkill(SkillTypeLuck.INSTANCE);
        addSkill(SkillTypeDisguise.INSTANCE);
        addSkill(SkillTypePotionMaster.Instance);
        addSkill(SkillTypeGastrosoph.INSTANCE);
        addSkill(SkillTypeMandom.INSTANCE);
        addSkill(SkillTypeMender.Instance);
        addSkill(SkillTypeSmeltingMaster.Instance);
        //w8ing for test
        addSkill(SkillTypeToolMaster.Instance);
        addSkill(SkillTypeMiner.Instance);
        addSkill(SkillTypeLumberman.Instance);
    }

    public void upgrade() {

    }

    public void addSkill(SkillType type) {
        Skill e = new Skill(type);
        skills.add(e);
        type.mount(playerEntity, e);
    }

    public Optional<Skill> getSkill(SkillType type) {
        for (Skill skill : skills) {
            if (skill.type == type) {
                return Optional.of(skill);
            }
        }
        return Optional.empty();
    }

    @Nonnull
    public List<Skill> getRoots() {
        return skills;
    }
}
