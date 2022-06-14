package com.dozono.dyinglightmod.gui;

import com.dozono.dyinglightmod.skill.SkillType;
import com.dozono.dyinglightmod.skill.survival.SkillTypeBackpacker;
import com.dozono.dyinglightmod.skill.survival.SkillTypeMandom;

import java.util.HashMap;

public class IconMapping {
    public static int getIndex(SkillType skillType) {
        HashMap<SkillType, Integer> skillTypeIntegerHashMap = new HashMap<>();

        skillTypeIntegerHashMap.put(SkillTypeMandom.INSTANCE, 0);
        skillTypeIntegerHashMap.put(SkillTypeBackpacker.INSTANCE, 2);
//        skillTypeIntegerHashMap.put(SkillTypeMandom.INSTANCE, 0);
//        skillTypeIntegerHashMap.put(SkillTypeMandom.INSTANCE, 0);
//        skillTypeIntegerHashMap.put(SkillTypeMandom.INSTANCE, 0);
//        skillTypeIntegerHashMap.put(SkillTypeMandom.INSTANCE, 0);
//        skillTypeIntegerHashMap.put(SkillTypeMandom.INSTANCE, 0);
//        skillTypeIntegerHashMap.put(SkillTypeMandom.INSTANCE, 0);

        Integer integer = skillTypeIntegerHashMap.get(skillType);
        if (integer == null) {
            return 0;
        }
        return integer;
    }
}
