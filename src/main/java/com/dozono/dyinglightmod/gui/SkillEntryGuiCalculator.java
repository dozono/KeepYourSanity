package com.dozono.dyinglightmod.gui;

import com.dozono.dyinglightmod.skill.SkillType;
import net.minecraft.client.Minecraft;

import java.util.*;
import java.util.stream.Collectors;

public class SkillEntryGuiCalculator {
    public static SkillEntryGui compute(Minecraft mc, SkillTabGui tab, SkillType skillType, Map<SkillType, SkillEntryGui> registry) {
        List<SkillEntryGui> children = skillType.getChildren().size() == 0
                ? Collections.emptyList()
                : skillType.getChildren().stream().map((d) -> compute(mc, tab, d, registry)).collect(Collectors.toList());
        SkillEntryGui gui = new SkillEntryGui(mc, tab, skillType, children, 0, 0);
        registry.put(skillType, gui);
        return gui;
    }
//    public static int getParent(SkillType skillType){
//        skillType.getParents()
//    }
}
