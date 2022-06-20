package com.dozono.dyinglightmod.skill;

import com.dozono.dyinglightmod.skill.agility.SkillTypeDoubleJump;
import com.dozono.dyinglightmod.skill.agility.SkillTypeRunner;
import com.dozono.dyinglightmod.skill.agility.SkillTypeSwimmer;
import com.dozono.dyinglightmod.skill.combat.SkillTypeCamouflage;
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
//        addSkill(SkillTypeLuck.INSTANCE);
//        addSkill(SkillTypeCamouflage.INSTANCE);
//        addSkill(SkillTypePotionMaster.Instance);
//        addSkill(SkillTypeGastrosoph.INSTANCE);
//        addSkill(SkillTypeMandom.INSTANCE);
//        addSkill(SkillTypeMender.Instance);
//        addSkill(SkillTypeSmeltingMaster.Instance);
//        //w8ing for test
//        addSkill(SkillTypeToolMaster.Instance);
//        addSkill(SkillTypeMiner.Instance);
//        addSkill(SkillTypeLumberman.Instance);

        addSkill(SkillTypeDoubleJump.INSTANCE);
        addSkill(SkillTypeRunner.INSTANCE);
        addSkill(SkillTypeSwimmer.INSTANCE);
    }

    public void upgrade() {

    }

    public void addSkill(SkillType type) {
        Skill e = type.createSkill();
        skills.add(e);
        type.mount(playerEntity, e);
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
}
