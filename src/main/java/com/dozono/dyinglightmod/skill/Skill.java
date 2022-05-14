package com.dozono.dyinglightmod.skill;

public class Skill {
    public final SkillType type;

    private int level = 1;

    public Skill(SkillType type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }
}
