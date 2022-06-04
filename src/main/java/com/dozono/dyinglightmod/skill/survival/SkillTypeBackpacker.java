package com.dozono.dyinglightmod.skill.survival;

import com.dozono.dyinglightmod.skill.Skill;
import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.entity.player.PlayerEntity;

public class SkillTypeBackpacker extends SkillType {
    public static final SkillType INSTANCE = new SkillTypeBackpacker().setRegistryName("backpacker");

    public SkillTypeBackpacker() {
        super(Builder.create());
    }

    @Override
    public void mount(PlayerEntity playerEntity, Skill skill) {

    }

    @Override
    public void onLevelUp(PlayerEntity player, Skill skill) {

    }


}
