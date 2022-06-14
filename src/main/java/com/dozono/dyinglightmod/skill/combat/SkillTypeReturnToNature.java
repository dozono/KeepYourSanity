package com.dozono.dyinglightmod.skill.combat;

import com.dozono.dyinglightmod.skill.SkillType;

public class SkillTypeReturnToNature extends SkillType {
    public static final SkillTypeReturnToNature INSTANCE = new SkillTypeReturnToNature();
    public SkillTypeReturnToNature() {
        super(Builder.create().addParent(SkillTypeKnockBackResist.INSTANCE));
    }
}
